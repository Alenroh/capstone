package com.excelsior.capstone.controller;

import com.excelsior.capstone.entity.CollegeClass;
import com.excelsior.capstone.entity.User;
import com.excelsior.capstone.service.CollegeClassService;
import com.excelsior.capstone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/college-class")
public class CollegeClassController {

    private final CollegeClassService collegeClassService;
    private final UserService userService;

    @Autowired
    public CollegeClassController(CollegeClassService collegeClassService, UserService userService) {
        this.collegeClassService = collegeClassService;
        this.userService = userService;
    }

    // Display the list of college classes and the form for adding/editing a class
    @GetMapping
    public String getAllCollegeClasses(Model model,
                                       @RequestParam(value = "successMessage", required = false) String successMessage,
                                       @RequestParam(value = "errorMessage", required = false) String errorMessage) {
        List<CollegeClass> classes = collegeClassService.getAllCollegeClasses();
        List<User> users = userService.getAllUsers(); // Fetch all users for the dropdown

        model.addAttribute("classes", classes);
        model.addAttribute("newClass", new CollegeClass());
        model.addAttribute("users", users); // Add users to the model

        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
        }
        if (successMessage != null) {
            model.addAttribute("successMessage", successMessage);
        }

        return "college-class";
    }

    // Load a class for editing
    @GetMapping("/edit")
    public String editCollegeClass(@RequestParam("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<CollegeClass> selectedClass = collegeClassService.getCollegeClassById(id);
        List<User> users = userService.getAllUsers();  // Fetch users for the dropdown

        if (selectedClass.isPresent()) {
            model.addAttribute("newClass", selectedClass.get());
            model.addAttribute("users", users);  // Add users to the model
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Class not found.");
            return "redirect:/college-class";
        }

        return "college-class";
    }

    // Save or update a college class with start/end date validation
    @PostMapping("/save")
    public String saveCollegeClass(@ModelAttribute("newClass") CollegeClass collegeClass,
                                   RedirectAttributes redirectAttributes) {
        try {
            if (collegeClass.getUser() == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Error: User must be selected.");
                return "redirect:/college-class";
            }

            // Validate that the start date is before or equal to the end date
            if (collegeClass.getStartDate().after(collegeClass.getEndDate())) {
                redirectAttributes.addFlashAttribute("errorMessage", "Error: Start date cannot be after the end date.");
                return "redirect:/college-class";
            }

            // Preserve the relationship with existing user and classes
            Optional<User> userOptional = userService.getUserById(collegeClass.getUser().getId());
            if (userOptional.isPresent()) {
                // Ensure existing user is associated
                collegeClass.setUser(userOptional.get());
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Error: Selected user does not exist.");
                return "redirect:/college-class";
            }

            // Preserve existing classes when saving or updating
            User existingUser = userOptional.get();
            List<CollegeClass> existingClasses = existingUser.getCollegeClasses();

            // If the class exists, update it, otherwise add it
            Optional<CollegeClass> existingClassOpt = existingClasses.stream()
                    .filter(c -> c.getId().equals(collegeClass.getId()))
                    .findFirst();

            if (existingClassOpt.isPresent()) {
                CollegeClass existingClass = existingClassOpt.get();
                existingClass.setClassName(collegeClass.getClassName());
                existingClass.setCredits(collegeClass.getCredits());
                existingClass.setInstructor(collegeClass.getInstructor());
                existingClass.setStartDate(collegeClass.getStartDate());
                existingClass.setEndDate(collegeClass.getEndDate());
                existingClass.setClassCode(collegeClass.getClassCode());
            } else {
                existingUser.getCollegeClasses().add(collegeClass); // Add new class
            }

            collegeClassService.saveCollegeClass(collegeClass);
            redirectAttributes.addFlashAttribute("successMessage", "College class saved successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving college class: " + e.getMessage());
        }
        return "redirect:/college-class";
    }

    // Handle form submission for deleting a class
    @PostMapping("/delete")
    public String deleteCollegeClass(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            collegeClassService.deleteCollegeClass(id);
            redirectAttributes.addFlashAttribute("successMessage", "College class deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting college class: " + e.getMessage());
        }
        return "redirect:/college-class";
    }
}
