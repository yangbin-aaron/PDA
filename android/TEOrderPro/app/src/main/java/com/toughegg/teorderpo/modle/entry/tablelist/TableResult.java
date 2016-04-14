package com.toughegg.teorderpo.modle.entry.tablelist;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Andy on 15/11/27.
 */
public class TableResult implements Serializable {
    private int status;
    private String message;
    private List<TableResultData> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<TableResultData> getData() {
        return data;
    }

    public void setData(List<TableResultData> data) {
        this.data = data;
    }

    @Override
    public String toString () {
        return "TableResult{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
