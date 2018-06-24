package pl.pelipe.shoppinglist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void save(UserEntity userEntity) {
        userEntity.setPassword(bCryptPasswordEncoder.encode(userEntity.getPassword()));
        userEntity.setRoles(new HashSet<>(roleRepository.findAll()));
        userRepository.save(userEntity);
    }

    @Override
    public void update(UserEntity updatedUser) {
        UserEntity userEntity = userRepository.getById(updatedUser.getId());
        if (updatedUser.getName() != null) userEntity.setName(updatedUser.getName());
        if (updatedUser.getLastName() != null) userEntity.setLastName(updatedUser.getLastName());
        userRepository.save(userEntity);
    }

    @Override
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserEntity findById(Integer id) {
        return userRepository.getById(id);
    }
}