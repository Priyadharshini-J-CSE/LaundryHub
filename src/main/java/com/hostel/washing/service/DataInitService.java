package com.hostel.washing.service;

import com.hostel.washing.model.*;
import com.hostel.washing.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
public class DataInitService implements CommandLineRunner {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private StoneRepository stoneRepository;
    
    @Autowired
    private MachineRepository machineRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Initialize sample users
        if (userRepository.count() == 0) {
            userRepository.save(new User("student1", "password", "student1@hostel.com", User.UserRole.STUDENT));
            userRepository.save(new User("dhobi1", "password", "dhobi1@hostel.com", User.UserRole.DHOBI));
        }
        
        // Initialize stones
        if (stoneRepository.count() == 0) {
            for (int i = 1; i <= 6; i++) {
                stoneRepository.save(new Stone("S" + i));
            }
        }
        
        // Initialize machines
        if (machineRepository.count() == 0) {
            for (int i = 1; i <= 4; i++) {
                machineRepository.save(new Machine("M" + i));
            }
        }
    }
}