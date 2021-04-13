package com.example.lager.service.impl;

import com.example.lager.repository.ProductRepository;
import com.example.lager.repository.entity.ProductEntity;
import com.example.lager.service.ProductService;
import com.example.lager.shared.ProductDto;
import com.example.lager.shared.Util;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class ProductServiceImpl implements ProductService {

    //@Autowired
    private final ProductRepository productRepository;
    private final Util util;

    public ProductServiceImpl(ProductRepository productRepository, Util util) {
        this.productRepository = productRepository;
        this.util = util;
    }

    public ProductDto addProduct(ProductDto productDetailsIn){
        ProductEntity productEntity = new ProductEntity();
        BeanUtils.copyProperties(productDetailsIn, productEntity);
        String productId = util.generateHash(productDetailsIn.getName());
        productEntity.setProductId(productId);
        ProductEntity productEntityOut = productRepository.save(productEntity);
        ProductDto productDtoOut = new ProductDto();
        BeanUtils.copyProperties(productEntityOut, productDtoOut);
        return productDtoOut;
    }


    public Optional<ProductDto> getByProductId(String pId) {
        Optional<ProductEntity> productIdEntity = productRepository.findByProductId(pId);
        return productIdEntity.map(productEntity ->{
              ProductDto productDto = new ProductDto();
              BeanUtils.copyProperties(productEntity, productDto);
              return productDto;
        });
    }


    public Optional<ProductDto> updateProduct(String pId, ProductDto productDtoIn) {
        Optional<ProductEntity> productIdEntity = productRepository.findByProductId(pId);
        if (productIdEntity.isEmpty())
            return Optional.empty();
        return productIdEntity.map(productEntity -> {
            ProductDto response = new ProductDto();
            //return data which has not changed
            productEntity.setProductId(productDtoIn.getProductId() !=null ? util.generateHash(productDtoIn.getName().substring(3)) : productEntity.getProductId());
            productEntity.setCategory(productDtoIn.getCategory() !=null ? productDtoIn.getCategory(): productEntity.getCategory());
            productEntity.setCost(productDtoIn.getCost() <1 ? productEntity.getCost() : productDtoIn.getCost());
            productEntity.setName(productDtoIn.getName() !=null ? productDtoIn.getName(): productEntity.getName());

            ProductEntity updatedProductEntity = productRepository.save(productEntity);
            BeanUtils.copyProperties(updatedProductEntity,response);
            return response;
        });
    }

    @Transactional
    public boolean deleteProduct(String pId) {
        long removedProductCount = productRepository.deleteByProductId(pId);
        return removedProductCount>0;
    }


    public List<ProductDto> getAllProducts() {
        Iterable<ProductEntity> productEntities = productRepository.findAll();
        ArrayList<ProductDto> productDtos = new ArrayList<>();
        for(ProductEntity productEntity: productEntities){
            ProductDto productDto = new ProductDto();
            BeanUtils.copyProperties(productEntity, productDto);
            productDtos.add(productDto);
        }
        return productDtos;
    }


}
