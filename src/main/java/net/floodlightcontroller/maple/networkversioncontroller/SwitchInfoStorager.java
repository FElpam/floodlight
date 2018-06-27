package net.floodlightcontroller.maple.networkversioncontroller;


import net.floodlightcontroller.core.FloodlightContext;
import net.floodlightcontroller.core.IFloodlightProviderService;
import net.floodlightcontroller.core.IOFMessageListener;
import net.floodlightcontroller.core.IOFSwitch;
import net.floodlightcontroller.core.internal.IOFSwitchService;
import net.floodlightcontroller.core.module.FloodlightModuleContext;
import net.floodlightcontroller.core.module.FloodlightModuleException;
import net.floodlightcontroller.core.module.IFloodlightModule;
import net.floodlightcontroller.core.module.IFloodlightService;
import net.floodlightcontroller.linkdiscovery.ILinkDiscovery;
import net.floodlightcontroller.maple.firstmodule.FirstModule;
import net.floodlightcontroller.topology.ITopologyListener;
import org.projectfloodlight.openflow.protocol.OFMessage;
import org.projectfloodlight.openflow.protocol.OFType;
import org.projectfloodlight.openflow.types.DatapathId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class SwitchInfoStorager implements IFloodlightModule, IOFMessageListener, ITopologyListener {
    protected IOFSwitchService switchService;
    protected IFloodlightProviderService provider;
    protected static Logger logger;
    @Override
    public Collection<Class<? extends IFloodlightService>> getModuleServices() {
        return null;
    }

    @Override
    public Map<Class<? extends IFloodlightService>, IFloodlightService> getServiceImpls() {
        return null;
    }

    @Override
    public Collection<Class<? extends IFloodlightService>> getModuleDependencies() {
        Collection<Class<? extends IFloodlightService>> l =
                new ArrayList<Class<? extends IFloodlightService>>();
        l.add(IFloodlightProviderService.class);
        l.add(IOFSwitchService.class);
        return l;
    }

    @Override
    public void init(FloodlightModuleContext context) throws FloodlightModuleException {
        provider = context.getServiceImpl(IFloodlightProviderService.class);
        switchService = context.getServiceImpl(IOFSwitchService.class);
        logger = LoggerFactory.getLogger(FirstModule.class);
    }

    @Override
    public void startUp(FloodlightModuleContext context) throws FloodlightModuleException {
        provider.addOFMessageListener(OFType.PACKET_IN,this);

    }

    @Override
    public Command receive(IOFSwitch sw, OFMessage msg, FloodlightContext cntx) {
        Map<DatapathId,IOFSwitch> switchMaps = switchService.getAllSwitchMap();
        for (Map.Entry<DatapathId,IOFSwitch> entry: switchMaps.entrySet()){
            IOFSwitch iofSwitch = entry.getValue();
            logger.info(entry.getKey() + " " + entry.getValue());
        }
        return Command.CONTINUE;
    }

    @Override
    public String getName() {
        return SwitchInfoStorager.class.getSimpleName();
    }

    @Override
    public boolean isCallbackOrderingPrereq(OFType type, String name) {
        return false;
    }

    @Override
    public boolean isCallbackOrderingPostreq(OFType type, String name) {
        return false;
    }

    @Override
    public void topologyChanged(List<ILinkDiscovery.LDUpdate> linkUpdates) {

    }
}
