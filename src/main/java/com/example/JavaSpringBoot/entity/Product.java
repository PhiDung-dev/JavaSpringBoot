package com.example.JavaSpringBoot.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {

    @Id
    String id;
    String productName;
    String description;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    List<ProductImage> images;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;

}
