package net.floodlightcontroller.maple.pktprocesser;

import net.floodlightcontroller.core.FloodlightContext;
import net.floodlightcontroller.core.IFloodlightProviderService;
import net.floodlightcontroller.core.IOFMessageListener;
import net.floodlightcontroller.core.IOFSwitch;
import net.floodlightcontroller.core.module.FloodlightModuleContext;
import net.floodlightcontroller.core.module.FloodlightModuleException;
import net.floodlightcontroller.core.module.IFloodlightModule;
import net.floodlightcontroller.core.module.IFloodlightService;
import net.floodlightcontroller.packet.ARP;
import net.floodlightcontroller.packet.Ethernet;
import net.floodlightcontroller.packet.IPv4;
import net.floodlightcontroller.packet.TCP;
import net.floodlightcontroller.packet.UDP;
import org.projectfloodlight.openflow.protocol.OFMessage;
import org.projectfloodlight.openflow.protocol.OFType;
import org.projectfloodlight.openflow.types.EthType;
import org.projectfloodlight.openflow.types.IPv4Address;
import org.projectfloodlight.openflow.types.IpProtocol;
import org.projectfloodlight.openflow.types.MacAddress;
import org.projectfloodlight.openflow.types.TransportPort;
import org.projectfloodlight.openflow.types.VlanVid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class PktProcesser implements IFloodlightModule, IOFMessageListener{
    protected IFloodlightProviderService provider;
    protected static Logger logger;

    @Override
    public Command receive(IOFSwitch sw, OFMessage msg, FloodlightContext cntx) {
        logger.info("switch!!!!!!!!!!!!! "+ sw.toString());
        switch (msg.getType()) {
            case PACKET_IN:
                /* Retrieve the deserialized packet in message */
                Ethernet eth = IFloodlightProviderService.bcStore.get(cntx, IFloodlightProviderService.CONTEXT_PI_PAYLOAD);
                logger.info("eth: " + eth.toString());
                /* Various getters and setters are exposed in Ethernet */
                MacAddress srcMac = eth.getSourceMACAddress();
                VlanVid vlanId = VlanVid.ofVlan(eth.getVlanID());

                /*
                 * Check the ethertype of the Ethernet frame and retrieve the appropriate payload.
                 * Note the shallow equality check. EthType caches and reuses instances for valid types.
                 */
                if (eth.getEtherType() == EthType.IPv4) {
                    /* We got an IPv4 packet; get the payload from Ethernet */
                    IPv4 ipv4 = (IPv4) eth.getPayload();
                    logger.info("ipv4: " + ipv4.toString());
                    /* Various getters and setters are exposed in IPv4 */
                    byte[] ipOptions = ipv4.getOptions();
                    IPv4Address dstIp = ipv4.getDestinationAddress();

                    /*d
                     * Check the IP protocol version of the IPv4 packet's payload.
                     */
                    if (ipv4.getProtocol() == IpProtocol.TCP) {
                        /* We got a TCP packet; get the payload from IPv4 */
                        TCP tcp = (TCP) ipv4.getPayload();
                        logger.info("tcp: " + tcp.toString());
                        /* Various getters and setters are exposed in TCP */
                        TransportPort srcPort = tcp.getSourcePort();
                        TransportPort dstPort = tcp.getDestinationPort();
                        short flags = tcp.getFlags();

                        /* Your logic here! */
                    } else if (ipv4.getProtocol() == IpProtocol.UDP) {
                        /* We got a UDP packet; get the payload from IPv4 */
                        UDP udp = (UDP) ipv4.getPayload();
                        logger.info("udp: " + udp.toString());
                        /* Various getters and setters are exposed in UDP */
                        TransportPort srcPort = udp.getSourcePort();
                        TransportPort dstPort = udp.getDestinationPort();

                        /* Your logic here! */
                    }

                } else if (eth.getEtherType() == EthType.ARP) {
                    /* We got an ARP packet; get the payload from Ethernet */
                    ARP arp = (ARP) eth.getPayload();

                    /* Various getters and setters are exposed in ARP */
                    boolean gratuitous = arp.isGratuitous();

                } else {
                    /* Unhandled ethertype */
                }
                break;
            default:
                break;
        }
        return Command.CONTINUE;

    }

    @Override
    public String getName() {
        return PktProcesser.class.getSimpleName();
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
        return l;
    }
    @Override
    public void init(FloodlightModuleContext context) throws FloodlightModuleException {
        provider = context.getServiceImpl(IFloodlightProviderService.class);
        logger = LoggerFactory.getLogger(PktProcesser.class);
    }

    @Override
    public void startUp(FloodlightModuleContext context) throws FloodlightModuleException {
        provider.addOFMessageListener(OFType.PACKET_IN,this);
    }
}
