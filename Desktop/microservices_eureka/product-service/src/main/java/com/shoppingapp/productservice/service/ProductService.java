package com.shoppingapp.productservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.shoppingapp.productservice.dto.ProductRequest;
import com.shoppingapp.productservice.dto.ProductResponse;
import com.shoppingapp.productservice.model.Product;
import com.shoppingapp.productservice.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//serviciu la care vom apela de fiecare data cand accesam un produs
@Service
@RequiredArgsConstructor //in momentul compilarii creaaza constr de care avem nevoie
@Slf4j //pentru loguri
public class ProductService {
//constructor injection depozitul de produse
    private final ProductRepository productRepository;


    //cream un produs care preia cererea de produs
    public void createProduct(ProductRequest productRequest){
        //mapam cererea de produs catre modelul de produs, deci pentru asta cream var de tip produs
        Product product = Product.builder()
        .name(productRequest.getName())
        .description(productRequest.getDescription())
        .price(productRequest.getPrice())
        .build();

        //salvam produsul in baza de date
        productRepository.save(product);
        log.info("Product {} is saved", product.getId());
        //odata ce adaugam prod, vor aparea loguri
        
    }

//return lista de produse
    public List<ProductResponse> getAllProducts() {
    List<Product> products = productRepository.findAll();

    return products.stream().map(product -> mapToProductResponse(product)).collect(Collectors.toList());
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
