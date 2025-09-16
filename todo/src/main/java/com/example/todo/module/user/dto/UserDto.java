package com.example.todo.module.user.dto;

public class UserDto {

    public static class SignUp {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class Login {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class Update {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class Response {
        private final Long id;
        private final String username;

        public Response(Long id, String username) {
            this.id = id;
            this.username = username;
        }
        
        public Long getId() { return id; }
        public String getUsername() { return username; }
    }
    
}
