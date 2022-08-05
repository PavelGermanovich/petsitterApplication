package com.germanovich.springboot.petsitterApp.contoller;

import com.germanovich.springboot.petsitterApp.dao.OrderPlannedRepository;
import com.germanovich.springboot.petsitterApp.dao.PetsitterRepository;
import com.germanovich.springboot.petsitterApp.dto.PetsitterOrderDto;
import com.germanovich.springboot.petsitterApp.entity.OrderPlanned;
import com.germanovich.springboot.petsitterApp.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class OrderController {

    @Autowired
    private PetsitterRepository petsitterRepository;

    @Autowired
    private OrderPlannedRepository orderPlannedRepository;

    @Autowired
    private OrderService orderService;

    @PostMapping(value = "/bookPetsitter")
    public String createBookingController(PetsitterOrderDto petsitterOrderDto, Model model) {
        try {
            orderService.createPlannedOrder(petsitterOrderDto);
        } catch (Exception e) {
            model.addAttribute("messagePetCreated", "Failure");
        }
        model.addAttribute("messagePetCreated", "Failed");

        return ("redirect:/profile/petsitterInfo/" + petsitterOrderDto.getPetsitterId());

    }
}
