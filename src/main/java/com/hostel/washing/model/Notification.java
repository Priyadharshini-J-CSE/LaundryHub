package com.hostel.washing.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "notifications")
public class Notification {
    @Id
    private String id;
    private String userId;
    private String message;
    private NotificationType type;
    private String relatedId;
    private LocalDateTime createdAt;
    private boolean read;
    
    public enum NotificationType {
        STONE_BOOKED, MACHINE_BOOKED, LAUNDRY_ACCEPTED, LAUNDRY_REJECTED, ITEM_FOUND, GENERAL
    }
    
    public Notification() {}
    
    public Notification(String userId, String message, NotificationType type, String relatedId) {
        this.userId = userId;
        this.message = message;
        this.type = type;
        this.relatedId = relatedId;
        this.createdAt = LocalDateTime.now();
        this.read = false;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public NotificationType getType() { return type; }
    public void setType(NotificationType type) { this.type = type; }
    
    public String getRelatedId() { return relatedId; }
    public void setRelatedId(String relatedId) { this.relatedId = relatedId; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }
}