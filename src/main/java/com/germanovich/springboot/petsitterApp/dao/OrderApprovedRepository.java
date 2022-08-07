package com.germanovich.springboot.petsitterApp.dao;

import com.germanovich.springboot.petsitterApp.entity.OrderApproved;
import org.springframework.data.repository.CrudRepository;

public interface OrderApprovedRepository extends CrudRepository<OrderApproved, Integer> {
}
