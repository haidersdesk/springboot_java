package com.example.lager.service;
import com.example.lager.shared.ProductDto;

import java.util.Optional;


public interface ProductService{

  ProductDto addProduct(ProductDto userDetails);
  Optional<ProductDto> getByProductId(String pId);
  Optional<ProductDto> updateProduct(String pId, ProductDto productDtoIn);
}
