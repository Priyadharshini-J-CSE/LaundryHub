package com.hostel.washing.controller;

import com.hostel.washing.model.*;
import com.hostel.washing.repository.*;
import com.hostel.washing.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/student")
public class StudentController {
    
    @Autowired
    private StoneRepository stoneRepository;
    
    @Autowired
    private MachineRepository machineRepository;
    
    @Autowired
    private LaundryRequestRepository laundryRequestRepository;
    
    @Autowired
    private LostFoundRepository lostFoundRepository;
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.UserRole.STUDENT) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "student/dashboard";
    }
    
    @GetMapping("/stones")
    public String stones(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        
        // Check and release expired bookings
        List<Stone> stones = stoneRepository.findAll();
        for (Stone stone : stones) {
            if (!stone.isAvailable() && stone.getBookedUntil() != null && 
                stone.getBookedUntil().isBefore(LocalDateTime.now())) {
                stone.setAvailable(true);
                stone.setBookedBy(null);
                stone.setBookedAt(null);
                stone.setBookedUntil(null);
                stoneRepository.save(stone);
            }
        }
        
        model.addAttribute("stones", stones);
        model.addAttribute("currentUser", user.getUsername());
        return "student/stones";
    }
    
    @PostMapping("/stones/book/{id}")
    public String bookStone(@PathVariable String id, @RequestParam int hours, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        
        // Check if user already has a booking
        List<Stone> userBookings = stoneRepository.findByBookedBy(user.getUsername());
        if (!userBookings.isEmpty()) {
            model.addAttribute("error", "You already have a stone booked");
            model.addAttribute("stones", stoneRepository.findAll());
            return "student/stones";
        }
        
        // Check cooldown period after cancellation
        if (user.getLastCancelTime() != null && 
            user.getLastCancelTime().plusMinutes(10).isAfter(LocalDateTime.now())) {
            long minutesLeft = java.time.Duration.between(LocalDateTime.now(), user.getLastCancelTime().plusMinutes(10)).toMinutes();
            model.addAttribute("error", "You must wait " + (minutesLeft + 1) + " more minutes before booking again");
            model.addAttribute("stones", stoneRepository.findAll());
            model.addAttribute("currentUser", user.getUsername());
            return "student/stones";
        }
        
        Stone stone = stoneRepository.findById(id).orElse(null);
        
        if (stone != null && stone.isAvailable() && hours <= 2) {
            stone.setAvailable(false);
            stone.setBookedBy(user.getUsername());
            stone.setBookedAt(LocalDateTime.now());
            stone.setBookedUntil(LocalDateTime.now().plusHours(hours));
            stoneRepository.save(stone);
            
            // Create notification
            String message = "You booked Stone " + stone.getStoneNumber() + " for " + hours + " hour(s)";
            notificationService.createNotification(user.getUsername(), message, Notification.NotificationType.STONE_BOOKED, stone.getId());
        }
        
        return "redirect:/student/stones";
    }
    
    @GetMapping("/machines")
    public String machines(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        
        // Check and release expired bookings
        List<Machine> machines = machineRepository.findAll();
        for (Machine machine : machines) {
            if (!machine.isAvailable() && machine.getBookedUntil() != null && 
                machine.getBookedUntil().isBefore(LocalDateTime.now())) {
                machine.setAvailable(true);
                machine.setBookedBy(null);
                machine.setBookedAt(null);
                machine.setBookedUntil(null);
                machineRepository.save(machine);
            }
        }
        
        model.addAttribute("machines", machines);
        model.addAttribute("currentUser", user.getUsername());
        return "student/machines";
    }
    
    @PostMapping("/machines/book/{id}")
    public String bookMachine(@PathVariable String id, @RequestParam int hours, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        
        // Check if user already has a booking
        List<Machine> userBookings = machineRepository.findByBookedBy(user.getUsername());
        if (!userBookings.isEmpty()) {
            model.addAttribute("error", "You already have a machine booked");
            model.addAttribute("machines", machineRepository.findAll());
            return "student/machines";
        }
        
        // Check cooldown period after cancellation
        if (user.getLastCancelTime() != null && 
            user.getLastCancelTime().plusMinutes(10).isAfter(LocalDateTime.now())) {
            long minutesLeft = java.time.Duration.between(LocalDateTime.now(), user.getLastCancelTime().plusMinutes(10)).toMinutes();
            model.addAttribute("error", "You must wait " + (minutesLeft + 1) + " more minutes before booking again");
            model.addAttribute("machines", machineRepository.findAll());
            model.addAttribute("currentUser", user.getUsername());
            return "student/machines";
        }
        
        Machine machine = machineRepository.findById(id).orElse(null);
        
        if (machine != null && machine.isAvailable() && hours <= 2) {
            machine.setAvailable(false);
            machine.setBookedBy(user.getUsername());
            machine.setBookedAt(LocalDateTime.now());
            machine.setBookedUntil(LocalDateTime.now().plusHours(hours));
            machineRepository.save(machine);
            
            // Create notification
            String message = "You booked Washing Machine " + machine.getMachineNumber() + " for " + hours + " hour(s)";
            notificationService.createNotification(user.getUsername(), message, Notification.NotificationType.MACHINE_BOOKED, machine.getId());
        }
        
        return "redirect:/student/machines";
    }
    
    @GetMapping("/dhobi")
    public String dhobi(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.UserRole.STUDENT) {
            return "redirect:/login";
        }
        try {
            List<LaundryRequest> requests = laundryRequestRepository.findByStudentId(user.getUsername());
            model.addAttribute("requests", requests != null ? requests : new java.util.ArrayList<>());
        } catch (Exception e) {
            model.addAttribute("requests", new java.util.ArrayList<>());
        }
        return "student/dhobi";
    }
    
    @PostMapping("/dhobi/book")
    public String bookDhobi(@RequestParam String description, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.UserRole.STUDENT) {
            return "redirect:/login";
        }
        
        if (description == null || description.trim().isEmpty()) {
            model.addAttribute("error", "Description cannot be empty");
            List<LaundryRequest> requests = laundryRequestRepository.findByStudentId(user.getUsername());
            model.addAttribute("requests", requests != null ? requests : new java.util.ArrayList<>());
            return "student/dhobi";
        }
        
        try {
            LaundryRequest request = new LaundryRequest(user.getUsername(), user.getUsername(), description.trim());
            System.out.println("Creating request: " + request.getDescription() + " for " + request.getStudentName());
            LaundryRequest saved = laundryRequestRepository.save(request);
            System.out.println("Saved request with ID: " + saved.getId());
            
            long totalCount = laundryRequestRepository.count();
            System.out.println("Total requests in DB after save: " + totalCount);
        } catch (Exception e) {
            System.out.println("Error saving request: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Failed to submit request. Please try again.");
            List<LaundryRequest> requests = laundryRequestRepository.findByStudentId(user.getUsername());
            model.addAttribute("requests", requests != null ? requests : new java.util.ArrayList<>());
            return "student/dhobi";
        }
        
        return "redirect:/student/dhobi";
    }
    
    @GetMapping("/notifications")
    public String notifications(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        
        // Create sample notifications for now
        List<Notification> sampleNotifications = new java.util.ArrayList<>();
        model.addAttribute("notifications", sampleNotifications);
        
        return "student/notifications";
    }
    
    @GetMapping("/lost-found")
    public String lostFound(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        List<LostFound> myItems = lostFoundRepository.findByReportedBy(user.getUsername());
        List<LostFound> allItems = lostFoundRepository.findByStatus(LostFound.LostFoundStatus.REPORTED);
        
        // Separate active and history items
        List<LostFound> activeItems = myItems.stream()
            .filter(item -> item.getStatus() == LostFound.LostFoundStatus.REPORTED)
            .collect(java.util.stream.Collectors.toList());
        List<LostFound> historyItems = myItems.stream()
            .filter(item -> item.getStatus() != LostFound.LostFoundStatus.REPORTED)
            .collect(java.util.stream.Collectors.toList());
        
        model.addAttribute("activeItems", activeItems);
        model.addAttribute("historyItems", historyItems);
        model.addAttribute("allItems", allItems);
        model.addAttribute("currentUser", user.getUsername());
        return "student/lost-found";
    }
    
    @PostMapping("/lost-found/report")
    public String reportLostItem(@RequestParam String description, @RequestParam String type,
                                @RequestParam(value = "imageUrl", required = false) MultipartFile imageFile, HttpSession session) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return "redirect:/login";
            }
            
            String finalImageUrl = "no-image";
            
            if (imageFile != null && !imageFile.isEmpty()) {
                try {
                    byte[] imageBytes = imageFile.getBytes();
                    String base64Image = java.util.Base64.getEncoder().encodeToString(imageBytes);
                    String contentType = imageFile.getContentType();
                    if (contentType == null) contentType = "image/jpeg";
                    finalImageUrl = "data:" + contentType + ";base64," + base64Image;
                } catch (Exception e) {
                    finalImageUrl = "no-image";
                }
            }
            
            LostFound item = new LostFound(user.getUsername(), description, finalImageUrl, 
                                         LostFound.LostFoundType.valueOf(type.toUpperCase()));
            lostFoundRepository.save(item);
            return "redirect:/student/lost-found";
        } catch (Exception e) {
            return "redirect:/student/lost-found";
        }
    }
    
    @PostMapping("/lost-found/found/{id}")
    public String markAsFound(@PathVariable String id, @RequestParam(required = false) MultipartFile proofImage, HttpSession session) {
        User user = (User) session.getAttribute("user");
        LostFound item = lostFoundRepository.findById(id).orElse(null);
        
        if (item != null && !item.getReportedBy().equals(user.getUsername())) {
            item.setStatus(LostFound.LostFoundStatus.FOUND);
            String proofUrl = "no-proof";
            
            if (proofImage != null && !proofImage.isEmpty()) {
                proofUrl = "proof-uploaded-" + System.currentTimeMillis();
            }
            
            item.setResponse("Found by " + user.getUsername() + ". Proof: " + proofUrl);
            lostFoundRepository.save(item);
        }
        
        return "redirect:/student/lost-found";
    }
    
    @PostMapping("/lost-found/found-owner/{id}")
    public String markAsFoundByOwner(@PathVariable String id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        LostFound item = lostFoundRepository.findById(id).orElse(null);
        
        if (item != null && item.getReportedBy().equals(user.getUsername())) {
            item.setStatus(LostFound.LostFoundStatus.FOUND);
            item.setResponse("Item recovered by owner");
            lostFoundRepository.save(item);
        }
        
        return "redirect:/student/lost-found";
    }
    
    @PostMapping("/stones/cancel/{id}")
    public String cancelStone(@PathVariable String id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        Stone stone = stoneRepository.findById(id).orElse(null);
        
        if (stone != null && stone.getBookedBy().equals(user.getUsername())) {
            stone.setAvailable(true);
            stone.setBookedBy(null);
            stone.setBookedAt(null);
            stone.setBookedUntil(null);
            stoneRepository.save(stone);
            
            // Set cooldown time
            user.setLastCancelTime(LocalDateTime.now());
            userRepository.save(user);
            
            notificationService.createNotification(user.getUsername(), 
                "You cancelled Stone " + stone.getStoneNumber() + " booking. Wait 10 minutes before booking again.", 
                Notification.NotificationType.GENERAL, stone.getId());
        }
        
        return "redirect:/student/stones";
    }
    
    @PostMapping("/machines/cancel/{id}")
    public String cancelMachine(@PathVariable String id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        Machine machine = machineRepository.findById(id).orElse(null);
        
        if (machine != null && machine.getBookedBy().equals(user.getUsername())) {
            machine.setAvailable(true);
            machine.setBookedBy(null);
            machine.setBookedAt(null);
            machine.setBookedUntil(null);
            machineRepository.save(machine);
            
            // Set cooldown time
            user.setLastCancelTime(LocalDateTime.now());
            userRepository.save(user);
            
            notificationService.createNotification(user.getUsername(), 
                "You cancelled Washing Machine " + machine.getMachineNumber() + " booking. Wait 10 minutes before booking again.", 
                Notification.NotificationType.GENERAL, machine.getId());
        }
        
        return "redirect:/student/machines";
    }
}