package com.app.springmongo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document("product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity {
    @Field
    private String name;

    @Field
    private String id_product_type;

    @Field
    private String image;

    @Field
    private String description;

    @Field
    private Integer importPrice;

    @Field
    private Integer price;

    @Field
    private Integer sale;

}
