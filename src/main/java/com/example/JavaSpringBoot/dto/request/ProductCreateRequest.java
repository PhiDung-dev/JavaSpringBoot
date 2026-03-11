package com.example.JavaSpringBoot.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCreateRequest {

    String id;
    String productName;
    String description;
    List<String> imgs;
    String categoryId;

}
