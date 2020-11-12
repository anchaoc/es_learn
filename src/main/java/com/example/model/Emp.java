package com.example.model;

import com.example.constant.EmpConstants;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * @author anchao
 * @date 2020/11/12 15:06
 **/
@Data
@Document(indexName = EmpConstants.INDEX_NAME,type = EmpConstants.TYPE)
public class Emp {
    /**
     * @Id 可使 主键ID与索引ID同步
     */
    @Id
    private String id;
    @Field(type= FieldType.Text,analyzer=EmpConstants.ANALYZER_NAME,searchAnalyzer=EmpConstants.ANALYZER_NAME)
    private String name;
    @Field(type= FieldType.Integer)
    private Integer age;
    @Field(type=FieldType.Date)
    private Date bir;
    @Field(type= FieldType.Text,analyzer=EmpConstants.ANALYZER_NAME,searchAnalyzer=EmpConstants.ANALYZER_NAME)
    private String content;
    @Field(type= FieldType.Text,analyzer=EmpConstants.ANALYZER_NAME,searchAnalyzer=EmpConstants.ANALYZER_NAME)
    private String address;
}
