package com.illumino.domain;

import com.opencsv.bean.CsvBindByName;

public class LookupTableData {
    @CsvBindByName(column = "dstport")
    private int dstPort;
    @CsvBindByName(column = "protocol")
    private String protocol;
    @CsvBindByName(column = "tag")
    private String tag;

    public int getDstPort() {
        return dstPort;
    }

    public void setDstPort(int dstPort) {
        this.dstPort = dstPort;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }



    @Override
    public String toString() {
        return "LookupTableData{" +
                "dstPort=" + dstPort +
                ", protocol='" + protocol + '\'' +
                ", tag='" + tag + '\'' +
                '}';
    }

}
