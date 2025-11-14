package com.hostel.washing.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "stones")
public class Stone {
    @Id
    private String id;
    private String stoneNumber;
    private boolean available;
    private String bookedBy;
    private LocalDateTime bookedAt;
    private LocalDateTime bookedUntil;
    
    public Stone() {}
    
    public Stone(String stoneNumber) {
        this.stoneNumber = stoneNumber;
        this.available = true;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getStoneNumber() { return stoneNumber; }
    public void setStoneNumber(String stoneNumber) { this.stoneNumber = stoneNumber; }
    
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    
    public String getBookedBy() { return bookedBy; }
    public void setBookedBy(String bookedBy) { this.bookedBy = bookedBy; }
    
    public LocalDateTime getBookedAt() { return bookedAt; }
    public void setBookedAt(LocalDateTime bookedAt) { this.bookedAt = bookedAt; }
    
    public LocalDateTime getBookedUntil() { return bookedUntil; }
    public void setBookedUntil(LocalDateTime bookedUntil) { this.bookedUntil = bookedUntil; }
}