package pl.pelipe.shoppinglist.user;

import org.springframework.stereotype.Service;

@Service
public class UserServiceFacade {

    private final UserRepository userRepository;

    public UserServiceFacade(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity getById(Long id) {
        return userRepository.getById(id);
    }
}
