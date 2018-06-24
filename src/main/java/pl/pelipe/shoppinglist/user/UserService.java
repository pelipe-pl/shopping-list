package pl.pelipe.shoppinglist.user;

public interface UserService {
    void save(UserEntity userEntity);

    void update(UserEntity userEntity);

    UserEntity findByUsername(String username);

    UserEntity findById(Integer id);
}
