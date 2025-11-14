package com.hostel.washing.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "lost_found")
public class LostFound {
    @Id
    private String id;
    private String reportedBy;
    private String itemDescription;
    private String imageUrl;
    private LostFoundType type;
    private LostFoundStatus status;
    private LocalDateTime reportedAt;
    private String response;
    
    public enum LostFoundType {
        DHOBI, STONE_MACHINE
    }
    
    public enum LostFoundStatus {
        REPORTED, FOUND, NOT_FOUND
    }
    
    public LostFound() {}
    
    public LostFound(String reportedBy, String itemDescription, String imageUrl, LostFoundType type) {
        this.reportedBy = reportedBy;
        this.itemDescription = itemDescription;
        this.imageUrl = imageUrl;
        this.type = type;
        this.status = LostFoundStatus.REPORTED;
        this.reportedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getReportedBy() { return reportedBy; }
    public void setReportedBy(String reportedBy) { this.reportedBy = reportedBy; }
    
    public String getItemDescription() { return itemDescription; }
    public void setItemDescription(String itemDescription) { this.itemDescription = itemDescription; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    public LostFoundType getType() { return type; }
    public void setType(LostFoundType type) { this.type = type; }
    
    public LostFoundStatus getStatus() { return status; }
    public void setStatus(LostFoundStatus status) { this.status = status; }
    
    public LocalDateTime getReportedAt() { return reportedAt; }
    public void setReportedAt(LocalDateTime reportedAt) { this.reportedAt = reportedAt; }
    
    public String getResponse() { return response; }
    public void setResponse(String response) { this.response = response; }
}