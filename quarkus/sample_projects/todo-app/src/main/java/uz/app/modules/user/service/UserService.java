package uz.app.modules.user.service;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import uz.app.modules.user.dto.UserDto;
import uz.app.modules.user.entity.User;
import uz.app.modules.user.repository.UserRepository;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;

    @Transactional
    public UserDto.response create(UserDto.create in) {
        if (userRepository.find("email", in.email()).firstResult() != null) {
            throw new BadRequestException("User with email " + in.email() + " already exists");
        }

        User user = in.toEntity();
        userRepository.persist(user);
        return toResponse(user);
    }

    public UserDto.response get(Long id) {
        return userRepository.findByIdOptional(id)
                .map(this::toResponse)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
    }

    public List<UserDto.response> list() {
        return userRepository.listAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public UserDto.response update(Long id, UserDto.update in) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new NotFoundException("User not found with id: " + id);
        }

        if (in.email() != null && !in.email().equals(user.email)) {
             if (userRepository.find("email", in.email()).firstResult() != null) {
                throw new BadRequestException("User with email " + in.email() + " already exists");
            }
            user.email = in.email();
        }

        if (in.name() != null) {
            user.name = in.name();
        }

        return toResponse(user);
    }

    @Transactional
    public void delete(Long id) {
        boolean deleted = userRepository.deleteById(id);
        if (!deleted) {
            throw new NotFoundException("User not found with id: " + id);
        }
    }

    private UserDto.response toResponse(User user) {
        return new UserDto.response(
                user.id,
                user.name,
                user.email
        );
    }
}
