package com.excelsior.capstone.service;

import com.excelsior.capstone.entity.ClassReport;
import com.excelsior.capstone.repository.ClassReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClassReportService {

    private final ClassReportRepository classReportRepository;

    @Autowired
    public ClassReportService(ClassReportRepository classReportRepository) {
        this.classReportRepository = classReportRepository;
    }

    // Fetch all ClassReports
    public List<ClassReport> getAllClassReports() {
        return classReportRepository.findAll();
    }

    // Fetch a ClassReport by its ID
    public Optional<ClassReport> getClassReportById(Long id) {
        return classReportRepository.findById(id);
    }

    // Save a new or updated ClassReport
    public ClassReport saveClassReport(ClassReport classReport) {
        return classReportRepository.save(classReport);
    }

    // Delete a ClassReport by its ID
    public void deleteClassReport(Long id) {
        classReportRepository.deleteById(id);
    }
}
