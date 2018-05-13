package pl.pelipe.shoppinglist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void save(UserEntity userEntity) {
        userEntity.setPassword(userEntity.getPassword());
        userEntity.setRoles(new HashSet(roleRepository.findAll()));
        userRepository.save(userEntity);
    }

    @Override
    public UserEntity findByUsername(String userName) {
        return userRepository.findByUserName(userName);
    }
}
