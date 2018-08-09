package net.floodlightcontroller.maple.networkversioncontroller;

import org.projectfloodlight.openflow.protocol.OFFlowMod;

import java.util.ArrayList;

public class NetVersion implements java.io.Serializable{
    private ArrayList<OFFlowMod> flowModList;
    private int id;
    public NetVersion(){
    }

    public NetVersion(ArrayList<OFFlowMod> flowModList, int id) {
        this.flowModList = flowModList;
        this.id = id;
    }

    public ArrayList<OFFlowMod> getFlowModList() {
        return flowModList;
    }

    public void setFlowModList(ArrayList<OFFlowMod> flowModList) {
        this.flowModList = flowModList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
