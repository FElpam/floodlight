package net.floodlightcontroller.maple.networkversioncontroller;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import net.floodlightcontroller.core.web.serializers.OFFlowModSerializer;
import org.projectfloodlight.openflow.protocol.OFFlowMod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class NetVersionSerializer extends JsonSerializer<NetVersion> {
    protected static Logger logger = LoggerFactory.getLogger(NetVersionSerializer.class);

    @Override
    public void serialize(NetVersion netVersion, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        serializeNetVersion(jsonGenerator,netVersion);
    }
    public static void serializeNetVersion(JsonGenerator jGen, NetVersion netVersion) throws IOException, JsonProcessingException {
        jGen.configure(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS, true); // IMHO this just looks nicer and is easier to read if everything is quoted
        jGen.writeStartObject();
        jGen.writeNumberField("id", netVersion.getId());
        jGen.writeArrayFieldStart("flowModList");
        for(OFFlowMod flowMod:netVersion.getFlowModList()){
            OFFlowModSerializer.serializeFlowMod(jGen, flowMod);
        }
        jGen.writeEndArray();
        jGen.writeEndObject();

    }
}
