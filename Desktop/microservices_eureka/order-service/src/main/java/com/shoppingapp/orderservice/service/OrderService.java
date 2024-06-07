package com.shoppingapp.orderservice.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.shoppingapp.orderservice.dto.InventoryResponse;
import com.shoppingapp.orderservice.dto.OrderLineItemsDto;
import com.shoppingapp.orderservice.dto.OrderRequest;
import com.shoppingapp.orderservice.model.Order;
import com.shoppingapp.orderservice.model.OrderLineItems;
import com.shoppingapp.orderservice.repository.OrderRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

    // inject order repository in order service
    private final OrderRepository orderRepository;
    // inject web client
    private final WebClient webClient;

    // va prelua cererea de comanda
    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        // cream lista de comanda
        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderLineItemsList(orderLineItems);

        // colectam toate sku code
        List<String> skuCodes = order.getOrderLineItemsList().stream()
                .map(orderLineItem -> orderLineItem.getSkuCode())
                .toList();

        // stabilim comunicarea sincrona cu inventariere
        // apelam Inventory Service, si plasam comanda daca produsul e in stoc
        // inventory controller este expus la un get endpoint
        // salv rsp in result prim p
        InventoryResponse[] inventoryResponsArray = webClient.get()
                .uri("http://localhost:8083/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class) // citeste datele din webclient rsp
                // by default va fi o cerere asincrona, pentru a o face sincrona:
                .block();

        // verif daca e sau nu in stoc
        // avem o lista de matrice la inventar, o convertim in stream si apelam allMatch
        // method pt a verifica daca e in stoc
        // daca va contine IsInStoc true va reveni true si vice versa
        boolean allProductsInStock = Arrays.stream(inventoryResponsArray)
                .allMatch(inventoryResponse -> inventoryResponse.isInStock());

        // verificam stocul
        if (allProductsInStock) {
            // daca toate prod sunt in stoc plasam comanda
            orderRepository.save(order);
            // primim order request de la client, cererea trece la controller
            // din controller cererea de comanda trece la order service, mapam order request
            // in obj
            // si in final salvam comanda in repozitoriu
        } else {
            throw new IllegalArgumentException("Product is not in stock, please try again later");

        }

        // finisam

    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
