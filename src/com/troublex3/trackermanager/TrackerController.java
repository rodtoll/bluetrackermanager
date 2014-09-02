package com.troublex3.trackermanager;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

/**
 * Acts as controller linking data store to the business logic. The store should be pure storage. All higher level
 * logic should be in this class. This class is threadsafe.
 */
public class TrackerController {

    private static TrackerStore store;
    private static TrackerIsyClient isyClient;
    private static Date lastBackgroundCheck;
    private static Object backgroundLock;

    static {
        store = new TrackerStore();
        try {
            store.load();
        } catch(IOException except) {

        }

        isyClient = new TrackerIsyClient("admin", "ErgoFlat91", "10.0.1.19");
        lastBackgroundCheck = new Date();
        backgroundLock = new Object();
    }

    public static List<TrackerDevice> getDevices() {
        return store.getDeviceList();
    }

    public static TrackerDevice getDevice(String deviceAddress) {
        return store.getDevice(deviceAddress);
    }

    public static TrackerNode getNode(String nodeId) {
        return store.getNode(nodeId);
    }

    public static ArrayList<TrackerNode> getNodes() {
        return new ArrayList<TrackerNode>(store.getNodeList());
    }

    protected static Boolean setIsyVariable(Integer variableId, Integer valueToSet) {
        try {
            isyClient.setVariable(variableId, valueToSet);
            return true;
        }
        catch(MalformedURLException e) { return false; }
        catch(IOException e) { return false; }
    }

    public static Boolean markNodeHeartbeatSeen(String nodeName, Date dateToUse) {
        TrackerNode node = store.getNode(nodeName);
        if(node != null) {
            node.setLastHeartbeat(dateToUse);
            sendUpdateIfNeeded(node, dateToUse);
            store.updateNode(node);
            return true;
        } else {
            return false;
        }
    }

    public static Boolean markNodeReadingSeen(String nodeName, Date dateToUse) {
        TrackerNode node = store.getNode(nodeName);
        if(node != null) {
            node.setLastReading(dateToUse);
            sendUpdateIfNeeded(node, dateToUse);
            store.updateNode(node);
            return true;
        } else {
            return false;
        }
    }

    private static Boolean sendUpdateIfNeeded(TrackerDevice device, Date timeStamp) {
        synchronized(lastBackgroundCheck) {
            Boolean isDevicePresent = device.checkPresence(timeStamp);
            if (device.getIsPresent() != isDevicePresent) {
                device.setIsPresent(isDevicePresent);
                if (device.getIsyVariableId() != 0) {
                    setIsyVariable(device.getIsyVariableId(), (isDevicePresent) ? 1 : 0);
                }
                return true;
            }
        }
        return false;
    }

    private static Boolean sendUpdateIfNeeded(TrackerNode node, Date timeStamp) {
        synchronized(lastBackgroundCheck) {
            Boolean isNodePresent = node.checkPresence(timeStamp);
            if (node.getIsPresent() != isNodePresent) {
                node.setIsPresent(isNodePresent);
                if (node.getIsyVariableId() != 0) {
                    setIsyVariable(node.getIsyVariableId(), (isNodePresent) ? 1 : 0);
                }
                return true;
            }
        }
        return false;
    }

    public static void addReading(String nodeId, String deviceAddress, Double valueToWrite ) {
        Date timeStamp = new Date();

        if(!markNodeReadingSeen(nodeId, timeStamp))
            return;

        // Construct a reading object
        TrackerReading reading = new TrackerReading(timeStamp, deviceAddress, valueToWrite, nodeId);

        // Add the reading to the store
        store.addReading(reading);

        // Note the last reading value to the device, create device if needed
        TrackerDevice device = store.getDevice(deviceAddress);

        Boolean newDevice = false;

        if(device == null) {
            device = new TrackerDevice(deviceAddress, deviceAddress, timeStamp, 0, 4, 60);
            newDevice = true;
        }

        device.setLastReading(reading);

        sendUpdateIfNeeded(device, timeStamp);

        if(newDevice) {
            store.addDevice(device);
        } else {
            store.updateDevice(device);
        }
        doPresenceCheckIfNeeded(timeStamp);
    }

    public static SortedSet<TrackerReading> getDeviceReadings(String nodeId) {
        return store.getDeviceReadings(nodeId);
    }

    public static void doPresenceCheckIfNeeded(Date nowTime) {
        synchronized(lastBackgroundCheck) {
            long timeDelta = nowTime.getTime() - lastBackgroundCheck.getTime();
            long timeDeltaInS = timeDelta / 1000;
            if(timeDeltaInS > 35) {
                ArrayList<TrackerNode> nodes = getNodes();
                for(TrackerNode node : nodes) {
                    if(sendUpdateIfNeeded(node, nowTime)) {
                        store.updateNode(node);
                    }
                }

                List<TrackerDevice> devices = getDevices();
                for(TrackerDevice device : devices) {
                    if(sendUpdateIfNeeded(device, nowTime)) {
                        store.updateDevice(device);
                    }
                }
                lastBackgroundCheck = nowTime;
            }
        }
    }
}
