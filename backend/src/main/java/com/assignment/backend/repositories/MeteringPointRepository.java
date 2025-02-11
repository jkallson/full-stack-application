package com.assignment.backend.repositories;

import com.assignment.backend.entities.CustomerEntity;
import com.assignment.backend.entities.MeteringPointEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeteringPointRepository extends CrudRepository<MeteringPointEntity, Long> {
    List<MeteringPointEntity> findAllByCustomer(CustomerEntity customerEntity);
}
