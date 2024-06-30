package org.carl.user.inter;

import io.smallrye.mutiny.Uni;
import java.util.List;
import org.carl.generated.tables.pojos.Users;
import org.carl.user.model.User;

public interface IUserService {
    Uni<Integer> deleteUserById(Integer id);

    Uni<List<Users>> findAll();

    Uni<User> login(User u);

    Uni<User> register(User u);

    Uni<Boolean> sendCodeToEmail(String email);

    Uni<Boolean> isExistByEmail(String email);
}
