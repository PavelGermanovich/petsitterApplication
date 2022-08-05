package com.germanovich.springboot.petsitterApp.dao;

import com.germanovich.springboot.petsitterApp.entity.OrderStatus;
import org.springframework.data.repository.CrudRepository;

public interface OrderStatusRepository extends CrudRepository<OrderStatus, Integer> {
    public OrderStatus findOrderStatusByStatusName(String statusName);
}
