package com.example.JavaSpringBoot.dto.respose;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {

    String id;
    String productName;
    String description;
    List<String> imgs;
    CategoryResponse category;

}
