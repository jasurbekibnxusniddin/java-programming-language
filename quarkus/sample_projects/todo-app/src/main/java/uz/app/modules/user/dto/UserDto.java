package uz.app.modules.user.dto;

import uz.app.modules.user.entity.User;

public class UserDto {

    public record response(
            Long id,
            String name,
            String email
            ) {
    }

    public record create(
            String name,
            String email,
            String password
            ) {
        
        public User toEntity() {
            User user = new User();
            user.name = this.name;
            user.email = this.email;
            // Password will be hashed in the service
            return user;
        }
    }

    public record update(
            String name,
            String email
            ) {
    }
}
