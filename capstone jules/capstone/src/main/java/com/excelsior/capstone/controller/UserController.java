package com.excelsior.capstone.controller;

import com.excelsior.capstone.entity.User;
import com.excelsior.capstone.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user-main")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Load the user management page and list all users
    @GetMapping
    public String showUserManagement(Model model,
                                     @ModelAttribute("successMessage") String successMessage,
                                     @ModelAttribute("errorMessage") String errorMessage) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);  // List of all users

        // Ensure the form object is available in the model
        if (!model.containsAttribute("userForm")) {
            model.addAttribute("userForm", new User());  // Blank user for adding new users
        }

        model.addAttribute("successMessage", successMessage);
        model.addAttribute("errorMessage", errorMessage);
        return "user-main";  // This refers to the Thymeleaf template (user-main.html)
    }

    // Save or update user while preserving college classes
    @PostMapping("/save")
    public String saveUser(@ModelAttribute("userForm") @Valid User user,
                           BindingResult result,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            // If there are validation errors, reload the form with errors
            List<User> users = userService.getAllUsers();
            model.addAttribute("users", users);  // Reload the list of users
            model.addAttribute("errorMessage", "Validation error occurred. Please fix the errors and try again.");
            return "user-main";  // Reload form with validation errors
        }

        // Save user if there are no validation errors and preserve classes
        try {
            userService.saveUser(user);  // Service method handles the logic of preserving classes
            redirectAttributes.addFlashAttribute("successMessage", "User saved successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving user: " + e.getMessage());
        }

        return "redirect:/user-main";  // Redirect to avoid form resubmission on page refresh
    }

    // Load user data for editing
    @GetMapping("/edit")
    public String editUser(@RequestParam Long id,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            model.addAttribute("userForm", user.get());  // Set the user to be edited
            List<User> users = userService.getAllUsers();
            model.addAttribute("users", users);  // Reload the list of users
            return "user-main";  // Re-load the user-main.html with userForm populated
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "User not found");
            return "redirect:/user-main";  // Redirect if user not found
        }
    }

    // Handle delete action
    @PostMapping("/delete")
    public String deleteUser(@RequestParam Long id,
                             RedirectAttributes redirectAttributes) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "User not found");
        }
        return "redirect:/user-main";
    }
}
