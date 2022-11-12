package com.example.stock.service;

import com.example.stock.entity.Stock;
import com.example.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StockService {
    private final StockRepository stockRepository;

    public void decrease(Long id, Long decreaseQuantity){

        Stock stock = stockRepository.findById(id).orElseThrow();

        stock.decrease(decreaseQuantity);

        stockRepository.saveAndFlush(stock);
    }


}
