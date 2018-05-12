package pl.pelipe.shoppinglist.user;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void add(UserDto userDto) {
        UserEntity userEntity = new UserEntity(
                userDto.getEmail(),
                userDto.getName(),
                userDto.getLastName(),
                userDto.getPassword());
        userRepository.save(userEntity);
    }

    public UserDto getById(Long id) {
        return toDto(userRepository.getById(id));
    }

    private UserDto toDto(UserEntity userEntity) {
        return new UserDto(
                userEntity.getId(),
                userEntity.getName(),
                userEntity.getLastName(),
                userEntity.getEmail(),
                userEntity.getCreatedAt());
    }
}
