package com.troublex3.trackermanager;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.Map;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.PreparedQuery;

/**
 * Created by rodtoll on 7/25/14.
 */
public class TrackerStore {

    private static HashMap<String,TrackerNode> trackerNodes = new HashMap<String,TrackerNode>();
    private static HashMap<String, SortedSet<TrackerReading>> trackerReadings = new HashMap<String, SortedSet<TrackerReading>>();
    private static HashMap<String, String> addressToFriendly = new HashMap<String, String>();
    private static HashMap<String, TrackerDevice> devicesSeen = new HashMap<String,TrackerDevice>();

    public static TrackerNode getNode(String id) {
        if(trackerNodes.containsKey(id)) {
            return trackerNodes.get(id);
        } else {
            return null;
        }
    }

    public static void flush() {
        /*DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        for(Map.Entry<String,TrackerNode> entry : trackerNodes.entrySet()) {
            SortedSet<TrackerReading> readings = trackerReadings.get(entry.getKey());
            synchronized (readings) {
                ArrayList<Entity> entitiesToWrite = new ArrayList<Entity>();
                for (TrackerReading reading : readings) {
                    Entity newEntity = new Entity(Constants.READINGS_ENTITY_NAME);
                    newEntity.setProperty(Constants.READINGS_ENTITY_NODENAME_PROPERTY, entry.getKey());
                    newEntity.setProperty(Constants.READINGS_ENTITY_ADDRESS_PROPERTY, reading.getAddress());
                    newEntity.setProperty(Constants.READINGS_ENTITY_SIGNALSTRENGTH_PROPERTY, reading.getSignalStrength());
                    newEntity.setProperty(Constants.READINGS_ENTITY_TIMESTAMP_PROPERTY, reading.getTimeStamp());
                    entitiesToWrite.add(newEntity);
                }
                datastore.put(entitiesToWrite);
                readings.clear();
            }
        }*/
    }

    public static void updateNode(TrackerNode node) {
        // No op because the node you are updating IS the node when using the in-memory store
    }

    static {
        load();
    }

    public static List<TrackerNode> getNodeList() {
        return new ArrayList<TrackerNode>(trackerNodes.values());
    }

    public static void addReading(TrackerNode node, TrackerReading reading) {
        SortedSet<TrackerReading> readingSet = trackerReadings.get(node.getNodeId());
        synchronized (readingSet) {
            readingSet.add(reading);
            // Keep us below the maximum number of readings
            if(readingSet.size() > Constants.READINGS_MAX_COUNT) {
                readingSet.remove(readingSet.first());
            }
            TrackerDevice device = null;

            if(!devicesSeen.containsKey(reading.getAddress())) {
                device = new TrackerDevice();
                device.setAddress(reading.getAddress());
                device.setFriendlyName(TrackerStore.translateAddressIfPossible(reading.getAddress()));
                devicesSeen.put(reading.getAddress(), device);
            } else {
                device = devicesSeen.get(reading.getAddress());
            }
            device.updateSeenMap(node.getNodeId(),new Date());
        }
    }

    public static Collection<TrackerDevice> getDevicesSeen() {
        return devicesSeen.values();
    }

    public static TrackerDevice getDevice(String addressOrFriendlyName) {
        String nameToUse = translateFriendlyToAddressIfPossible(addressOrFriendlyName);
        return devicesSeen.get(nameToUse);
    }

/*    public static void deleteReading(TrackerNode node, UUID readingId) {
        HashMap<UUID, TrackerReading> readingSet = trackerReadings.get(node.getNodeId());
        if(readingSet != null) {
            synchronized (readingSet) {
                if (readingSet.containsKey(readingId)) {
                    readingSet.remove(readingId);
                }
            }
        }
    } */

    public static List<TrackerReading> getReadings(TrackerNode node) {
        return getReadings(node, null);
    }

    public static List<TrackerReading> getReadings(TrackerNode node, String address) {
        //DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        ArrayList<TrackerReading> readings = new ArrayList<TrackerReading>();

        SortedSet<TrackerReading> results = TrackerStore.trackerReadings.get(node.getNodeId());
        for(TrackerReading reading : results) {
            if(address != null) {
                if(!reading.getAddress().contentEquals(address)) {
                    continue;
                }
            }
            readings.add(reading);
        }

        /*Filter isOfNode = new FilterPredicate(Constants.READINGS_ENTITY_NODENAME_PROPERTY,
                FilterOperator.EQUAL,
                node.getNodeId());

        Query query = new Query(Constants.READINGS_ENTITY_NAME).setFilter(isOfNode);
        PreparedQuery pq = datastore.prepare(query);

        for( Entity result : pq.asIterable()) {
            String readingAddress = (String) result.getProperty(Constants.READINGS_ENTITY_ADDRESS_PROPERTY);
            // If there is an address filter, make sure this reading is for the specified address
            if(address != null) {
                if(!address.contentEquals(readingAddress)) {
                    continue;
                }
            }
            TrackerReading reading = new TrackerReading();
            reading.setAddress(readingAddress);
            reading.setTimeStamp((Date) result.getProperty(Constants.READINGS_ENTITY_TIMESTAMP_PROPERTY));
            reading.setSignalStrength((Double) result.getProperty(Constants.READINGS_ENTITY_SIGNALSTRENGTH_PROPERTY));
            readings.add(reading);
        }

        SortedSet<TrackerReading> readingSet = trackerReadings.get(node.getNodeId());*/

       /* synchronized (readingSet) {
            readings.addAll(readingSet);
        }*/

        return readings;
    }

    public static String translateAddressIfPossible(String address) {
        if(addressToFriendly.containsKey(address)) {
            return addressToFriendly.get(address);
        } else {
            return address;
        }
    }

    public static String translateFriendlyToAddressIfPossible(String address) {
        if(addressToFriendly.containsValue(address)) {
            for(Map.Entry<String,String> entry: addressToFriendly.entrySet()) {
                if(entry.getValue().contentEquals(address)) {
                    return entry.getKey();
                }
            }
        }
        return address;
    }

    public static void load() {
        trackerNodes.clear();
        addressToFriendly.clear();

        addressToFriendly.put("E6:18:EB:4A:FE:D1", "Miley Collar");
        addressToFriendly.put("F0:B2:48:5A:8A:49", "Joanne Truck");
        addressToFriendly.put("D4:AD:89:B6:D5:C9", "Rod Fitbit");
        addressToFriendly.put("00:07:80:71:E9:29", "Radius USB Beacon");
        addressToFriendly.put("90:59:AF:0B:84:DE", "SensorTag Pink");
        addressToFriendly.put("90:59:AF:0B:85:10", "SensorTag O");
        addressToFriendly.put("E1:07:2C:6F:9A:B2", "Jo Fitbit");
        addressToFriendly.put("D1:75:8B:D0:21:6E", "Radius BeaconTag");
        addressToFriendly.put("E3:26:26:CA:8C:12", "Infiniti Est2DB");
        addressToFriendly.put("F0:A5:E2:21:8A:39", "Estimote T1");
        addressToFriendly.put("F3:68:2F:8C:7D:79", "Estimote LB3");
        addressToFriendly.put("30:14:4A:3C:F2:0F", "MainFloor TV");
        addressToFriendly.put("10.0.1.165", "Rod MotoX");
        addressToFriendly.put("10.0.1.67", "Jo Galaxy S5");

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
            trackerReadings.put(nodeName, new TreeSet<TrackerReading>());
            trackerNodes.put(nodeName, node);
        }
    }
}
