package com.troublex3.trackermanager;

import com.sun.javafx.collections.transformation.SortedList;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by rodtoll on 7/25/14.
 */
public class TrackerStore {

    private static HashMap<String,TrackerDevice> devices = new HashMap<String, TrackerDevice>();
    private static HashMap<String,TrackerNode> nodes = new HashMap<String, TrackerNode>();
    private static HashMap<String,SortedSet<TrackerReading>> readingsByDevice = new HashMap<String, SortedSet<TrackerReading>>();
    private static HashMap<String,SortedSet<TrackerReading>> readingsByNode = new HashMap<String, SortedSet<TrackerReading>>();
    private static HashMap<String,String> friendlyNames = new HashMap<String, String>();

    public static String translateFriendlyToAddressIfPossible(String address) {
        if(friendlyNames.containsValue(address)) {
            for(Map.Entry<String,String> entry: friendlyNames.entrySet()) {
                if(entry.getValue().contentEquals(address)) {
                    return entry.getKey();
                }
            }
        }
        return address;
    }

    public static String translateFriendlyToAddress(String address) {
        if(friendlyNames.containsKey(address)) {
            return friendlyNames.get(address);
        } else {
            return address;
        }
    }

    public static TrackerNode getNode(String nodeId) {
        if(nodes.containsKey(nodeId)) {
            return nodes.get(nodeId);
        } else {
            return null;
        }
    }

    public static List<TrackerNode> getNodeList() {
        return new ArrayList<TrackerNode>(nodes.values());
    }

    public static TrackerDevice getDevice(String deviceId) {
        String addressToUse = translateFriendlyToAddress(deviceId);
        if(devices.containsKey(addressToUse)) {
            return devices.get(addressToUse);
        } else {
            return null;
        }
    }

    public static void updateNode(TrackerNode node) {
        // No-op
    }

    public static void updateDevice(TrackerDevice device) {
        // No-op
    }

    public static void addDevice(TrackerDevice device) {
        devices.put(device.getAddress(), device);
    }

    public static TrackerDevice createDevice(String address, TrackerReading initialReading) {
        TrackerDevice newDevice = new TrackerDevice();
        newDevice.setAddress(address);
        newDevice.setFriendlyName(translateFriendlyToAddress(address));
        newDevice.setLastReading(initialReading);
        return newDevice;
    }

    public static SortedSet<TrackerReading> getNodeReadings(String nodeId) {
        return readingsByNode.get(nodeId);
    }

    public static SortedSet<TrackerReading> getDeviceReadings(String deviceId) {
        return readingsByDevice.get(deviceId);
    }

    public static void addReading(TrackerReading reading) {
        SortedSet<TrackerReading> byDevice = readingsByDevice.get(reading.getAddress());
        if(byDevice == null) {
            byDevice = new TreeSet<TrackerReading>();
            readingsByDevice.put(reading.getAddress(), byDevice);
        }
        SortedSet<TrackerReading> byNode = readingsByDevice.get(reading.getNodeName());
        if(byNode == null) {
            byNode = new TreeSet<TrackerReading>();
            readingsByNode.put(reading.getNodeName(),byNode);
        }

        byDevice.add(reading);
        if(byDevice.size() > Constants.READINGS_MAX_COUNT) {
            byDevice.remove(byDevice.first());
        }

        byNode.add(reading);
        if(byNode.size() > Constants.READINGS_MAX_COUNT) {
            byNode.remove(byDevice.first());
        }
    }

    public static List<TrackerDevice> getDeviceList() {
        return new ArrayList<TrackerDevice>(devices.values());
    }

    static {
        load();
    }

    public static void load() {
        friendlyNames.clear();

        friendlyNames.put("E6:18:EB:4A:FE:D1", "Miley Collar");
        friendlyNames.put("F0:B2:48:5A:8A:49", "Joanne Truck");
        friendlyNames.put("D4:AD:89:B6:D5:C9", "Rod Fitbit");
        friendlyNames.put("00:07:80:71:E9:29", "Radius USB Beacon");
        friendlyNames.put("90:59:AF:0B:84:DE", "SensorTag Pink");
        friendlyNames.put("90:59:AF:0B:85:10", "SensorTag O");
        friendlyNames.put("E1:07:2C:6F:9A:B2", "Jo Fitbit");
        friendlyNames.put("D1:75:8B:D0:21:6E", "Radius BeaconTag");
        friendlyNames.put("E3:26:26:CA:8C:12", "Infiniti Est2DB");
        friendlyNames.put("F0:A5:E2:21:8A:39", "Estimote T1");
        friendlyNames.put("F3:68:2F:8C:7D:79", "Estimote LB3");
        friendlyNames.put("30:14:4A:3C:F2:0F", "MainFloor TV");
        friendlyNames.put("10.0.1.165", "Rod MotoX");
        friendlyNames.put("10.0.1.67", "Jo Galaxy S5");

        String [] PreConfiguredNodes = {
            "macbookpro",
            "pb1-basement",
            "pb3-playroom",
            "pbmaster-office",
            "rainbow-frontdoor",
            "smoky-garage",
            "white-tvroom",
            "twilio"
        };

        for(String nodeName : PreConfiguredNodes) {
            TrackerNode node = new TrackerNode();
            node.setNodeId(nodeName);
            nodes.put(nodeName,node);
        }
    }
}
