package com.example.JavaSpringBoot.controller;

import com.example.JavaSpringBoot.dto.request.ProductCreateRequest;
import com.example.JavaSpringBoot.dto.request.ProductUpdateRequest;
import com.example.JavaSpringBoot.dto.respose.ApiResponse;
import com.example.JavaSpringBoot.dto.respose.ProductResponse;
import com.example.JavaSpringBoot.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {

    ProductService productService;

    @PostMapping
    public ApiResponse<ProductResponse> createProduct(@RequestBody ProductCreateRequest request) {
        ApiResponse<ProductResponse> apiResponse = ApiResponse.<ProductResponse>builder()
                .result(productService.createProduct(request))
                .build();
        return apiResponse;
    }

    @GetMapping
    public ApiResponse<List<ProductResponse>> readProducts() {
        ApiResponse<List<ProductResponse>> apiResponse = ApiResponse.<List<ProductResponse>>builder()
                .result(productService.readProducts())
                .build();
        return apiResponse;
    }

    @GetMapping("/{productId}")
    public ApiResponse<ProductResponse> readProduct(@PathVariable("productId") String id) {
        ApiResponse<ProductResponse> apiResponse = ApiResponse.<ProductResponse>builder()
                .result(productService.readProduct(id))
                .build();
        return apiResponse;
    }

    @PutMapping("/{productId}")
    public ApiResponse<ProductResponse> updateProduct(@PathVariable("productId") String id, @RequestBody ProductUpdateRequest request) {
        ApiResponse<ProductResponse> apiResponse = ApiResponse.<ProductResponse>builder()
                .result(productService.updateProduct(id, request))
                .build();
        return apiResponse;
    }

    @DeleteMapping("/{productId}")
    public ApiResponse<Void> deleteProduct(@PathVariable("productId") String id) {
        productService.deleteProduct(id);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .message("product has been deleted")
                .build();
        return apiResponse;
    }

}
