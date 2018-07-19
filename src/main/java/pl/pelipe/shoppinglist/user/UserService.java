package pl.pelipe.shoppinglist.user;

import java.io.IOException;

public interface UserService {
    void save(UserEntity userEntity) throws IOException;

    void update(UserEntity userEntity);

    UserEntity findByUsername(String username);

    UserEntity findById(Long id);
}
