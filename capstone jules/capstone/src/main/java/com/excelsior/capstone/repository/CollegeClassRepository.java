package com.excelsior.capstone.repository;


import com.excelsior.capstone.entity.CollegeClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CollegeClassRepository extends JpaRepository<CollegeClass, Long> {
    Optional<CollegeClass> findByClassCode(String classCode);
}
