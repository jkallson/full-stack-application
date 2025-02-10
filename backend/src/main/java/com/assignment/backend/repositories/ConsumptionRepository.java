package com.assignment.backend.repositories;

import com.assignment.backend.entities.ConsumptionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumptionRepository extends CrudRepository<ConsumptionEntity, Long> {
}
