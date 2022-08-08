package com.germanovich.springboot.petsitterApp.dao;

import com.germanovich.springboot.petsitterApp.entity.OrderSubmitted;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderSubmittedRepository extends JpaRepository<OrderSubmitted, Integer> {
    @Query("from order_planned where petSitter.user.email = ?1 and orderStatus.id in (1, 2)")
    List<OrderSubmitted> findPetsitterSubmittedOrders(String email);

    @Query("from order_planned where petSitter.user.email = ?1 and orderStatus.id in (4)")
    List<OrderSubmitted> findPetsitterSubmittedDeclinedOrders(String email);

    @Query("from order_planned where petOwner.user.email = ?1 and orderStatus.id in (1, 2)")
    List<OrderSubmitted> findPetownerSubmittedOrders(String email);

    @Query("from order_planned where petOwner.user.email = ?1 and orderStatus.id in (4)")
    List<OrderSubmitted> findPetownerSubmittedDeclinedOrders(String email);
}
