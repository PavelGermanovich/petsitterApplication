package com.germanovich.springboot.petsitterApp.service;

import com.germanovich.springboot.petsitterApp.dao.*;
import com.germanovich.springboot.petsitterApp.dto.PetsitterOrderDto;
import com.germanovich.springboot.petsitterApp.entity.OrderPlanned;
import com.germanovich.springboot.petsitterApp.entity.PetOwner;
import com.germanovich.springboot.petsitterApp.entity.PetSitter;
import com.germanovich.springboot.petsitterApp.enums.ORDER_STATUS_ENUM;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;


@Service
public class OrderService {
    @Autowired
    private PetsitterRepository petsitterRepository;
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private PetOwnerRepository petOwnerRepository;
    @Autowired
    private OrderStatusRepository orderStatusRepository;
    @Autowired
    private OrderPlannedRepository orderPlannedRepository;

    public OrderPlanned createPlannedOrder(PetsitterOrderDto petsitterOrderDto, Principal principal) {
        petsitterOrderDto.setPetownerId(petOwnerRepository.findPetOwnerByUserEmail(principal.getName()).getId());

        OrderPlanned orderPlanned = convertPetsitterOrderDtoToOrderPlanned(petsitterOrderDto);
        //ToDo Add validation for incoming order, dates check, service provided check etc.

        return  orderPlannedRepository.save(orderPlanned);
    }

    public OrderPlanned convertPetsitterOrderDtoToOrderPlanned(PetsitterOrderDto petsitterOrderDto) {
        PetSitter petsitter = petsitterRepository.findById(petsitterOrderDto.getPetsitterId()).get();

        OrderPlanned orderPlanned = new OrderPlanned();
        orderPlanned.setPetSitter(petsitter);
        orderPlanned.setService(serviceRepository.findServiceByServiceName(petsitterOrderDto.getService().getRoleName()));
        orderPlanned.setPetOwner(petOwnerRepository.findById(petsitterOrderDto.getPetownerId()).get());
        orderPlanned.setStartDate(petsitterOrderDto.getStartDate());
        orderPlanned.setEndDate(petsitterOrderDto.getEndDate());
        orderPlanned.setOrderStatus(orderStatusRepository.findOrderStatusByStatusName(ORDER_STATUS_ENUM.SUBMITTED.getName()));

        int daysToPetsit = (int) DAYS.between(petsitterOrderDto.getStartDate(), petsitterOrderDto.getEndDate());
        orderPlanned.setUnits(daysToPetsit);
        orderPlanned.setPlannedPrice(daysToPetsit *
                petsitter.getServiceProvidedWithCost().stream()
                        .filter(x -> x.getService().getServiceName().equals(petsitterOrderDto.getService()
                                .getRoleName())).findFirst().get().getCostForServicePerUnit());
        return orderPlanned;
    }

    public List<OrderPlanned> getPlannedOrders(Principal principal) {
        return orderPlannedRepository.findUsersSubmittedOrders(principal.getName());
    }
}
