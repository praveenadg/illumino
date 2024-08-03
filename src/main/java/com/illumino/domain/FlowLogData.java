package com.illumino.domain;

import com.opencsv.bean.CsvBindByName;

public class FlowLogData {

    @CsvBindByName(column = "version")
    private int version;
    @CsvBindByName(column = "vpc-id")
    private String vpcId;
    @CsvBindByName(column = "subnet-id")
    private String subNetId;
    @CsvBindByName(column = "instance-id")
    private String instanceId;
    @CsvBindByName(column = "interface-id")
    private String interfaceId;
    @CsvBindByName(column = "account-id")
    private long accountId;
    @CsvBindByName(column = "type")
    private String type;
    @CsvBindByName(column = "srcaddr")
    private String srcAddr;
    @CsvBindByName(column = "dstaddr")
    private String dstAddr;
    @CsvBindByName(column = "srcport")
    private int srcPort;
    @CsvBindByName(column = "dstport")
    private int dstPort;
    @CsvBindByName(column = "pkt-srcaddr")
    private String pktSrcAddr;
    @CsvBindByName(column = "pkt-dstaddr")
    private String pktDstAddr;
    @CsvBindByName(column = "protocol")
    private String protocol;
    @CsvBindByName(column = "bytes")
    private long bytes;
    @CsvBindByName(column = "packets")
    private int packets;
    @CsvBindByName(column = "start")
    private long start;
    @CsvBindByName(column = "end")
    private long end;
    @CsvBindByName(column = "action")
    private String action;//can be changed to enum
    @CsvBindByName(column = "tcp-flags")
    private int tcpFlags;
    @CsvBindByName(column = "log-status")
    private String logStatus;//can be changed to enum.

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getVpcId() {
        return vpcId;
    }

    public void setVpcId(String vpcId) {
        this.vpcId = vpcId;
    }

    public String getSubNetId() {
        return subNetId;
    }

    public void setSubNetId(String subNetId) {
        this.subNetId = subNetId;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSrcAddr() {
        return srcAddr;
    }

    public void setSrcAddr(String srcAddr) {
        this.srcAddr = srcAddr;
    }

    public String getDstAddr() {
        return dstAddr;
    }

    public void setDstAddr(String dstAddr) {
        this.dstAddr = dstAddr;
    }

    public int getSrcPort() {
        return srcPort;
    }

    public void setSrcPort(int srcPort) {
        this.srcPort = srcPort;
    }

    public int getDstPort() {
        return dstPort;
    }

    public void setDstPort(int dstPort) {
        this.dstPort = dstPort;
    }

    public String getPktSrcAddr() {
        return pktSrcAddr;
    }

    public void setPktSrcAddr(String pktSrcAddr) {
        this.pktSrcAddr = pktSrcAddr;
    }

    public String getPktDstAddr() {
        return pktDstAddr;
    }

    public void setPktDstAddr(String pktDstAddr) {
        this.pktDstAddr = pktDstAddr;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public long getBytes() {
        return bytes;
    }

    public void setBytes(long bytes) {
        this.bytes = bytes;
    }

    public int getPackets() {
        return packets;
    }

    public void setPackets(int packets) {
        this.packets = packets;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getTcpFlags() {
        return tcpFlags;
    }

    public void setTcpFlags(int tcpFlags) {
        this.tcpFlags = tcpFlags;
    }

    public String getLogStatus() {
        return logStatus;
    }

    public void setLogStatus(String logStatus) {
        this.logStatus = logStatus;
    }

    @Override
    public String toString() {
        return "FlowLogData{" +
                "version=" + version +
                ", vpcId='" + vpcId + '\'' +
                ", subNetId='" + subNetId + '\'' +
                ", instanceId='" + instanceId + '\'' +
                ", interfaceId='" + interfaceId + '\'' +
                ", accountId=" + accountId +
                ", type='" + type + '\'' +
                ", srcAddr='" + srcAddr + '\'' +
                ", dstAddr='" + dstAddr + '\'' +
                ", srcPort=" + srcPort +
                ", dstPort=" + dstPort +
                ", pktSrcAddr='" + pktSrcAddr + '\'' +
                ", pktDstAddr='" + pktDstAddr + '\'' +
                ", protocol='" + protocol + '\'' +
                ", bytes=" + bytes +
                ", packets=" + packets +
                ", start=" + start +
                ", end=" + end +
                ", action='" + action + '\'' +
                ", tcpFlags=" + tcpFlags +
                ", logStatus='" + logStatus + '\'' +
                '}';
    }



}
