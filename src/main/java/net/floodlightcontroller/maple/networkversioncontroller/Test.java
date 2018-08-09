package net.floodlightcontroller.maple.networkversioncontroller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.nio.ch.Net;

import java.io.File;

public class Test {
    protected static Logger logger = LoggerFactory.getLogger(Test.class);
    public static void main(String[] args) throws Exception {
        //ObjectMapper类用序列化与反序列化映射器
        ObjectMapper mapper = new ObjectMapper();
        File json = new File("src/main/java/net/floodlightcontroller/maple/netver/version1.json");
        //当反序列化json时，未知属性会引起的反序列化被打断，这里我们禁用未知属性打断反序列化功能，
        //因为，例如json里有10个属性，而我们的bean中只定义了2个属性，其它8个属性将被忽略
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        //从json映射到java对象，得到country对象后就可以遍历查找,下面遍历部分内容，能说明问题就可以了
        NetVersion netVersion = mapper.readValue(json, NetVersion.class);
        //设置时间格式，便于阅读
        System.out.println("[TEST]" + netVersion.getId() + " " + netVersion.getFlowModList());


    }
}
