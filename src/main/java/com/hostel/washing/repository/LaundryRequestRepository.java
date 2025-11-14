package com.hostel.washing.repository;

import com.hostel.washing.model.LaundryRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface LaundryRequestRepository extends MongoRepository<LaundryRequest, String> {
    List<LaundryRequest> findByStudentId(String studentId);
    List<LaundryRequest> findByStudentName(String studentName);
    List<LaundryRequest> findByStatus(LaundryRequest.RequestStatus status);
    List<LaundryRequest> findByStatusNot(LaundryRequest.RequestStatus status);
}