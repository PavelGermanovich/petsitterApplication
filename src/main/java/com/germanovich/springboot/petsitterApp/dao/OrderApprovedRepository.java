package com.germanovich.springboot.petsitterApp.dao;

import com.germanovich.springboot.petsitterApp.entity.OrderApproved;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderApprovedRepository extends CrudRepository<OrderApproved, Integer> {
    @Query("from order_sent where petsitter.user.email = ?1 and status.id=3")
    List<OrderApproved> findPetsitterApprovedOrders(String email);

    @Query("from order_sent where petsitter.user.email = ?1 and status.id in (4, 5)")
    List<OrderApproved> findPetsitterDeclinedOrders(String email);

    @Query("from order_sent where petowner.user.email = ?1 and status.id=3")
    List<OrderApproved> findPetownerApprovedOrders(String email);

    @Query("from order_sent where petowner.user.email = ?1 and status.id in (4, 5)")
    List<OrderApproved> findPetownerDeclinedOrders(String email);
}

