package com.example.JavaSpringBoot.dto.request;

import com.example.JavaSpringBoot.entity.Product;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryCreateRequest {

    String id;
    String categoryName;
    String img;
    String detail;

}
