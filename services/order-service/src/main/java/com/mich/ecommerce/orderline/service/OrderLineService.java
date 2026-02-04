package com.mich.ecommerce.orderline.service;

import com.mich.ecommerce.orderline.dto.OrderLineRequest;
import com.mich.ecommerce.orderline.mapper.OrderLineMapper;
import com.mich.ecommerce.orderline.repository.OrderLineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderLineService {
    private final OrderLineRepository orderLineRepository;
    private final OrderLineMapper orderLineMapper;

    public Integer saveOrderLine(OrderLineRequest orderLineRequest) {
        var order = orderLineMapper.toOrderLine(orderLineRequest);
        return orderLineRepository.save(order).getId();
    }
}
