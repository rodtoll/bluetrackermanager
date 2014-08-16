package com.troublex3.trackermanager;

import java.util.Date;
import com.troublex3.trackermanager.TrackerDevice;
import com.troublex3.trackermanager.TrackerReading;

/**
 * Created by rodtoll on 8/16/14.
 */
public class TrackerShared {

    public static void addReading(String nodeId, String deviceAddress, Double valueToWrite ) {

        TrackerNode node = TrackerStore.getNode(nodeId);
        if(node == null) {
            return;
        }

        Date timeStamp = new Date();

        // Construct a reading object
        TrackerReading reading = new TrackerReading();
        reading.setAddress(deviceAddress);
        reading.setSignalStrength(valueToWrite.doubleValue());
        reading.setTimeStamp(timeStamp);
        reading.setNodeName(nodeId);

        // Add the reading to the store
        TrackerStore.addReading(reading);

        // Note the last reading timestamp to the node and write it out
        node.setLastReading(timeStamp);
        TrackerStore.updateNode(node);

        // Note the last reading value to the device, create device if needed
        TrackerDevice device = TrackerStore.getDevice(deviceAddress);
        if(device == null) {
            device = TrackerStore.createDevice(deviceAddress, reading);
            TrackerStore.addDevice(device);
        } else {
            device.setLastReading(reading);
            TrackerStore.updateDevice(device);
        }
    }
}
