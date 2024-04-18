package com.seb.service;

import com.seb.dao.UserDao;
import com.seb.model.User;

import java.util.List;

public class UserService {
    private UserDao userDao = new UserDao();

    public UserService() {
    }

    public UserService(UserDao userDaoMock) {
        this.userDao = userDaoMock;
    }

    // Retrieves a user by username
    public User getUserByUsername(String username) {
        return userDao.findUserByUsername(username);
    }

    // Retrieves a user's ID by username
    public Long getUserIdByUsername(String username) {
        User user = userDao.findUserByUsername(username);
        return user != null ? user.getId() : null;
    }

    // Retrieves all users
    public List<User> findAllUsers() {
        return userDao.findAllUsers();
    }

    // Updates a user's information
    public boolean updateUser(User user) {
        // Assume PasswordUtil.hash() is a method that hashes the password
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(user.getPassword());
        }
        return userDao.updateUserDetails(user);
    }

    // Registers a new user
    public boolean registerUser(String username, String password) {
        String hashedPassword = password; // Hash password
        User user = new User(null, username, hashedPassword, 1000, null, null, null);
        return userDao.createUser(user);
    }

    // Logs in a user
    public User loginUser(String username, String password) {
        User user = userDao.findUserByUsername(username);
        if (user != null) {
            // Verify the password matches (assuming PasswordUtil.check matches hashed passwords)
            if (password.equals(user.getPassword())) {
                return user;
            }
        }
        return null;
    }

    // Checks if the user is valid (for simplicity, keeping as is. Consider moving to an auth service.)
    public boolean isValidUser(String authHeader, String username) {
        String expectedToken = "Basic " + username + "-sebToken";
        return expectedToken.equals(authHeader);
    }

    public User getUserById(Long userId) {
        return userDao.findUserById(userId);
    }
}
