package com.excelsior.capstone.service;

import com.excelsior.capstone.entity.User;
import com.excelsior.capstone.entity.CollegeClass;
import com.excelsior.capstone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Retrieve all users
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Find a user by ID
    public Optional<User> getUserById(Long id) {
        return Optional.ofNullable(id)
                .flatMap(userRepository::findById);
    }

    // Find a user by username
    public Optional<User> getUserByUsername(String username) {
        return Optional.ofNullable(username)
                .filter(u -> !u.isEmpty())
                .flatMap(userRepository::findByUsername);
    }

    // Save or update a user while preserving existing collegeClasses
    @Transactional
    public User saveUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        if (user.getId() != null) {
            // Fetch the existing user and update details
            return userRepository.findById(user.getId()).map(existingUser -> {
                existingUser.setUsername(user.getUsername());
                existingUser.setEmail(user.getEmail());
                existingUser.setFirstName(user.getFirstName());
                existingUser.setLastName(user.getLastName());

                // Preserve and update CollegeClasses
                List<CollegeClass> newClasses = user.getCollegeClasses();
                if (newClasses != null) {
                    // Map existing classes for easier lookups
                    Map<Long, CollegeClass> existingClassesMap = existingUser.getCollegeClasses().stream()
                            .collect(Collectors.toMap(CollegeClass::getId, Function.identity()));

                    // Update or add new classes
                    newClasses.forEach(newClass -> {
                        CollegeClass existingClass = existingClassesMap.get(newClass.getId());
                        if (existingClass != null) {
                            // Update existing class details
                            existingClass.setClassName(newClass.getClassName());
                            existingClass.setCredits(newClass.getCredits());
                            existingClass.setInstructor(newClass.getInstructor());
                            existingClass.setStartDate(newClass.getStartDate());
                            existingClass.setEndDate(newClass.getEndDate());
                            existingClass.setClassCode(newClass.getClassCode());
                        } else {
                            // Add the new class
                            newClass.setUser(existingUser);
                            existingUser.getCollegeClasses().add(newClass);
                        }
                    });

                    // Remove classes that are no longer in the updated list
                    existingUser.getCollegeClasses().removeIf(existingClass ->
                            newClasses.stream().noneMatch(newClass -> newClass.getId().equals(existingClass.getId())));
                }

                return userRepository.save(existingUser);
            }).orElseThrow(() -> new IllegalArgumentException("User not found for the given ID"));
        }

        // If it's a new user, save directly
        return userRepository.save(user);
    }

    // Delete a user by ID and cascade delete their associated collegeClasses
    public void deleteUser(Long id) {
        if (id == null || !userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found for the given ID: " + id);
        }
        userRepository.deleteById(id);
    }
}

// Custom exception class for better error handling
class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
