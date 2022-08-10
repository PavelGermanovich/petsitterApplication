package com.germanovich.springboot.petsitterApp.contoller;

import com.germanovich.springboot.petsitterApp.dao.OrderApprovedRepository;
import com.germanovich.springboot.petsitterApp.dao.OrderSubmittedRepository;
import com.germanovich.springboot.petsitterApp.dao.PetsitterRepository;
import com.germanovich.springboot.petsitterApp.dto.PetsitterOrderDto;
import com.germanovich.springboot.petsitterApp.entity.OrderApproved;
import com.germanovich.springboot.petsitterApp.entity.OrderSubmitted;
import com.germanovich.springboot.petsitterApp.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private PetsitterRepository petsitterRepository;

    @Autowired
    private OrderSubmittedRepository orderSubmittedRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderApprovedRepository orderApprovedRepository;

    @PostMapping(value = "/bookPetsitter")
    public String createBookingController(PetsitterOrderDto petsitterOrderDto, Model model, Principal principal,
                                          RedirectAttributes redirectAttributes) {
        try {
            orderService.createPlannedOrder(petsitterOrderDto, principal);
            redirectAttributes.addFlashAttribute("orderBooked", "Success");
        }
        catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("orderBooked", "Failed");
        }

        return ("redirect:/bookings");

    }

    @GetMapping(value = "/bookings")
    public String getBookings(Model model, Principal principal) {
        List<OrderSubmitted> draftOrders = orderService.getPlannedOrders(principal);
        List<OrderApproved> approvedOrders = orderService.getApprovedOrders(principal);
        List<OrderApproved> othersOrders = orderService.getApprovedHistoryOrders(principal);

        model.addAttribute("draftBookings", draftOrders);
        model.addAttribute("approvedOrders", approvedOrders);
        model.addAttribute("historyOrders", othersOrders);

        return "bookings";
    }

    @PostMapping(value = "/declineOrder")
    public String declineSubmittedOrder(Model model, Principal principal, RedirectAttributes redirectAttributes,
                                        @RequestParam("id") int orderId) {
        try {
            boolean isOrderCancelled = orderService.cancelOrder(orderId, principal);
            if (!isOrderCancelled) {
                redirectAttributes.addFlashAttribute("declineSubmitted", "Failed");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("declineSubmitted", "Failed");
        }
        redirectAttributes.addFlashAttribute("declineSubmitted", "Success");
        return "redirect:/bookings";
    }

    @PostMapping(value = "/approveOrder")
    public String approveOrder(Principal principal, RedirectAttributes redirectAttributes,
                               @RequestParam("id") int orderId) {
        try {
            boolean isOrderApproved = orderService.approveOrder(orderId, principal);
            if (!isOrderApproved) {
                redirectAttributes.addFlashAttribute("approveSubmitted", "Failed");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("approveSubmitted", "Failed");
        }
        redirectAttributes.addFlashAttribute("approveSubmitted", "Success");
        return "redirect:/bookings";
    }

    @PostMapping(value = "/finishOrder")
    public String finishApprovedOrder(Principal principal, RedirectAttributes redirectAttributes,
                                      @RequestParam("id") int orderId) {
        try {
            boolean isOrderFinished = orderService.finishApprovedOrder(orderId, principal);
            if (!isOrderFinished) {
                redirectAttributes.addFlashAttribute("finishApproved", "Failed");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("finishApproved", "Failed");
        }
        redirectAttributes.addFlashAttribute("finishApproved", "Success");
        return "redirect:/bookings";
    }

    @PostMapping(value = "/declineApprovedOrder")
    public String declineApprovedOrder(Principal principal, RedirectAttributes redirectAttributes,
                                       @RequestParam("id") int orderId) {
        try {
            boolean isOrderDeclined = orderService.declineApprovedOrder(orderId, principal);
            if (!isOrderDeclined) {
                redirectAttributes.addFlashAttribute("declineApproved", "Failed");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("declineApproved", "Failed");
        }
        redirectAttributes.addFlashAttribute("declineApproved", "Success");
        return "redirect:/bookings";
    }
}
