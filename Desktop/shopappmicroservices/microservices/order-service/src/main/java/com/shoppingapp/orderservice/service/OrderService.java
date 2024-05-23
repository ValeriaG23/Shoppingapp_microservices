package com.shoppingapp.orderservice.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.shoppingapp.orderservice.dto.OrderLineItemsDto;
import com.shoppingapp.orderservice.dto.OrderRequest;
import com.shoppingapp.orderservice.model.Order;
import com.shoppingapp.orderservice.model.OrderLineItems;
import com.shoppingapp.orderservice.repository.OrderRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

    // inject order repository in order service
    private final OrderRepository orderRepository;

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

        // plasam comanda in baza de date
        orderRepository.save(order);
        // primim order request de la client, cererea trece la controller
        // din controller cererea de comanda trece la order service, mapam order request
        // in obj
        // si in final salvam comanda in repozitoriu
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
