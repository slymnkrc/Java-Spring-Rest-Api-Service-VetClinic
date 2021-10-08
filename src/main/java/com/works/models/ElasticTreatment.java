package com.works.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "treatment")
@Data
public class ElasticTreatment {

    @Id
    private String treId;

    @Field(type = FieldType.Text)
    private String treNote;

    @Field(type = FieldType.Text)
    private String treLab;

    @Field(type = FieldType.Text)
    private String treOperation;

    @Field(type = FieldType.Text)
    private String treDressing;

    @Field(type = FieldType.Text)
    private String treRadiography;

    @Field(type = FieldType.Text)
    private String treMedicine;

    @Field(type = FieldType.Text)
    private String treCode;

    @Field(type = FieldType.Text)
    private String cuName;

    @Field(type = FieldType.Text)
    private String paName;

    @Field(type = FieldType.Text)
    private String vacName;
}
