package com.example.JavaSpringBoot.mapper;

import com.example.JavaSpringBoot.dto.request.CategoryCreateRequest;
import com.example.JavaSpringBoot.dto.request.CategoryUpdateRequest;
import com.example.JavaSpringBoot.dto.respose.CategoryResponse;
import com.example.JavaSpringBoot.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toCategory(CategoryCreateRequest request);
    CategoryResponse toCategoryResponse(Category category);
    List<CategoryResponse> toCategoryResponseList(List<Category> categories);
    void updateCategory(@MappingTarget Category category, CategoryUpdateRequest request);

}
