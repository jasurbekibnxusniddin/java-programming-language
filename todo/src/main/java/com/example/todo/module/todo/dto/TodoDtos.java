package com.example.todo.module.todo.dto;

public class TodoDtos {
     public static class Create {
        private String title;

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
    }

    public static class Update {
        private String title;
        private boolean completed;

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public boolean isCompleted() { return completed; }
        public void setCompleted(boolean completed) { this.completed = completed; }
    }

    public static class Response {
        private final Long id;
        private final String title;
        private final boolean completed;
    
        public Response(Long id, String title, boolean completed) {
            this.id = id;
            this.title = title;
            this.completed = completed;
        }
    
        public Long getId() { return id; }
        public String getTitle() { return title; }
        public boolean isCompleted() { return completed; }
    }    
}
