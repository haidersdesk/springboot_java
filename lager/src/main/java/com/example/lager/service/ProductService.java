package com.example.lager.service;
import com.example.lager.repository.entity.ProductEntity;
import com.example.lager.shared.ProductDto;

import java.util.List;
import java.util.Optional;


public interface ProductService{

  ProductDto addProduct(ProductDto userDetails);
  Optional<ProductDto> getByProductId(String pId);
  Optional<ProductDto> updateProduct(String pId, ProductDto productDtoIn);
  boolean deleteProduct(String pId);
  List<ProductDto> getAllProducts();
}
