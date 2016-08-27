package de.plpt.phpmyadmin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.Streams;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Pascal on 29.07.2016.
 */
public class DataTable implements Serializable,Iterable<List<String>> {


    @SerializedName("sql")
    @Expose
    private String sql;
    @SerializedName("columns")
    @Expose
    private List<String> columns = new ArrayList<String>();
    @SerializedName("rows")
    @Expose
    private List<List<String>> rows = new ArrayList<List<String>>();
    @SerializedName("rowCount")
    @Expose
    private Integer rowCount;
    @SerializedName("columnCount")
    @Expose
    private Integer columnCount;

    /**
     *
     * @return
     * The sql
     */
    public String getSql() {
        return sql;
    }

    /**
     *
     * @param sql
     * The sql
     */
    public void setSql(String sql) {
        this.sql = sql;
    }

    /**
     *
     * @return
     * The columns
     */
    public List<String> getColumns() {
        return columns;
    }

    /**
     *
     * @param columns
     * The columns
     */
    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    /**
     *
     * @return
     * The rows
     */
    public List<List<String>> getRows() {
        return rows;
    }

    /**
     *
     * @param rows
     * The rows
     */
    public void setRows(List<List<String>> rows) {
        this.rows = rows;
    }

    /**
     *
     * @return
     * The rowCount
     */
    public Integer getRowCount() {
        return rowCount;
    }

    /**
     *
     * @param rowCount
     * The rowCount
     */
    public void setRowCount(Integer rowCount) {
        this.rowCount = rowCount;
    }

    /**
     *
     * @return
     * The columnCount
     */
    public Integer getColumnCount() {
        return columnCount;
    }

    /**
     *
     * @param columnCount
     * The columnCount
     */
    public void setColumnCount(Integer columnCount) {
        this.columnCount = columnCount;
    }

    public String getValue(int row,int column)
    {
        return rows.get(row).get(column);
    }

    public String getValue(int row,String columnName)
    {
        int idx = columns.indexOf(columnName);
        return getValue(row,idx);
    }

    @Override
    public Iterator<List<String>> iterator() {
        return rows.iterator();
    }



}
