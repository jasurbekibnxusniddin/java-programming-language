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
            String email
            ) {
        
        public User toEntity() {
            User user = new User();
            user.name = this.name;
            user.email = this.email;
            return user;
        }
    }

    public record update(
            String name,
            String email
            ) {
    }
}
