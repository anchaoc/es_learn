package com.example.repository;

import com.example.EsLearnApplication;
import com.example.constant.EmpConstants;
import com.example.mapper.EmpEsRepository;
import com.example.model.Emp;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * @author anchao
 * @date 2020/11/12 16:55
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EsLearnApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class TestRepository {
    @Resource
    private EmpEsRepository empEsRepository;
    @Resource
    private RestHighLevelClient restHighLevelClient;
    /**
     *  id 不存在添加
     *  id 存在修改
     */
    @Test
    public void testSave(){
        Emp emp = new Emp();
        emp.setId(UUID.randomUUID().toString());
        emp.setName("安超");
        emp.setAddress("北京海淀西二旗");
        emp.setAge(new Random().nextInt(30));
        emp.setBir(new Date());
        emp.setContent("安超，毕业于康奈尔小学，现为上海交通大学船建学院特别副研究员。");
        empEsRepository.save(emp);
    }
    @Test
    public void testDelete(){
        Emp emp = new Emp();
        emp.setId("a1436d14-f28c-4159-8fdd-9c1f4b35f840");
        empEsRepository.delete(emp);
    }

    @Test
    public void testQuery(){
//        Optional<Emp> optionalEmp = empEsRepository.findById("f30a988f-f3d6-432c-9044-2aae20a1ebbe");
//        optionalEmp.ifPresent(System.out::println);
        Iterable<Emp> empEsRepositoryAll = empEsRepository.findAll(Sort.by("age").descending());
        empEsRepositoryAll.forEach(System.out::println);
    }

    @Test
    public void testSearch(){
//        Page<Emp> result = empEsRepository
//                .search(QueryBuilders.matchAllQuery(), PageRequest.of(0, 20));
//        System.out.println(result.getContent());
        // List<Emp> empList = empEsRepository.findByName("安超");
//        List<Emp> empList = empEsRepository.findByAge(6);
//        System.out.println(empList);
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("content", "小学");
        Iterable<Emp> empIterable = empEsRepository.search(termQueryBuilder);
        empIterable.forEach(System.out::println);
    }

    @Test
    public void testHighSearch() throws IOException {
        SearchRequest searchRequest = new SearchRequest(EmpConstants.INDEX_NAME);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        TermQueryBuilder termQueryBuilder = QueryBuilders
                .termQuery("content", "小学");
        searchSourceBuilder.query(termQueryBuilder)
                //.sort("age.keyword", SortOrder.DESC).from(0).size(10)
                .highlighter(new HighlightBuilder().field("content").requireFieldMatch(false).preTags("<span style='color:red'>").postTags("</span>"));
        searchRequest.types(EmpConstants.TYPE).source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        for (SearchHit hit : searchResponse.getHits()) {
            System.out.println(hit.getSourceAsString());
        }
    }

}
