package com.hostel.washing.repository;

import com.hostel.washing.model.LostFound;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface LostFoundRepository extends MongoRepository<LostFound, String> {
    List<LostFound> findByReportedBy(String reportedBy);
    List<LostFound> findByType(LostFound.LostFoundType type);
    List<LostFound> findByStatus(LostFound.LostFoundStatus status);
}