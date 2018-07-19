package pl.pelipe.shoppinglist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.pelipe.shoppinglist.email.EmailService;
import pl.pelipe.shoppinglist.item.ItemListService;

import java.io.IOException;
import java.util.HashSet;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private ItemListService itemListService;
    private EmailService emailService;

    @Autowired
    public UserServiceImpl(RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository, ItemListService itemListService, EmailService emailService) {
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.itemListService = itemListService;
        this.emailService = emailService;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void save(UserEntity userEntity) throws IOException {
        userEntity.setPassword(bCryptPasswordEncoder.encode(userEntity.getPassword()));
        userEntity.setRoles(new HashSet<>(roleRepository.findAll()));
        userRepository.save(userEntity);
        itemListService.addSampleLists(userEntity);
        emailService.send(
                userEntity.getUsername(),
                "Welcome to shopping list",
                "Welcome to shopping list by Pelipe. Now you can manage your own list");
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
    public UserEntity findById(Long id) {
        return userRepository.getById(id);
    }
}