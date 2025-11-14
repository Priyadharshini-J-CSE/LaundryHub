package com.hostel.washing.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "machines")
public class Machine {
    @Id
    private String id;
    private String machineNumber;
    private boolean available;
    private String bookedBy;
    private LocalDateTime bookedAt;
    private LocalDateTime bookedUntil;
    
    public Machine() {}
    
    public Machine(String machineNumber) {
        this.machineNumber = machineNumber;
        this.available = true;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getMachineNumber() { return machineNumber; }
    public void setMachineNumber(String machineNumber) { this.machineNumber = machineNumber; }
    
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    
    public String getBookedBy() { return bookedBy; }
    public void setBookedBy(String bookedBy) { this.bookedBy = bookedBy; }
    
    public LocalDateTime getBookedAt() { return bookedAt; }
    public void setBookedAt(LocalDateTime bookedAt) { this.bookedAt = bookedAt; }
    
    public LocalDateTime getBookedUntil() { return bookedUntil; }
    public void setBookedUntil(LocalDateTime bookedUntil) { this.bookedUntil = bookedUntil; }
}