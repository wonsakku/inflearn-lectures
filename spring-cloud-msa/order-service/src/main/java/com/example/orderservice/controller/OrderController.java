package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.jpa.OrderEntity;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.vo.RequestOrder;
import com.example.orderservice.vo.ResponseOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/order-service")
public class OrderController {

    private final OrderService orderService;
    private final ModelMapper modelMapper;
    private final Environment env;

    @GetMapping("/health_check")
    public String status(){
        return String.format("It's Working in Order Service on PORT %s",
                env.getProperty("local.server.port"));
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity createOrder(@PathVariable("userId") String userId,
                                      @RequestBody RequestOrder requestOrder) {
        log.info("createOrder");

        OrderDto orderDto = modelMapper.map(requestOrder, OrderDto.class);
        orderDto.setUserId(userId);
        OrderDto createOrder = orderService.createOrder(orderDto);
        ResponseOrder responseOrder = modelMapper.map(createOrder, ResponseOrder.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);
    }


    @GetMapping("/{userId}/orders")
    public ResponseEntity getOrders(@PathVariable("userId") String userId) {
        log.info("getOrders");
        Iterable<OrderEntity> orderList = orderService.getOrdersByUserId(userId);
        List<ResponseOrder> responseOrders = new ArrayList<>();
        orderList.forEach(order -> responseOrders.add(modelMapper.map(order, ResponseOrder.class)));

//        List<ResponseOrder> responseOrders = Stream.of(orderList)
//                .map(entity -> modelMapper.map(entity, ResponseOrder.class))
//                .collect(Collectors.toList());


        return ResponseEntity.status(HttpStatus.OK).body(responseOrders);
    }





}
