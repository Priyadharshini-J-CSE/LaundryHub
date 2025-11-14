package com.hostel.washing.controller;

import com.hostel.washing.model.*;
import com.hostel.washing.repository.*;
import com.hostel.washing.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/dhobi")
public class DhobiController {
    
    @Autowired
    private LaundryRequestRepository laundryRequestRepository;
    
    @Autowired
    private LostFoundRepository lostFoundRepository;
    
    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.UserRole.DHOBI) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "dhobi/dashboard";
    }
    
    @GetMapping("/laundry-requests")
    public String laundryRequests(Model model) {
        try {
            List<LaundryRequest> allRequests = laundryRequestRepository.findAll();
            model.addAttribute("allRequests", allRequests);
        } catch (Exception e) {
            model.addAttribute("allRequests", new java.util.ArrayList<>());
        }
        return "dhobi/laundry-requests";
    }
    
    @PostMapping("/laundry-requests/accept/{id}")
    public String acceptRequest(@PathVariable String id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        LaundryRequest request = laundryRequestRepository.findById(id).orElse(null);
        
        if (request != null) {
            request.setStatus(LaundryRequest.RequestStatus.ACCEPTED);
            request.setCurrentStage("ACCEPTED");
            request.setDhobiId(user.getUsername());
            laundryRequestRepository.save(request);
            
            notificationService.createNotification(request.getStudentId(), 
                "Your laundry request has been accepted by " + user.getUsername(), 
                Notification.NotificationType.LAUNDRY_ACCEPTED, request.getId());
        }
        
        return "redirect:/dhobi/laundry-requests";
    }
    
    @PostMapping("/laundry-requests/reject/{id}")
    public String rejectRequest(@PathVariable String id) {
        LaundryRequest request = laundryRequestRepository.findById(id).orElse(null);
        
        if (request != null) {
            request.setStatus(LaundryRequest.RequestStatus.REJECTED);
            laundryRequestRepository.save(request);
            
            notificationService.createNotification(request.getStudentId(), 
                "Your laundry request has been rejected", 
                Notification.NotificationType.LAUNDRY_REJECTED, request.getId());
        }
        
        return "redirect:/dhobi/laundry-requests";
    }
    
    @GetMapping("/lost-requests")
    public String lostRequests(Model model) {
        List<LostFound> items = lostFoundRepository.findByStatus(LostFound.LostFoundStatus.REPORTED);
        model.addAttribute("items", items);
        return "dhobi/lost-requests";
    }
    
    @PostMapping("/lost-requests/respond/{id}")
    public String respondToLostItem(@PathVariable String id, @RequestParam String response,
                                   @RequestParam String status) {
        LostFound item = lostFoundRepository.findById(id).orElse(null);
        
        if (item != null) {
            item.setResponse(response);
            item.setStatus(LostFound.LostFoundStatus.valueOf(status.toUpperCase()));
            lostFoundRepository.save(item);
        }
        
        return "redirect:/dhobi/lost-requests";
    }
    
    @PostMapping("/laundry-requests/update-stage/{id}")
    public String updateStage(@PathVariable String id, @RequestParam String stage) {
        LaundryRequest request = laundryRequestRepository.findById(id).orElse(null);
        
        if (request != null) {
            request.setStatus(LaundryRequest.RequestStatus.valueOf(stage));
            request.setCurrentStage(stage);
            laundryRequestRepository.save(request);
            
            String stageMessage = "Your laundry is now: " + stage.replace("_", " ");
            notificationService.createNotification(request.getStudentId(), 
                stageMessage, Notification.NotificationType.GENERAL, request.getId());
        }
        
        return "redirect:/dhobi/laundry-requests";
    }
}