package com.excelsior.capstone.controller;

import com.excelsior.capstone.entity.User;
import com.excelsior.capstone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/class-report")
public class ClassReportController {

    private final UserService userService;

    @Autowired
    public ClassReportController(UserService userService) {
        this.userService = userService;
    }

    // Show the report terminal with a user dropdown
    @GetMapping
    public String showReportTerminal(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("reportGenerated", false);  // Initial state without report

        return "class-report";  // Serve the class-report.html template
    }

    // Generate the report for a specific user
    @GetMapping("/generate")
    public String generateReport(@RequestParam("userId") Long userId, Model model) {
        Optional<User> userOptional = userService.getUserById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            model.addAttribute("selectedUser", user);
            model.addAttribute("classes", user.getCollegeClasses());  // Add college classes
            model.addAttribute("currentDate", LocalDateTime.now());  // Add current date

            model.addAttribute("reportGenerated", true);  // Set reportGenerated to true to show the table
        } else {
            model.addAttribute("errorMessage", "User not found.");
            model.addAttribute("reportGenerated", false);
        }

        return "class-report";  // Serve the same page with report details
    }
}
