package net.floodlightcontroller.maple.networkversioncontroller;

import org.projectfloodlight.openflow.types.U64;

public class SerializableU64  implements java.io.Serializable{
    long raw;
    SerializableU64 (U64 u){
        raw = u.getValue();
    }
}
