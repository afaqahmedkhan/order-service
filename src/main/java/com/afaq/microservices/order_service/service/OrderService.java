package com.afaq.microservices.order_service.service;

import client.InventoryClient;
import com.afaq.microservices.order_service.dto.OrderRequest;
import com.afaq.microservices.order_service.model.Order;
import com.afaq.microservices.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;

    public void placeOrder(OrderRequest orderRequest){

        boolean isProductInStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());
        if(isProductInStock){
            Order order = new Order();
            order.setOrderNumber(UUID.randomUUID().toString());
            order.setPrice(orderRequest.price());
            order.setQuantity(orderRequest.quantity());
            order.setSkuCode(orderRequest.skuCode());
            orderRepository.save(order);
        }else {
            throw new RuntimeException("Product with skuCode "+ orderRequest.skuCode() + "is not in stock");
        }


    }
}
