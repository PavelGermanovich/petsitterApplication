package com.germanovich.springboot.petsitterApp.contoller;

import com.germanovich.springboot.petsitterApp.dao.OrderPlannedRepository;
import com.germanovich.springboot.petsitterApp.dao.PetsitterRepository;
import com.germanovich.springboot.petsitterApp.dto.PetsitterOrderDto;
import com.germanovich.springboot.petsitterApp.entity.OrderPlanned;
import com.germanovich.springboot.petsitterApp.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private PetsitterRepository petsitterRepository;

    @Autowired
    private OrderPlannedRepository orderPlannedRepository;

    @Autowired
    private OrderService orderService;

    @PostMapping(value = "/bookPetsitter")
    public String createBookingController(PetsitterOrderDto petsitterOrderDto, Model model, Principal principal,
                                          RedirectAttributes redirectAttributes) {
        try {
            orderService.createPlannedOrder(petsitterOrderDto, principal);
            redirectAttributes.addFlashAttribute("messagePetCreated", "Success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("messagePetCreated", "Failed");
        }
        return ("redirect:/profile/petsitterInfo/" + petsitterOrderDto.getPetsitterId());

    }

    @GetMapping(value = "/bookings")
    public String getBookings(Model model, Principal principal) {
        List<OrderPlanned> draftOrders = orderService.getPlannedOrders(principal);
        model.addAttribute("draftBookings", draftOrders);
        return "draftBookings";
    }
}
