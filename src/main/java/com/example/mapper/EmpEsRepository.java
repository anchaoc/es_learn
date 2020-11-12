package com.example.mapper;

import com.example.model.Emp;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * @author anchao
 * @date 2020/11/12 16:51
 **/
public interface EmpEsRepository extends ElasticsearchRepository<Emp,String> {

    /**
     * 自定义实现 根据名称查询
     * @param name 名称
     * @return list
     */
    List<Emp> findByName(String name);

    List<Emp> findByAge(Integer age);
}
