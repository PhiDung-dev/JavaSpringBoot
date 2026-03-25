package com.example.JavaSpringBoot.service;

import com.example.JavaSpringBoot.dto.request.ProductCreateRequest;
import com.example.JavaSpringBoot.dto.request.ProductUpdateRequest;
import com.example.JavaSpringBoot.dto.respose.ProductResponse;
import com.example.JavaSpringBoot.entity.Category;
import com.example.JavaSpringBoot.entity.Product;
import com.example.JavaSpringBoot.entity.ProductImage;
import com.example.JavaSpringBoot.exception.AppException;
import com.example.JavaSpringBoot.exception.ErrorCode;
import com.example.JavaSpringBoot.mapper.ProductMapper;
import com.example.JavaSpringBoot.repository.CategoryRepository;
import com.example.JavaSpringBoot.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService {

    ProductRepository productRepository;
    ProductMapper productMapper;
    CategoryRepository categoryRepository;

    public ProductResponse createProduct(ProductCreateRequest request) {
        if(productRepository.existsByProductName(request.getProductName())) {
            throw new AppException(ErrorCode.PRODUCT_EXISTED);
        }
        Product product = productMapper.toProduct(request);
        List<ProductImage> images = request.getImgs().stream()
                .map(url->{
                    return ProductImage.builder()
                            .url(url)
                            .product(product)
                            .build();
                }).toList();
        product.setImages(images);
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(()->new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        product.setCategory(category);
        return productMapper.toProductResponse(productRepository.save(product));
    }

    public List<ProductResponse> readProducts() {
        return productMapper.toProductResponseList(productRepository.findAll());
    }

    public ProductResponse readProduct(String id) {
        return productMapper.toProductResponse(productRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.PRODUCT_NOT_FOUND)));
    }

    public ProductResponse updateProduct(String id, ProductUpdateRequest request) {
        Product product = productRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        productMapper.updateProduct(product, request);
        product.getImages().clear();
        request.getImgs().forEach((url)->{
            ProductImage img = ProductImage.builder()
                    .url(url)
                    .product(product)
                    .build();
            product.getImages().add(img);
        });
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(()->new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        product.setCategory(category);
        return productMapper.toProductResponse(productRepository.save(product));
    }

    public void deleteProduct(String id) {
        if(!productRepository.existsById(id)) {
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        productRepository.deleteById(id);
    }

}
