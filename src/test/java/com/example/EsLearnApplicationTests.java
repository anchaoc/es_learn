package com.example;

import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootTest
public class EsLearnApplicationTests {

    private TransportClient transportClient;


    @Before
    public void startTransportClient() throws UnknownHostException {
        this.transportClient = new PreBuiltTransportClient(Settings.EMPTY);
        transportClient.addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.1.184"), 9300));
    }

    @After
    public void shutdownTransportClient(){
        transportClient.close();
    }

    @Test
    public void testCreateIndex(){
        CreateIndexResponse indexResponse = transportClient.admin().indices().prepareCreate("dangdang").get();
        if (indexResponse.isAcknowledged()) {
            System.out.println("索引创建成功");
        }else {
            System.err.println("索引创建失败");
        }
    }

    @Test
    public void testDeleteIndex(){
        AcknowledgedResponse acknowledgedResponse = transportClient.admin().indices().prepareDelete("dangdang").get();
        if (acknowledgedResponse.isAcknowledged()) {
            System.out.println("索引删除成功");
        }else{
            System.out.println("索引删除失败");
        }
    }
}
