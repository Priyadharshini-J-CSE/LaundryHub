package com.hostel.washing.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "laundry_requests")
public class LaundryRequest {
    @Id
    private String id;
    private String studentId;
    private String studentName;
    private String description;
    private RequestStatus status;
    private LocalDateTime requestedAt;
    private String dhobiId;
    private String currentStage;
    
    public enum RequestStatus {
        PENDING, ACCEPTED, REJECTED, COLLECTED, WASHING, IRONING, READY_TO_PICK, DELIVERED
    }
    
    public LaundryRequest() {}
    
    public LaundryRequest(String studentId, String studentName, String description) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.description = description;
        this.status = RequestStatus.PENDING;
        this.requestedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public RequestStatus getStatus() { return status; }
    public void setStatus(RequestStatus status) { this.status = status; }
    
    public LocalDateTime getRequestedAt() { return requestedAt; }
    public void setRequestedAt(LocalDateTime requestedAt) { this.requestedAt = requestedAt; }
    
    public String getDhobiId() { return dhobiId; }
    public void setDhobiId(String dhobiId) { this.dhobiId = dhobiId; }
    
    public String getCurrentStage() { return currentStage; }
    public void setCurrentStage(String currentStage) { this.currentStage = currentStage; }
}