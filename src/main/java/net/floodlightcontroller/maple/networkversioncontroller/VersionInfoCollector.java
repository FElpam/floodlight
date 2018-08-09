package net.floodlightcontroller.maple.networkversioncontroller;


import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import net.floodlightcontroller.core.FloodlightContext;
import net.floodlightcontroller.core.IFloodlightProviderService;
import net.floodlightcontroller.core.IOFMessageListener;
import net.floodlightcontroller.core.IOFSwitch;
import net.floodlightcontroller.core.internal.IOFSwitchService;
import net.floodlightcontroller.core.module.FloodlightModuleContext;
import net.floodlightcontroller.core.module.FloodlightModuleException;
import net.floodlightcontroller.core.module.IFloodlightModule;
import net.floodlightcontroller.core.module.IFloodlightService;
import net.floodlightcontroller.core.web.serializers.OFFlowModSerializer;
import net.floodlightcontroller.threadpool.IThreadPoolService;
import net.floodlightcontroller.topology.TopologyManager;
import org.projectfloodlight.openflow.protocol.OFFlowMod;
import org.projectfloodlight.openflow.protocol.OFMessage;
import org.projectfloodlight.openflow.protocol.OFType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import static org.projectfloodlight.openflow.protocol.OFType.FLOW_MOD;

public class VersionInfoCollector implements IFloodlightModule, IOFMessageListener {
    protected IOFSwitchService switchService;
    protected IThreadPoolService poolService;
    protected IFloodlightProviderService provider;
    protected static Logger logger;
    protected TopologyManager topologyManager;
    protected NetVersion netVersion;
    protected ArrayList<OFFlowMod> flowModList;
    protected ScheduledExecutorService scheduledExecutorService;
    private ReentrantLock lock;
    private static final JsonFactory jsonFactory = new JsonFactory();
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
        l.add(IThreadPoolService.class);
        return l;
    }

    @Override
    public void init(FloodlightModuleContext context) throws FloodlightModuleException {
        provider = context.getServiceImpl(IFloodlightProviderService.class);
        switchService = context.getServiceImpl(IOFSwitchService.class);
        poolService = context.getServiceImpl(IThreadPoolService.class);
        logger = LoggerFactory.getLogger(VersionInfoCollector.class);
        topologyManager = new TopologyManager();
        topologyManager.init(context);
        netVersion = new NetVersion(new ArrayList<>(), 0);
        flowModList = new ArrayList<>();
        scheduledExecutorService = poolService.getScheduledExecutor();
        lock = new ReentrantLock();
    }

    @Override
    public void startUp(FloodlightModuleContext context) throws FloodlightModuleException {
        provider.addOFMessageListener(OFType.FLOW_REMOVED,this);
        provider.addOFMessageListener(FLOW_MOD, this);
        provider.addOFMessageListener(OFType.PORT_MOD, this);
        scheduledExecutorService.scheduleWithFixedDelay(() -> versionStorage() , 0, 5, TimeUnit.SECONDS);

    }

    @Override
    public Command receive(IOFSwitch sw, OFMessage msg, FloodlightContext cntx) {
        if (msg.getType() == FLOW_MOD) {
            OFFlowMod ofFlowMod = (OFFlowMod) msg;
            logger.info(ofFlowMod.toString());
            flowModList.add(ofFlowMod);
        }
        return Command.CONTINUE;
    }

    @Override
    public String getName() {
        return VersionInfoCollector.class.getSimpleName();
    }

    @Override
    public boolean isCallbackOrderingPrereq(OFType type, String name) {
        return false;
    }

    @Override
    public boolean isCallbackOrderingPostreq(OFType type, String name) {
        return false;
    }


//    //记录最开始版本时网络的所有状态信息
//    public void versionInit(Map<DatapathId, IOFSwitch> switchMap){
//        Version version = new Version();
//        version.setSwitchMap(switchMap);
//        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("version.txt"))) {
//            oos.writeObject(version);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    public void versionStorage() {
        logger.info("Begin to lock!" );


        if (!flowModList.isEmpty()) {
            lock.lock();
            try (JsonGenerator jsonGen = jsonFactory.createGenerator(new File(
                    "src/main/java/net/floodlightcontroller/maple/netver/version" + netVersion.getId() + ".json"), JsonEncoding.UTF8)) {


                netVersion.setFlowModList(flowModList);
                netVersion.setId(netVersion.getId() + 1);
                NetVersionSerializer.serializeNetVersion(jsonGen,netVersion );
                flowModList = new ArrayList<>();
                netVersion.setFlowModList(flowModList);

            } catch (IOException e) {
                logger.error("Could not instantiate JSON Generator. {}", e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }


}

