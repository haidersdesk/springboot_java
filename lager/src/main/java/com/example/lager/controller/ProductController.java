package com.example.lager.controller;

import com.example.lager.model.request.ProductDetailsRequestModel;
import com.example.lager.model.response.ProductDetailsResponseModel;
import com.example.lager.service.ProductService;
import com.example.lager.shared.ProductDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping(value="/{pId}")
    public ProductDetailsResponseModel getProduct(@PathVariable String pId){
        ProductDetailsResponseModel responseModel = new ProductDetailsResponseModel();
        Optional<ProductDto> optionalProductDto = productService.getByProductId(pId);
        if(optionalProductDto.isPresent()) {
            ProductDto productDto = optionalProductDto.get();
            BeanUtils.copyProperties(productDto, responseModel);
            return responseModel;
        }
        throw new RuntimeException("No data with this id "+ pId);
    }

    @PutMapping("/{pId}")
    public ProductDetailsResponseModel updateProduct(@PathVariable String pId, @RequestBody ProductDetailsRequestModel productData){
        //Copy Json in to DTO
        ProductDto productDtoIn = new ProductDto();
        BeanUtils.copyProperties(productData, productDtoIn);

        //Pass DTO to Service Layer
        Optional<ProductDto> productDtoOut = productService.updateProduct(pId, productDtoIn);
        if(productDtoOut.isEmpty()){
            throw new RuntimeException("No Id Found");
        }

        ProductDto productDto = productDtoOut.get();
        ProductDetailsResponseModel responseModel = new ProductDetailsResponseModel();
        BeanUtils.copyProperties(productDto, responseModel);
        return responseModel;

    }


    @PostMapping
    public ProductDetailsResponseModel addProduct(@RequestBody ProductDetailsRequestModel productDetails){
        //Copy Json in to DTO
        ProductDto productDtoIn = new ProductDto();
        BeanUtils.copyProperties(productDetails, productDtoIn);

        //Pass DTO to Service Layer
        ProductDto productDtoOut = productService.addProduct(productDtoIn);

        //Copy DTO from Service Layer to response
        ProductDetailsResponseModel response = new ProductDetailsResponseModel();
        BeanUtils.copyProperties(productDtoOut, response);

        return response;
    }



}
