package com.assignment.backend.repositories;

import com.assignment.backend.entities.MeteringPointEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeteringPointRepository extends CrudRepository<MeteringPointEntity, Long> {
}
