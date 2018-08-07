package pl.pelipe.shoppinglist.user;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.pelipe.shoppinglist.email.EmailService;
import pl.pelipe.shoppinglist.item.ItemListService;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;

import static pl.pelipe.shoppinglist.config.Keys.URL_HOMEPAGE;
import static pl.pelipe.shoppinglist.config.Keys.URL_PASSWORD_RESET;

@Service
public class UserServiceImpl implements UserService {

    private static final String PASSWORD_RESET_CONTENT = "You have requested for new password. Following link is valid for 24 hours. " +
            "To reset your password, please click: \n";
    private static final String WELCOME_CONTENT = "Welcome to Shopping List by Pelipe. Now you can manage your own lists. ";
    private static final String PASSWORD_CHANGED_CONTENT = "Your password in Shopping List has been changed.";

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordResetTokenRepository tokenRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private ItemListService itemListService;
    private EmailService emailService;
    private Environment environment;

    @Autowired
    public UserServiceImpl(RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository, ItemListService itemListService, EmailService emailService, PasswordResetTokenRepository tokenRepository, Environment environment) {
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.itemListService = itemListService;
        this.emailService = emailService;
        this.tokenRepository = tokenRepository;
        this.environment = environment;
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
                "Welcome to Shopping List",
                WELCOME_CONTENT
                        + "Click" + " <a href='" + environment.getRequiredProperty(URL_HOMEPAGE)
                        + "'>here</a> to log in and start!");
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

    @Override
    public Boolean sendPasswordResetToken(String username) throws IOException {
        UserEntity userEntity = findByUsername(username);
        if (userEntity != null) {
            PasswordResetTokenEntity token = new PasswordResetTokenEntity();
            String tokenValue = RandomStringUtils.randomAlphabetic(20);
            token.setUser(userEntity);
            token.setToken(tokenValue);
            token.setActive(true);
            token.setExpiryDate(LocalDateTime.now().plusHours(24));
            emailService.send(
                    userEntity.getUsername(),
                    "Shopping List - Password Recovery",
                    PASSWORD_RESET_CONTENT +
                            " <a href=" + '"'
                            + environment.getRequiredProperty(URL_PASSWORD_RESET)
                            + "?token=" + tokenValue
                            + '"' + ">HERE</a>");
            tokenRepository.save(token);
            return true;
        } else return false;
    }

    @Override
    public String resetPassword(String tokenValue, String newPassword, String newPasswordConfirm) throws IOException {
        PasswordResetTokenEntity tokenEntity = tokenRepository.getByToken(tokenValue);
        if (!newPassword.equals(newPasswordConfirm)) return "Passwords do not match.";
        if (newPassword.length() < 6) return "Password must be longer than 6 characters";
        else if (tokenEntity == null) return "Invalid token.";
        else if (!tokenEntity.getActive()) return "Token is not active anymore.";
        else if (LocalDateTime.now().isAfter(tokenEntity.getExpiryDate())) return "Token has expired";
        else {
            UserEntity userEntity = tokenEntity.getUser();
            userEntity.setPassword(bCryptPasswordEncoder.encode(newPassword));
            userRepository.save(userEntity);
            tokenEntity.setActive(false);
            tokenRepository.save(tokenEntity);
            emailService.send(
                    userEntity.getUsername(),
                    "Shopping List - Password has been changed.",
                    PASSWORD_CHANGED_CONTENT);
            return "Success";
        }
    }
}