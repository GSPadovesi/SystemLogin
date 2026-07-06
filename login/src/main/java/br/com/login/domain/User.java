package br.com.login.domain;

import java.util.UUID;

public class User {
    private final UUID id;
    private String username;
    private String email;
    private String password;
    private UserRole role;

    private User(UUID id, String username, String email, String password, UserRole role){
        this.id = id;
        setUsername(username);
        setEmail(email);
        setPassword(password);
        setRole(role);
    }

    public static User create(String username, String email, String password){
        return new User(
        null,
          username,
          email,
          password,
          UserRole.USER
        );
    }

    public static User restore(UUID id, String username, String email, String password, UserRole role) {
        return new User(
                id,
                username,
                email,
                password,
                role
        );
    }

    public void changeUsername(String username) {
        setUsername(username);
    }

    public void changePassword(String password) {
        setPassword(password);
    }

    public void changeRole(UserRole role) {
        setRole(role);
    }

    public String getPassword() {
        return password;
    }

    private void setPassword(String password) { this.password = password; }

    public String getEmail() {
        return email;
    }

    private void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    private void setUsername(String username) {
        this.username = username;
    }

    public UUID getId() {
        return id;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
