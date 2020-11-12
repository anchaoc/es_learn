package com.example.resthignlevelclient;

import com.example.EsLearnApplication;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.lucene.all.AllTermQuery;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
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
    public void init() {
    }

    @Test
    public void testDeleteById() throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest("ems", "emp", "1");
        DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        System.out.println(deleteResponse.status());
    }

    @Test
    public void testAddIndex() throws IOException {
        IndexRequest indexRequest = new IndexRequest("ems", "emp", "4");
        indexRequest.source("{\"name\":\"小红好好学习将来再北京工作\",\"age\":25}", XContentType.JSON);
        IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        System.out.println(indexResponse.status());
    }

    @Test
    public void testSearch() throws IOException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("name", "学习");
        MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
        searchSourceBuilder.query(matchAllQueryBuilder)
                .from(0)
                .size(20)
                .postFilter(QueryBuilders.matchAllQuery())
                .sort("age.keyword", SortOrder.DESC)
                .highlighter(new HighlightBuilder().field("*").requireFieldMatch(false).preTags("<em color:red>").postTags("</em>"));
        SearchRequest searchRequest = new SearchRequest("ems");
        searchRequest.types("emp");
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        for (SearchHit hit : searchResponse.getHits()) {
            System.out.println("getSourceAsString : "+hit.getSourceAsString());
            System.out.println("getHighlightFields : "+hit.getHighlightFields());
        }
    }

    @Test
    public void testUpdated() throws IOException {
        UpdateRequest updateRequest = new UpdateRequest("ems","emp","1");
        updateRequest.doc("{\"name\":\"小花\",\"age\":27}",XContentType.JSON);
        UpdateResponse updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        System.out.println(updateResponse.status());
    }

    @Test
    public void testBulk() throws IOException {
        IndexRequest indexRequest = new IndexRequest("ems","emp","6");
        indexRequest.source("{\"name\":\"中国\",\"age\":20}", XContentType.JSON);
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.add(indexRequest);
        BulkResponse bulkItemResponses = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(bulkItemResponses.status());
    }


    @Test
    public void testSearch2() throws IOException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("name", "学习");
        //QueryStringQueryBuilder queryStringQueryBuilder = QueryBuilders.queryStringQuery("name=小花");
        MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
        searchSourceBuilder.query(matchAllQueryBuilder)
                .from(0)
                .size(20);
               // .postFilter(QueryBuilders.matchAllQuery())
                //.sort("age.keyword", SortOrder.DESC)
                //.highlighter(new HighlightBuilder().field("name").requireFieldMatch(false).preTags("<span style='color:red'>").postTags("</span>"));
        SearchRequest searchRequest = new SearchRequest("ems");
        searchRequest.types("emp");
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        for (SearchHit hit : searchResponse.getHits()) {
            System.out.println("getSourceAsString : "+hit.getSourceAsString());
            System.out.println("getHighlightFields : "+hit.getHighlightFields());
        }
    }
}
