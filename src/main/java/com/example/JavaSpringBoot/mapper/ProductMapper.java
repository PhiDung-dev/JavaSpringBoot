package com.example.JavaSpringBoot.mapper;

import com.example.JavaSpringBoot.dto.request.ProductCreateRequest;
import com.example.JavaSpringBoot.dto.request.ProductUpdateRequest;
import com.example.JavaSpringBoot.dto.respose.ProductResponse;
import com.example.JavaSpringBoot.entity.Category;
import com.example.JavaSpringBoot.entity.Product;
import com.example.JavaSpringBoot.entity.ProductImage;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = CategoryMapper.class)
public interface ProductMapper {

    @Mapping(target = "images", ignore = true)
    @Mapping(target = "category", ignore = true)
    Product toProduct(ProductCreateRequest request);

    @Mapping(target = "imgs", source = "images")
    @Mapping(target = "categoryId", source = "category")
    ProductResponse toProductResponse(Product product);
    List<ProductResponse> toProductResponseList(List<Product> products);

    @Mapping(target = "images", ignore = true)
    @Mapping(target = "category", ignore = true)
    void updateProduct(@MappingTarget Product product, ProductUpdateRequest request);

    default String map(ProductImage productImage) {
        return productImage.getUrl();
    }

}
