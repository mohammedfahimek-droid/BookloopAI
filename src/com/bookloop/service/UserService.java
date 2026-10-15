package com.bookloop.service;

import com.bookloop.exception.UserNotFoundException;
import com.bookloop.model.Student;
import com.bookloop.model.User;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Concrete implementation of IUserService.
 * Manages campus user profiles, eco-karma scores, and student sessions.
 */
public class UserService implements IUserService, Serializable {
    private static final long serialVersionUID = 1L;

    private final Map<String, User> userRegistry;
    private User activeUser;

    public UserService() {
        this.userRegistry = new ConcurrentHashMap<>();
    }

    @Override
    public void registerUser(User user) {
        if (user == null) throw new IllegalArgumentException("User cannot be null");
        userRegistry.put(user.getId(), user);
        if (activeUser == null) {
            activeUser = user;
        }
    }

    @Override
    public Optional<User> findUserById(String id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(userRegistry.get(id));
    }

    @Override
    public User getUserById(String id) {
        return findUserById(id).orElseThrow(() -> UserNotFoundException.forId(id));
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(userRegistry.values());
    }

    @Override
    public List<Student> getAllStudents() {
        return userRegistry.values().stream()
                .filter(u -> u instanceof Student)
                .map(u -> (Student) u)
                .collect(Collectors.toList());
    }

    @Override
    public User getActiveUser() {
        return activeUser;
    }

    @Override
    public void setActiveUser(User user) {
        if (user != null && userRegistry.containsKey(user.getId())) {
            this.activeUser = user;
        }
    }

    @Override
    public void awardKarma(String userId, int points) {
        User user = getUserById(userId);
        user.addKarma(points);
    }

    @Override
    public void recordSavings(String userId, double savings) {
        User user = getUserById(userId);
        user.recordSavings(savings);
    }

    @Override
    public List<User> getSustainabilityLeaderboard(int topN) {
        return userRegistry.values().stream()
                .sorted() // Uses Comparable in User based on karmaPoints
                .limit(topN)
                .collect(Collectors.toList());
    }
}
