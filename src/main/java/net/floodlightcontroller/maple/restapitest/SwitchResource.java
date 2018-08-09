package net.floodlightcontroller.maple.restapitest;

import net.floodlightcontroller.staticentry.web.ListStaticEntriesResource;
import net.floodlightcontroller.staticentry.web.SFPEntryMap;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SwitchResource extends ServerResource {
    protected static Logger log = LoggerFactory.getLogger(ListStaticEntriesResource.class);
    @Get("json")
    public SwitchInfo ListSwitch(){
        return new SwitchInfo();
    }
}
