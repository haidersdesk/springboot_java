package com.example.lager.controller;

import com.example.lager.exception.BadRequestException;
import com.example.lager.exception.NotFoundException;
import com.example.lager.model.request.ProductDetailsRequestModel;
import com.example.lager.model.response.ProductDetailsResponseModel;
import com.example.lager.service.ProductService;
import com.example.lager.shared.ProductDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductService productService;


    @GetMapping
    public List<ProductDetailsResponseModel> getAllProducts(){
        List<ProductDto> productDtos = productService.getAllProducts();
        ArrayList<ProductDetailsResponseModel> responseList = new ArrayList<>();
        for (ProductDto productDto: productDtos){
            ProductDetailsResponseModel responseModel= new ProductDetailsResponseModel();
            BeanUtils.copyProperties(productDto, responseModel);
            responseList.add(responseModel);

        }
        //return productDtos.stream().forEach((productDto) -> BeanUtils.copyProperties(productDto,productDtoOut));
       return responseList;

    }


    @GetMapping(value="/{pId}")
    public ResponseEntity<ProductDetailsResponseModel> getProduct(@PathVariable String pId){
        ProductDetailsResponseModel responseModel = new ProductDetailsResponseModel();
        Optional<ProductDto> optionalProductDto = productService.getByProductId(pId);
        if(optionalProductDto.isPresent()) {
            ProductDto productDto = optionalProductDto.get();
            BeanUtils.copyProperties(productDto, responseModel);
            return new ResponseEntity<>(responseModel, HttpStatus.OK);
            //return responseModel;
        }
        throw new NotFoundException("***No data with this id***"+ pId);
    }

    @PutMapping("/{pId}")
    public ProductDetailsResponseModel updateProduct(@PathVariable String pId, @RequestBody ProductDetailsRequestModel productData){
        //Copy Json in to DTO
        ProductDto productDtoIn = new ProductDto();
        BeanUtils.copyProperties(productData, productDtoIn);

        //Pass DTO to Service Layer
        Optional<ProductDto> productDtoOut = productService.updateProduct(pId, productDtoIn);
        if(productDtoOut.isEmpty()){
            throw new NotFoundException("****No Id Found****");
        }

        ProductDto productDto = productDtoOut.get();
        ProductDetailsResponseModel responseModel = new ProductDetailsResponseModel();
        BeanUtils.copyProperties(productDto, responseModel);
        return responseModel;

    }


    @DeleteMapping("/{pId}")
    public String deleteProduct(@PathVariable String pId){
        ProductDetailsResponseModel responseModel= new ProductDetailsResponseModel();
        boolean deleted = productService.deleteProduct(pId);
        if(deleted){
            return"";
        }
        throw new BadRequestException("***No product with this id***"+ pId);
    }





    @PostMapping
    public ResponseEntity<ProductDetailsResponseModel> addProduct(@RequestBody ProductDetailsRequestModel productDetails){
        //Copy Json in to DTO
        ProductDto productDtoIn = new ProductDto();
        BeanUtils.copyProperties(productDetails, productDtoIn);

        //Pass DTO to Service Layer
        ProductDto productDtoOut = productService.addProduct(productDtoIn);

        //Copy DTO from Service Layer to response
        ProductDetailsResponseModel response = new ProductDetailsResponseModel();
        BeanUtils.copyProperties(productDtoOut, response);

        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }



}
