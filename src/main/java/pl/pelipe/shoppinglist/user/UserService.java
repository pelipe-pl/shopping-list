package pl.pelipe.shoppinglist.user;

public interface UserService {
    void save(UserEntity userEntity);

    UserEntity findByUsername(String username);
}
