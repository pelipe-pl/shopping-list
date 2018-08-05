package pl.pelipe.shoppinglist.user;

import java.io.IOException;

public interface UserService {
    void save(UserEntity userEntity) throws IOException;

    void update(UserEntity userEntity);

    UserEntity findByUsername(String username);

    UserEntity findById(Long id);

    Boolean sendPasswordResetToken(String username) throws IOException;

    String resetPassword(String tokenValue, String newPassword, String newPasswordConfirm) throws IOException;
}
