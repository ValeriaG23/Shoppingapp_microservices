package com.shoppingapp.orderservice.controller;

import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.shoppingapp.orderservice.dto.OrderRequest;
import com.shoppingapp.orderservice.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    //injectam Order service
    private final OrderService orderService;


      @PostMapping
      @ResponseStatus(HttpStatus.CREATED)
      public String placeOrder(@RequestBody OrderRequest orderRequest){
        //ori de cate ori userul plaseaza comanda va fi salvata in DB si va returna textul

        orderService.placeOrder(orderRequest);
        return "Order placed succesfully!";

      }

}
