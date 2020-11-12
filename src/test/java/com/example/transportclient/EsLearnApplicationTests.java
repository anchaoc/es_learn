package com.example.transportclient;

import com.alibaba.fastjson.JSON;
import com.example.model.Book;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 * @author anchao
 */
@SpringBootTest
public class EsLearnApplicationTests {
    /**
     * es 客户端(已过时)
     */
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

    @Test
    public void testCreateIndexAndTypeMapping() throws ExecutionException, InterruptedException {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("learn");
        createIndexRequest.mapping("book", "json", XContentType.JSON);
        CreateIndexResponse indexResponse = transportClient.admin().indices().create(createIndexRequest).get();
        if (indexResponse.isAcknowledged()) {
            System.out.println("索引创建成功");
        }else{
            System.err.println("索引创建失败");
        }
    }

    @Test
    public void testCreateDocument(){
        Book book = new Book("1", "快乐学习", 66.00, "安超", "des", "双师课堂由主师负责线上直播授课，线下辅导老师负责课堂管理、答疑辅导、学情跟进与反馈等服务", new Date());
        String source = JSON.toJSONString(book);
        IndexResponse indexResponse = transportClient.prepareIndex("dangdang", "book", book.getId()).setSource(source,XContentType.JSON).get();
        System.out.println(indexResponse.status());
    }
    @Test
    public void testCreateDocumentOptionId(){
        Book book = new Book("3", "活着", 66.00, "安超", "des", "《活着》讲述一个人一生的故事，这是一个历尽世间沧桑和磨难老人的人生感言，是一幕演绎人生苦难经历的戏剧。小说的叙述者“我”在年轻时获得了一个游手好闲的职业——去乡间收集民间歌谣。在夏天刚刚来到的季节，遇到那位名叫福贵的老人，听他讲述了自己坎坷的人生经历： 地主少爷福贵嗜赌成性，终于赌光了家业，一贫如洗，穷困之中福贵因母亲生病前去求医，没想到半路上被国民党部队抓了壮丁，后被解放军所俘虏，回到家乡他才知道母亲已经过世，妻子家珍含辛茹苦带大了一双儿女，但女儿不幸变成了哑巴。", new Date());
        String source = JSON.toJSONString(book);
        IndexResponse indexResponse = transportClient.prepareIndex("dangdang", "book").setSource(source,XContentType.JSON).get();
        System.out.println(indexResponse.status());
    }
    @Test
    public void testUpdated() throws ParseException {
        Book book = new Book();
        book.setPubDate(new Date());
        String source = JSON.toJSONStringWithDateFormat(book,"yyyy-MM-dd");
        UpdateResponse updateResponse = transportClient.prepareUpdate("dangdang", "book", "1").setDoc(source, XContentType.JSON).get();
        System.out.println(updateResponse.status());
    }

    @Test
    public void testDelete(){
        DeleteResponse deleteResponse = transportClient.prepareDelete("dangdang", "book", "z2A_unUB4rjlPY_lRsZt").get();
        System.out.println(deleteResponse.status());
    }
    @Test
    public void testFindOneById(){
        GetResponse documentFields = transportClient.prepareGet("dangdang", "book", "1").get();
        String source = documentFields.getSourceAsString();
        Book boook = JSON.parseObject(source,Book.class);
        System.out.println(boook);
    }
    @Test
    public void testFindAll(){
        MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
        SearchResponse searchResponse = transportClient.prepareSearch("dangdang").setTypes("book").setQuery(matchAllQueryBuilder).get();
        SearchHits hits = searchResponse.getHits();
        ArrayList<Book> resultData = new ArrayList<Book>(Integer.parseInt(hits.getTotalHits()+""));
        for (SearchHit hit : hits.getHits()) {
            String sourceAsString = hit.getSourceAsString();
            Book book = JSON.parseObject(sourceAsString,Book.class);
            resultData.add(book);
        }
        System.out.println(resultData);
    }
    @Test
    public void testTermQuery(){
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("content", "直播");
        SearchResponse searchResponse = transportClient.prepareSearch("dangdang").setTypes("book").setQuery(termQueryBuilder).get();
        SearchHits hits = searchResponse.getHits();
        ArrayList<Book> resultData = new ArrayList<>(Integer.parseInt(hits.getTotalHits()+""));
        for (SearchHit hit : hits.getHits()) {
            String sourceAsString = hit.getSourceAsString();
            Book book = JSON.parseObject(sourceAsString,Book.class);
            resultData.add(book);
        }
        System.out.println(resultData);
    }
}
