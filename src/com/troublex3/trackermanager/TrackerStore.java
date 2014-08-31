package com.troublex3.trackermanager;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;
import java.io.*;
import com.google.gson.*;

/**
 * Created by rodtoll on 7/25/14.
 */
public class TrackerStore {

    private List<TrackerDevice> devices = new ArrayList<TrackerDevice>();
    private List<TrackerNode> nodes = new ArrayList<TrackerNode>();

    private HashMap<String,SortedSet<TrackerReading>> readingsByDevice = new HashMap<String, SortedSet<TrackerReading>>();
    private HashMap<String,SortedSet<TrackerReading>> readingsByNode = new HashMap<String, SortedSet<TrackerReading>>();

    public TrackerNode getNode(String nodeId) {
        synchronized(nodes) {
            TrackerNode node = internalGetNode(nodeId);
            if(node == null) {
                return null;
            } else {
                return new TrackerNode(node);
            }
        }
    }

    protected TrackerNode internalGetNode(String nodeId) {
        synchronized (nodes) {
            for(TrackerNode node : nodes) {
                if(nodeId.contentEquals(node.getAddress()) || nodeId.contentEquals(node.getNodeId())) {
                    return node;
                }
            }
        }
        return null;
    }

    public List<TrackerNode> getNodeList() {
        ArrayList<TrackerNode> results = new ArrayList<TrackerNode>();
        synchronized (nodes) {
            for (TrackerNode node : nodes) {
                results.add(new TrackerNode(node));
            }
        }
        return results;
    }

    public TrackerDevice getDevice(String deviceId) {
        synchronized (devices) {
            TrackerDevice device = internalGetDevice(deviceId);
            if(device == null) {
                return null;
            } else {
                return new TrackerDevice(device);
            }
        }
    }

    protected TrackerDevice internalGetDevice(String deviceId) {
        synchronized(devices) {
            for (TrackerDevice device : devices) {
                if (deviceId.contentEquals(device.getAddress()) || deviceId.contentEquals(device.getFriendlyName())) {
                    return device;
                }
            }
        }
        return null;
    }

    public List<TrackerDevice> getDeviceList() {
        ArrayList<TrackerDevice> results = new ArrayList<TrackerDevice>();
        synchronized (devices) {
            for (TrackerDevice device : devices) {
                results.add(new TrackerDevice(device));
            }
        }
        return results;
    }

    public Boolean updateNode(TrackerNode node) {
        synchronized(nodes) {
            TrackerNode internalNode = internalGetNode(node.getNodeId());
            if(internalNode == null) {
                return false;
            }
            internalNode.setFrom(node);
            return true;
        }
    }

    public Boolean updateDevice(TrackerDevice device) {
        synchronized(devices) {
            TrackerDevice internalDevice = internalGetDevice(device.getAddress());
            if(internalDevice == null) {
                return false;
            }
            internalDevice.setFrom(device);
            return true;
        }
    }

    public Boolean addDevice(TrackerDevice device) {
        synchronized (devices) {
            if(getDevice(device.getAddress()) != null) {
                return false;
            } else {
                devices.add(device);
                return true;
            }
        }
    }

    public Boolean addNode(TrackerNode node) {
        synchronized (nodes) {
            if(getNode(node.getNodeId()) != null) {
                return false;
            } else {
                nodes.add(node);
                return true;
            }
        }
    }

    public SortedSet<TrackerReading> getNodeReadings(String nodeId) {
        synchronized(readingsByDevice) {
            SortedSet<TrackerReading> results = new TreeSet<TrackerReading>();
            for(TrackerReading reading : readingsByNode.get(nodeId)) {
                results.add(new TrackerReading(reading));
            }
            return results;
        }
    }

    public SortedSet<TrackerReading> getDeviceReadings(String deviceAddress) {
        synchronized(readingsByDevice) {
            TrackerDevice device = internalGetDevice(deviceAddress);
            if(device == null) {
                return null;
            }
            SortedSet<TrackerReading> currentList = readingsByDevice.get(device.getAddress());
            if(currentList == null) {
                return null;
            }
            SortedSet<TrackerReading> results = new TreeSet<TrackerReading>();
            for(TrackerReading reading : currentList) {
                results.add(new TrackerReading(reading));
            }
            return results;
        }
    }

    public void addReading(TrackerReading reading) {
        synchronized (readingsByDevice) {
            SortedSet<TrackerReading> byDevice = readingsByDevice.get(reading.getAddress());
            if (byDevice == null) {
                byDevice = new TreeSet<TrackerReading>();
                readingsByDevice.put(reading.getAddress(), byDevice);
            }
            SortedSet<TrackerReading> byNode = readingsByNode.get(reading.getNodeName());
            if (byNode == null) {
                byNode = new TreeSet<TrackerReading>();
                readingsByNode.put(reading.getNodeName(), byNode);
            }

            byDevice.add(reading);
            if (byDevice.size() > Constants.READINGS_MAX_COUNT) {
                byDevice.remove(byDevice.first());
            }

            byNode.add(reading);
            if (byNode.size() > Constants.READINGS_MAX_COUNT) {
                byNode.remove(byNode.first());
            }
        }
    }

    public void load() throws IOException  {

        Gson gson = new Gson();
        TrackerConfiguration configuration = gson.fromJson(new FileReader("tracker-network.json"),TrackerConfiguration.class);

        for(TrackerDevice device : configuration.devices) {
            addDevice(device);
        }

        for(TrackerNode node : configuration.nodes) {
            addNode(node);
        }
    }
}
