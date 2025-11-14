package com.hostel.washing.controller;

import com.hostel.washing.model.User;
import com.hostel.washing.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {
    
    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }
    
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, 
                       HttpSession session, Model model) {
        User user = userRepository.findByUsername(username).orElse(null);
        
        if (user != null && user.getPassword().equals(password)) {
            session.setAttribute("user", user);
            if (user.getRole() == User.UserRole.STUDENT) {
                return "redirect:/student/dashboard";
            } else {
                return "redirect:/dhobi/dashboard";
            }
        }
        
        model.addAttribute("error", "Invalid credentials");
        return "login";
    }
    
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }
    
    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password,
                          @RequestParam String email, @RequestParam String role, Model model) {
        if (userRepository.findByUsername(username).isPresent()) {
            model.addAttribute("error", "Username already exists");
            return "register";
        }
        
        User user = new User(username, password, email, User.UserRole.valueOf(role.toUpperCase()));
        userRepository.save(user);
        
        return "redirect:/login";
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}