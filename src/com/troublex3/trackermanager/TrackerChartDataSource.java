package com.troublex3.trackermanager;

import com.google.visualization.datasource.DataSourceServlet;
import com.google.visualization.datasource.base.TypeMismatchException;
import com.google.visualization.datasource.datatable.ColumnDescription;
import com.google.visualization.datasource.datatable.DataTable;
import com.google.visualization.datasource.datatable.TableCell;
import com.google.visualization.datasource.datatable.TableRow;
import com.google.visualization.datasource.datatable.value.ValueType;
import com.google.visualization.datasource.query.Query;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Date;
import javafx.util.Pair;
import sun.awt.SunHints;
import java.util.Collection;

import java.util.Map;
/**
 * Created by rodtoll on 7/27/14.
 */

public class TrackerChartDataSource extends DataSourceServlet {

    @Override
    public DataTable generateDataTable(Query query, HttpServletRequest request) {
/*
        Date currentTime = new Date();

        List<TrackerNode> nodes = TrackerStore.getNodeList();
        List<TrackerDevice> devices = new ArrayList<TrackerDevice>(TrackerStore.getDeviceList());

        DataTable data = new DataTable();
        data.addColumn(new ColumnDescription("address", ValueType.TEXT, "Address"));

        for(TrackerNode node : nodes){
            data.addColumn(new ColumnDescription(node.getNodeId(), ValueType.NUMBER, node.getNodeId()));
        }

        // Allocate 2x2 array to store values
        Double [][] dataMap = new Double[nodes.size()][devices.size()];
        Integer column = 0;
        for(TrackerNode node : nodes) {
            for(TrackerReading reading : TrackerStore.getNodeReadings(node.getNodeId())) {
                for(Integer row = 0; row < devices.size(); row++) {
                    if(devices.get(row).getAddress().contentEquals(reading.getAddress())) {
                        long diffInSeconds = (currentTime.getTime() - reading.getTimeStamp().getTime()) / 1000;
                        if(diffInSeconds < 30) {
                            dataMap[column][row] = reading.getSignalStrength();
                        }
                        break;
                    }
                }
            }
            column++;
        }

        for (Integer rowId = 0; rowId < devices.size(); rowId++) {
            TableRow newRow = new TableRow();
            newRow.addCell(devices.get(rowId).getFriendlyName());
            for (Integer columnId = 0; columnId < nodes.size(); columnId++) {
                if(dataMap[columnId][rowId] != null) {
                    newRow.addCell(100+dataMap[columnId][rowId]);
                } else {
                    newRow.addCell(0);
                }
            }
            try {
                data.addRow(newRow);
            } catch(TypeMismatchException e) {

            }
        }

        return data;*/
        return new DataTable();
    }

    @Override
    protected boolean isRestrictedAccessMode() {
        return false;
    }
}

