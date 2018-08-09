package net.floodlightcontroller.maple.networkversioncontroller;

import org.projectfloodlight.openflow.protocol.OFOxmList;
import org.projectfloodlight.openflow.protocol.OFVersion;
import org.projectfloodlight.openflow.protocol.match.Match;
import org.projectfloodlight.openflow.protocol.match.MatchField;

public class SerializableMatch implements java.io.Serializable{
    Iterable<MatchField<?>> matchFields;
    SerializableMatch(Match m){
        matchFields = m.getMatchFields();
    }
}
