package com.germanovich.springboot.petsitterApp.service;

import com.germanovich.springboot.petsitterApp.dao.*;
import com.germanovich.springboot.petsitterApp.dto.PetsitterOrderDto;
import com.germanovich.springboot.petsitterApp.entity.OrderApproved;
import com.germanovich.springboot.petsitterApp.entity.OrderSubmitted;
import com.germanovich.springboot.petsitterApp.entity.Petsitter;
import com.germanovich.springboot.petsitterApp.enums.ORDER_STATUS_ENUM;
import com.germanovich.springboot.petsitterApp.enums.PETSITTER_SERVICE;
import com.germanovich.springboot.petsitterApp.enums.USER_ROLE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

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
    private OrderSubmittedRepository orderSubmittedRepository;
    @Autowired
    private OrderApprovedRepository orderApprovedRepository;

    public OrderSubmitted createPlannedOrder(PetsitterOrderDto petsitterOrderDto, Principal principal) {
        petsitterOrderDto.setPetownerId(petOwnerRepository.findPetOwnerByUserEmail(principal.getName()).getId());

        OrderSubmitted orderSubmitted = convertPetsitterOrderDtoToOrderPlanned(petsitterOrderDto);
        //ToDo Add validation for incoming order, dates check, service provided check etc.

        return orderSubmittedRepository.save(orderSubmitted);
    }

    public OrderSubmitted convertPetsitterOrderDtoToOrderPlanned(PetsitterOrderDto petsitterOrderDto) {
        Petsitter petsitter = petsitterRepository.findById(petsitterOrderDto.getPetsitterId()).get();

        OrderSubmitted orderSubmitted = new OrderSubmitted();
        orderSubmitted.setPetSitter(petsitter);
        orderSubmitted.setService(serviceRepository.findServiceByServiceName(petsitterOrderDto.getService().getRoleName()));
        orderSubmitted.setPetOwner(petOwnerRepository.findById(petsitterOrderDto.getPetownerId()).get());
        orderSubmitted.setStartDate(petsitterOrderDto.getStartDate());
        if (petsitterOrderDto.getService().equals(PETSITTER_SERVICE.WALKING)) {
            orderSubmitted.setEndDate(petsitterOrderDto.getStartDate());
            orderSubmitted.setUnits(petsitterOrderDto.getHours() == 0 ? 1 : petsitterOrderDto.getHours());
        } else {
            int daysToPetsit = (int) DAYS.between(petsitterOrderDto.getStartDate(), petsitterOrderDto.getEndDate());
            orderSubmitted.setEndDate(petsitterOrderDto.getEndDate());
            orderSubmitted.setUnits(daysToPetsit);
        }
        orderSubmitted.setPlannedPrice(orderSubmitted.getUnits() *
                petsitter.getServiceProvidedWithCost().stream()
                        .filter(x -> x.getService().getServiceName().equals(petsitterOrderDto.getService()
                                .getRoleName())).findFirst().get().getCostForServicePerUnit());
        orderSubmitted.setOrderStatus(orderStatusRepository.findOrderStatusByStatusName(ORDER_STATUS_ENUM.SUBMITTED.getName()));

        orderSubmitted.setPetType(petsitterOrderDto.getPetType());

        return orderSubmitted;
    }

    public List<OrderSubmitted> getPlannedOrders(Principal principal) {
        if (((UsernamePasswordAuthenticationToken) principal).getAuthorities().stream()
                .anyMatch(x -> x.getAuthority().equals(USER_ROLE.PET_SITTER.toString()))) {
            return orderSubmittedRepository.findPetsitterSubmittedOrders(principal.getName());
        } else {
            return orderSubmittedRepository.findPetownerSubmittedOrders(principal.getName());
        }
    }

    public List<OrderApproved> getApprovedOrders(Principal principal) {
        if (((UsernamePasswordAuthenticationToken) principal).getAuthorities().stream()
                .anyMatch(x -> x.getAuthority().equals(USER_ROLE.PET_SITTER.toString()))) {
            return orderApprovedRepository.findPetsitterApprovedOrders(principal.getName());
        } else {
            return orderApprovedRepository.findPetownerApprovedOrders(principal.getName());
        }
    }

    public List<OrderApproved> getHistoryOrders(Principal principal) {
        if (((UsernamePasswordAuthenticationToken) principal).getAuthorities().stream()
                .anyMatch(x -> x.getAuthority().equals(USER_ROLE.PET_SITTER.toString()))) {
            List<OrderApproved> petsitterOrders = orderSubmittedRepository.findPetsitterSubmittedDeclinedOrders(principal.getName())
                    .stream().map(this::convertOrderPlannedToOrderApproved).collect(Collectors.toList());
            List<OrderApproved> orderApprovedList = orderApprovedRepository.findPetsitterDeclinedOrders(principal.getName());
            petsitterOrders.addAll(orderApprovedList);
            return petsitterOrders;
        } else {
            List<OrderApproved> petownerOrdersList = orderSubmittedRepository.findPetownerSubmittedDeclinedOrders(principal.getName())
                    .stream().map(this::convertOrderPlannedToOrderApproved).collect(Collectors.toList());
            List<OrderApproved> orderApprovedList = orderApprovedRepository.findPetownerDeclinedOrders(principal.getName());
            petownerOrdersList.addAll(orderApprovedList);
            return petownerOrdersList;
        }
    }

    public boolean cancelOrder(int orderPlannedId, Principal principal) {
        if (checkOrderSubmittedBelongsToPrincipal(orderPlannedId, principal)) {
            try {
                OrderSubmitted existingOrder = orderSubmittedRepository.findById(orderPlannedId).get();
                existingOrder.setOrderStatus(orderStatusRepository.findOrderStatusByStatusName(ORDER_STATUS_ENUM.DECLINED.getName()));
                orderSubmittedRepository.save(existingOrder);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

        } else {
            return false;
        }
    }

    public boolean approveOrder(int orderPlannedId, Principal principal) {
        if (checkOrderSubmittedBelongsToPrincipal(orderPlannedId, principal)) {
            OrderSubmitted existingOrder = orderSubmittedRepository.findById(orderPlannedId).get();
            existingOrder.setOrderStatus(orderStatusRepository.findOrderStatusByStatusName(ORDER_STATUS_ENUM.APPROVED.getName()));
            orderSubmittedRepository.save(existingOrder);
            OrderApproved orderApproved = convertOrderPlannedToOrderApproved(existingOrder);
            orderApproved.setStatus(orderStatusRepository.findOrderStatusByStatusName(ORDER_STATUS_ENUM.APPROVED.getName()));
            orderApprovedRepository.save(orderApproved);
            return true;
        } else {
            return false;
        }
    }

    private boolean checkOrderSubmittedBelongsToPrincipal(int orderPlannedId, Principal principal) {
        OrderSubmitted existingOrder = orderSubmittedRepository.findById(orderPlannedId).get();
        if (((UsernamePasswordAuthenticationToken) principal).getAuthorities().stream()
                .anyMatch(x -> x.getAuthority().equals(USER_ROLE.PET_SITTER.toString()))) {
            return existingOrder.getPetSitter().getUser().getEmail().equals(principal.getName());
        } else {
            return existingOrder.getPetOwner().getUser().getEmail().equals(principal.getName());
        }
    }

    private boolean checkOrderApprovedBelongsToPrincipal(int orderPlannedId, String principalEmail) {
        OrderSubmitted existingOrder = orderSubmittedRepository.findById(orderPlannedId).get();
        return existingOrder.getPetSitter().getUser().getEmail().equals(principalEmail);
    }

    public boolean declineApprovedOrder(int orderId, String principalEmail) {
        if (checkOrderApprovedBelongsToPrincipal(orderId, principalEmail)) {
            try {
                OrderApproved existingOrder = orderApprovedRepository.findById(orderId).get();
                existingOrder.setStatus(orderStatusRepository.findOrderStatusByStatusName(ORDER_STATUS_ENUM.DECLINED.getName()));
                orderApprovedRepository.save(existingOrder);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean finishApprovedOrder(int orderId, String principalEmail) {
        if (checkOrderApprovedBelongsToPrincipal(orderId, principalEmail)) {
            OrderApproved existingOrder = orderApprovedRepository.findById(orderId).get();
            existingOrder.setStatus(orderStatusRepository.findOrderStatusByStatusName(ORDER_STATUS_ENUM.DONE.getName()));
            orderApprovedRepository.save(existingOrder);
            return true;
        } else {
            return false;
        }
    }

    private OrderApproved convertOrderPlannedToOrderApproved(OrderSubmitted orderSubmitted) {
        OrderApproved orderApproved = new OrderApproved();
        orderApproved.setPetsitter(orderSubmitted.getPetSitter());
        orderApproved.setPetowner(orderSubmitted.getPetOwner());
        orderApproved.setStartTime(orderSubmitted.getStartDate());
        orderApproved.setEndTime(orderSubmitted.getEndDate());
        orderApproved.setPrice(orderSubmitted.getPlannedPrice());
        orderApproved.setUnitsNumber(orderSubmitted.getUnits());
        orderApproved.setPetType(orderSubmitted.getPetType());
        orderApproved.setService(orderSubmitted.getService());
        orderApproved.setStatus(orderSubmitted.getOrderStatus());
        return orderApproved;
    }
}
