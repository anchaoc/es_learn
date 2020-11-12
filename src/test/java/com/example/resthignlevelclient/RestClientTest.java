package com.example.resthignlevelclient;

import com.example.EsLearnApplication;
import com.example.model.Book;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author anchao
 * @date 2020/11/12 14:30
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EsLearnApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class RestClientTest {
    @Resource
    private RestHighLevelClient restHighLevelClient;
    //ElasticSearchRepository 面向对象 简单CRUD

    @Test
    public void init(){
    }

    @Test
    public void testDeleteById() throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest("dangdang","book","xNYwu3UBj1ia_1RnSYwb");
        DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        System.out.println(deleteResponse.status());
    }

    @Test
    public void testAddIndex() throws IOException {
        IndexRequest indexRequest = new IndexRequest("ems","emp","1");
        indexRequest.source("");
        IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        System.out.println(indexResponse.status());
    }

}
