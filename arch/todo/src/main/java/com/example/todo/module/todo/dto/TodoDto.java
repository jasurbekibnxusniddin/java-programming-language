package com.example.todo.module.todo.dto;

public class TodoDto {

    public static class Create {
        private String title;
        private boolean completed;
        private Long userId;

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public boolean isCompleted() { return completed; }
        public void setCompleted(boolean completed) { this.completed = completed; }
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
    }

    public static class Update {
        private String title;
        private Boolean completed;
        private Long userId;

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public Boolean getCompleted() { return completed; }
        public void setCompleted(Boolean completed) { this.completed = completed; }
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
    }

    public static class Response {
        private final Long id;
        private final String title;
        private final boolean completed;
        private final Long userId;
        private final String username;

        public Response(Long id, String title, boolean completed, Long userId, String username) {
            this.id = id;
            this.title = title;
            this.completed = completed;
            this.userId = userId;
            this.username = username;
        }
        
        public Long getId() { return id; }
        public String getTitle() { return title; }
        public boolean isCompleted() { return completed; }
        public Long getUserId() { return userId; }
        public String getUsername() { return username; }
    }

    public static class SimpleResponse {
        private final Long id;
        private final String title;
        private final boolean completed;

        public SimpleResponse(Long id, String title, boolean completed) {
            this.id = id;
            this.title = title;
            this.completed = completed;
        }
        
        public Long getId() { return id; }
        public String getTitle() { return title; }
        public boolean isCompleted() { return completed; }
    }
}