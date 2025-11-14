package com.hostel.washing.repository;

import com.hostel.washing.model.Stone;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface StoneRepository extends MongoRepository<Stone, String> {
    List<Stone> findByAvailable(boolean available);
    List<Stone> findByBookedBy(String bookedBy);
}