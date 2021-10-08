package com.works.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "sales")
@Data
public class ElasticSales {

    @Id
    private String saId;

    @Field(type = FieldType.Text)
    private String saNote;

    @Field(type = FieldType.Text)
    private String saLabType;

    @Field(type = FieldType.Text)
    private String saReceiptNo;

    @Field(type = FieldType.Text)
    private String saSoldDate;

    @Field(type = FieldType.Text)
    private String paName;

    @Field(type = FieldType.Text)
    private String cuName;

    @Field(type = FieldType.Text)
    private String vacName;

    @Field(type = FieldType.Text)
    private String proName;

}
