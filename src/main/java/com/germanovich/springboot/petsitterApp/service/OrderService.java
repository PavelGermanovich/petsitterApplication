package com.germanovich.springboot.petsitterApp.service;

import com.germanovich.springboot.petsitterApp.dao.OrderPlannedRepository;
import com.germanovich.springboot.petsitterApp.dao.PetOwnerRepository;
import com.germanovich.springboot.petsitterApp.dao.PetsitterRepository;
import com.germanovich.springboot.petsitterApp.dao.ServiceRepository;
import com.germanovich.springboot.petsitterApp.dto.PetsitterOrderDto;
import com.germanovich.springboot.petsitterApp.entity.OrderPlanned;
import com.germanovich.springboot.petsitterApp.entity.PetOwner;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    @Autowired
    private PetsitterRepository petsitterRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private PetOwnerRepository petOwnerRepository;

    @Autowired
    private OrderPlannedRepository orderPlannedRepository;

    public void createPlannedOrder(PetsitterOrderDto petsitterOrderDto) {
        OrderPlanned orderPlanned = convertPetsitterOrderDtoToOrderPlanned(petsitterOrderDto);


        return;
    }

    public OrderPlanned convertPetsitterOrderDtoToOrderPlanned(PetsitterOrderDto petsitterOrderDto) {
        OrderPlanned orderPlanned = new OrderPlanned();
        orderPlanned.setPetSitter(petsitterRepository.findById(petsitterOrderDto.getPetsitterId()).get());
        orderPlanned.setService(serviceRepository.findServiceByServiceName(petsitterOrderDto.getService().getRoleName()));
        orderPlanned.setPetOwner(petOwnerRepository.findById(petsitterOrderDto.getPetownerId()).get());
        orderPlanned.setStartDate(petsitterOrderDto.getStartDate());
        orderPlanned.setEndDate(petsitterOrderDto.getEndDate());

        //calculate units of work
        return orderPlanned;
    }
}
