package com.excelsior.capstone.repository;


import com.excelsior.capstone.entity.ClassReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassReportRepository extends JpaRepository<ClassReport, Long> {
}
