package com.bookloop.service;

import com.bookloop.model.Student;
import com.bookloop.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for campus user lifecycle and karma operations.
 * Demonstrates Interface Abstraction.
 */
public interface IUserService {
    void registerUser(User user);
    Optional<User> findUserById(String id);
    User getUserById(String id);
    List<User> getAllUsers();
    List<Student> getAllStudents();
    User getActiveUser();
    void setActiveUser(User user);
    void awardKarma(String userId, int points);
    void recordSavings(String userId, double savings);
    List<User> getSustainabilityLeaderboard(int topN);
}
