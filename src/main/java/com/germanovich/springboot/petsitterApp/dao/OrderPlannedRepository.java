package com.germanovich.springboot.petsitterApp.dao;

import com.germanovich.springboot.petsitterApp.entity.OrderPlanned;
import com.germanovich.springboot.petsitterApp.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderPlannedRepository extends JpaRepository<OrderPlanned, Integer> {
    @Query("from order_planned where petSitter.user.email = ?1")
    List<OrderPlanned> findUsersSubmittedOrders(String email);
}
