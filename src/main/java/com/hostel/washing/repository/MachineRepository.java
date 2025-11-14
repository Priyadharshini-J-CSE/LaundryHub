package com.hostel.washing.repository;

import com.hostel.washing.model.Machine;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface MachineRepository extends MongoRepository<Machine, String> {
    List<Machine> findByAvailable(boolean available);
    List<Machine> findByBookedBy(String bookedBy);
}