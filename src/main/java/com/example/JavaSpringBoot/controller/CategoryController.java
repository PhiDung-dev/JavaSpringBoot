package com.example.JavaSpringBoot.controller;

import com.example.JavaSpringBoot.dto.request.CategoryCreateRequest;
import com.example.JavaSpringBoot.dto.request.CategoryUpdateRequest;
import com.example.JavaSpringBoot.dto.respose.CategoryResponse;
import com.example.JavaSpringBoot.dto.respose.ApiResponse;
import com.example.JavaSpringBoot.service.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {

    CategoryService categoryService;

    @PostMapping
    public ApiResponse<CategoryResponse> createCategory(@RequestBody CategoryCreateRequest request) {
        ApiResponse<CategoryResponse> apiResponse = ApiResponse.<CategoryResponse>builder()
                .result(categoryService.createCategory(request))
                .build();
        return apiResponse;
    }

    @GetMapping
    public ApiResponse<List<CategoryResponse>> readCategories() {
        ApiResponse<List<CategoryResponse>> apiResponse = ApiResponse.<List<CategoryResponse>>builder()
                .result(categoryService.readCategories())
                .build();
        return apiResponse;
    }

    @GetMapping("/{categoryId}")
    public ApiResponse<CategoryResponse> readCategory(@PathVariable("categoryId") String id) {
        ApiResponse<CategoryResponse> apiResponse = ApiResponse.<CategoryResponse>builder()
                .result(categoryService.readCategory(id))
                .build();
        return apiResponse;
    }

    @PutMapping("/{categoryId}")
    public ApiResponse<CategoryResponse> updateCategory(@PathVariable("categoryId") String id, @RequestBody CategoryUpdateRequest request) {
        ApiResponse<CategoryResponse> apiResponse = ApiResponse.<CategoryResponse>builder()
                .result(categoryService.updateCategory(id, request))
                .build();
        return apiResponse;
    }

    @DeleteMapping("/{categoryId}")
    public ApiResponse<Void> deleteCategory(@PathVariable("categoryId") String id) {
        categoryService.deleteCategory(id);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .message("category has been deleted")
                .build();
        return apiResponse;
    }

}
