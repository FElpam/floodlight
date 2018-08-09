package net.floodlightcontroller.maple.networkversioncontroller;

import com.google.common.base.Objects;
import org.projectfloodlight.openflow.protocol.OFFlowMod;
import org.projectfloodlight.openflow.protocol.OFFlowModCommand;
import org.projectfloodlight.openflow.protocol.OFFlowModFlags;
import org.projectfloodlight.openflow.protocol.OFType;
import org.projectfloodlight.openflow.protocol.OFVersion;
import org.projectfloodlight.openflow.protocol.action.OFAction;
import org.projectfloodlight.openflow.protocol.instruction.OFInstruction;
import org.projectfloodlight.openflow.protocol.match.Match;
import org.projectfloodlight.openflow.types.OFBufferId;
import org.projectfloodlight.openflow.types.OFGroup;
import org.projectfloodlight.openflow.types.OFPort;
import org.projectfloodlight.openflow.types.TableId;
import org.projectfloodlight.openflow.types.U64;

import java.util.List;
import java.util.Set;

public class SerializableFlowMod implements java.io.Serializable{
    OFVersion ofVersion;
    OFType ofType;
    long xid;
    SerializableU64 cookie;
    SerializableU64 cookieMask;
    TableId tableId;
    OFFlowModCommand ofFlowModCommand;
    int idleTimeOut;
    int hardTimeOut;
    int priority;
    OFBufferId ofBufferId;
    OFPort ofPort;
    OFGroup ofGroup;
    Set<OFFlowModFlags> flags;
    SerializableMatch match;
    List<OFInstruction> instructions;
    List<OFAction> ofActions;
    int importance;

    public SerializableFlowMod(){

    }
    public SerializableFlowMod(OFFlowMod flowMod){
        ofVersion = flowMod.getVersion();
        ofType = flowMod.getType();
        xid = flowMod.getXid();
        cookie = new SerializableU64(flowMod.getCookie());
        cookieMask = new SerializableU64(flowMod.getCookieMask());
        tableId = flowMod.getTableId();
        ofFlowModCommand = flowMod.getCommand();
        idleTimeOut = flowMod.getIdleTimeout();
        hardTimeOut = flowMod.getHardTimeout();
        priority = flowMod.getPriority();
//        ofBufferId = flowMod.getBufferId();
//        ofPort = flowMod.getOutPort();
//        ofGroup = flowMod.getOutGroup();
//        flags = flowMod.getFlags();
//        match = new SerializableMatch(flowMod.getMatch());
//        instructions = flowMod.getInstructions();
        if (ofVersion != OFVersion.OF_13) {
            ofActions = flowMod.getActions();
            importance = flowMod.getImportance();
        }
        else {
            ofActions = null;
            importance = 0;
        }
    }
    public OFVersion getOfVersion() {
        return ofVersion;
    }

    public void setOfVersion(OFVersion ofVersion) {
        this.ofVersion = ofVersion;
    }

    public OFType getOfType() {
        return ofType;
    }

    public void setOfType(OFType ofType) {
        this.ofType = ofType;
    }

    public long getXid() {
        return xid;
    }

    public void setXid(long xid) {
        this.xid = xid;
    }

    public SerializableU64 getCookie() {
        return cookie;
    }

    public void setCookie(SerializableU64 cookie) {
        this.cookie = cookie;
    }

    public SerializableU64 getCookieMask() {
        return cookieMask;
    }

    public void setCookieMask(SerializableU64 cookieMask) {
        this.cookieMask = cookieMask;
    }

    public TableId getTableId() {
        return tableId;
    }

    public void setTableId(TableId tableId) {
        this.tableId = tableId;
    }

    public OFFlowModCommand getOfFlowModCommand() {
        return ofFlowModCommand;
    }

    public void setOfFlowModCommand(OFFlowModCommand ofFlowModCommand) {
        this.ofFlowModCommand = ofFlowModCommand;
    }

    public int getIdleTimeOut() {
        return idleTimeOut;
    }

    public void setIdleTimeOut(int idleTimeOut) {
        this.idleTimeOut = idleTimeOut;
    }

    public int getHardTimeOut() {
        return hardTimeOut;
    }

    public void setHardTimeOut(int hardTimeOut) {
        this.hardTimeOut = hardTimeOut;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public OFBufferId getOfBufferId() {
        return ofBufferId;
    }

    public void setOfBufferId(OFBufferId ofBufferId) {
        this.ofBufferId = ofBufferId;
    }

    public OFPort getOfPort() {
        return ofPort;
    }

    public void setOfPort(OFPort ofPort) {
        this.ofPort = ofPort;
    }

    public OFGroup getOfGroup() {
        return ofGroup;
    }

    public void setOfGroup(OFGroup ofGroup) {
        this.ofGroup = ofGroup;
    }

    public Set<OFFlowModFlags> getFlags() {
        return flags;
    }

    public void setFlags(Set<OFFlowModFlags> flags) {
        this.flags = flags;
    }

    public List<OFInstruction> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<OFInstruction> instructions) {
        this.instructions = instructions;
    }

    public List<OFAction> getOfActions() {
        return ofActions;
    }

    public void setOfActions(List<OFAction> ofActions) {
        this.ofActions = ofActions;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("ofVersion", ofVersion)
                .add("ofType", ofType)
                .add("xid", xid)
                .add("cookie", cookie)
                .add("cookieMask", cookieMask)
                .add("tableId", tableId)
                .add("ofFlowModCommand", ofFlowModCommand)
                .add("idleTimeOut", idleTimeOut)
                .add("hardTimeOut", hardTimeOut)
                .add("priority", priority)
                .add("ofBufferId", ofBufferId)
                .add("ofPort", ofPort)
                .add("ofGroup", ofGroup)
                .add("flags", flags)
                .add("match", match)
                .add("instructions", instructions)
                .add("ofActions", ofActions)
                .add("importance", importance)
                .toString();
    }
}
