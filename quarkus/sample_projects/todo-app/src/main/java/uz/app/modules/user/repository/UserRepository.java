package uz.app.modules.user.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import uz.app.modules.user.entity.User;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {
}
