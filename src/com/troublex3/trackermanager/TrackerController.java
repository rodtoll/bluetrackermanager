package com.troublex3.trackermanager;

import java.io.IOException;
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

    static {
        store = new TrackerStore();
        try {
            store.load();
        } catch(IOException except) {

        }
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

    public static Boolean markNodeHeartbeatSeen(String nodeName, Date dateToUse) {
        TrackerNode node = store.getNode(nodeName);
        if(node != null) {
            node.setLastHeartbeat(dateToUse);
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
            store.updateNode(node);
            return true;
        } else {
            return false;
        }
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
        if(device == null) {
            device = new TrackerDevice(deviceAddress, deviceAddress, timeStamp, null, 4, 60);
            store.addDevice(device);
        } else {
            device.setLastReading(reading);
            store.updateDevice(device);
        }
    }

    public static SortedSet<TrackerReading> getDeviceReadings(String nodeId) {
        return store.getDeviceReadings(nodeId);
    }
}
