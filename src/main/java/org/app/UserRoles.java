package org.app;

public class UserRoles {
    private int userId;
    private String username;
    private String role;

    public UserRoles(int userId, String username, String role) {
        this.userId = userId;
        this.username = username;
        this.role = role;
    }

    public UserRoles() {

    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setName(String username) {
        this.username = username;
    }

    public void setId(int userId) {
        this.userId = userId;
    }
}
