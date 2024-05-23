package com.shoppingapp.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
}

//cream separat deoarece e bine de separat modelul de entitati
//nu expunem entitatile model la lumea exterioara deoarece daca in viitor in prod class
//add 2 campuri noi care sunt utile pentru business model, nu treb sa expunem aceste campuri 
//util pentru transferul de date