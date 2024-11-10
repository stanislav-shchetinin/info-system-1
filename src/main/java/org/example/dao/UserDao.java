package org.example.dao;

import org.example.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private static List<User> users = new ArrayList<>();
    private static int userId = 1;

    public List<User> getAllUsers() {
        return users;
    }

    public void addUser(User user) {
        user.setId(userId++);
        users.add(user);
    }

    public User getUserById(int id) {
        return users.stream().filter(u -> u.getId() == id).findFirst().orElse(null);
    }

    public void updateUser(User user) {
        User existingUser = getUserById(user.getId());
        if (existingUser != null) {
            existingUser.setName(user.getName());
            existingUser.setEmail(user.getEmail());
        }
    }

    public void deleteUser(int id) {
        users.removeIf(u -> u.getId() == id);
    }
}