package com.example.JavaSpringBoot.service;

import com.example.JavaSpringBoot.dto.request.CategoryCreateRequest;
import com.example.JavaSpringBoot.dto.request.CategoryUpdateRequest;
import com.example.JavaSpringBoot.dto.respose.CategoryResponse;
import com.example.JavaSpringBoot.entity.Category;
import com.example.JavaSpringBoot.exception.AppException;
import com.example.JavaSpringBoot.exception.ErrorCode;
import com.example.JavaSpringBoot.mapper.CategoryMapper;
import com.example.JavaSpringBoot.repository.CategoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {

    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;

    public CategoryResponse createCategory(CategoryCreateRequest request) {
        if(categoryRepository.existsByCategoryName(request.getCategoryName())) {
            throw new AppException(ErrorCode.CATEGORY_EXISTED);
        }
        Category category = categoryMapper.toCategory(request);
        return categoryMapper.toCategoryResponse(categoryRepository.save(category));
    }

    public List<CategoryResponse> readCategories() {
        return categoryMapper.toCategoryResponseList(categoryRepository.findAll());
    }

    public CategoryResponse readCategory(String id) {
        return categoryMapper.toCategoryResponse(categoryRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.CATEGORY_NOT_FOUND)));
    }

    public CategoryResponse updateCategory(String id, CategoryUpdateRequest request) {
        Category category = categoryRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        categoryMapper.updateCategory(category, request);
        return categoryMapper.toCategoryResponse(categoryRepository.save(category));
    }

    public void deleteCategory(String id) {
        if(!categoryRepository.existsById(id)) {
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }
        categoryRepository.deleteById(id);
    }

}
