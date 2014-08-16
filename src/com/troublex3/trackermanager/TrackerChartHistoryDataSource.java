package com.troublex3.trackermanager;

import com.google.visualization.datasource.DataSourceServlet;
import com.google.visualization.datasource.base.TypeMismatchException;
import com.google.visualization.datasource.datatable.ColumnDescription;
import com.google.visualization.datasource.datatable.DataTable;
import com.google.visualization.datasource.datatable.TableCell;
import com.google.visualization.datasource.datatable.TableRow;
import com.google.visualization.datasource.datatable.value.DateTimeValue;
import com.google.visualization.datasource.datatable.value.ValueType;
import com.google.visualization.datasource.query.Query;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.GregorianCalendar;

import javafx.util.Pair;
import sun.awt.SunHints;

/**
 * Created by rodtoll on 8/9/14.
 */
public class TrackerChartHistoryDataSource extends DataSourceServlet {

    @Override
    public DataTable generateDataTable(Query query, HttpServletRequest request) throws TypeMismatchException{

        DataTable data = new DataTable();
        data.addColumn(new ColumnDescription("readingtime", ValueType.DATETIME, "Reading Timestamp"));
        data.addColumn(new ColumnDescription("node", ValueType.TEXT, "Node"));
        data.addColumn(new ColumnDescription("device", ValueType.TEXT, "Device"));
        data.addColumn(new ColumnDescription("reading", ValueType.NUMBER, "Reading"));

        List<TrackerNode> nodes = TrackerStore.getNodeList();

        for(TrackerNode node : nodes) {
            SortedSet<TrackerReading> readings = TrackerStore.getNodeReadings(node.getNodeId());
            for(TrackerReading reading : readings) {
                TableRow row = new TableRow();
                GregorianCalendar calendar = new GregorianCalendar();
                Date date = reading.getTimeStamp();
                row.addCell(new DateTimeValue(
                    date.getYear()+1900,
                    date.getMonth(),
                    date.getDay(),
                    date.getHours(),
                    date.getMinutes(),
                    date.getSeconds(),
                    0
                ));
                row.addCell(node.getNodeId());
                row.addCell(reading.getAddress());
                row.addCell(reading.getSignalStrengthPositive());
                data.addRow(row);
            }
        }
        return data;
    }

    @Override
    protected boolean isRestrictedAccessMode() {
        return false;
    }
}
