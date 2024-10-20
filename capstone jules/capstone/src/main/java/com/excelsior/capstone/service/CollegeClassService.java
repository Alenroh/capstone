package com.excelsior.capstone.service;


import com.excelsior.capstone.entity.CollegeClass;
import com.excelsior.capstone.repository.CollegeClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CollegeClassService {

    private final CollegeClassRepository collegeClassRepository;

    @Autowired
    public CollegeClassService(CollegeClassRepository collegeClassRepository) {
        this.collegeClassRepository = collegeClassRepository;
    }

    public List<CollegeClass> getAllCollegeClasses() {
        return collegeClassRepository.findAll();
    }

    public Optional<CollegeClass> getCollegeClassById(Long id) {
        return collegeClassRepository.findById(id);
    }

    public CollegeClass saveCollegeClass(CollegeClass collegeClass) {
        return collegeClassRepository.save(collegeClass);
    }

    public void deleteCollegeClass(Long id) {
        collegeClassRepository.deleteById(id);
    }

    public Optional<CollegeClass> getCollegeClassByClassCode(String classCode) {
        return collegeClassRepository.findByClassCode(classCode);
    }
}
