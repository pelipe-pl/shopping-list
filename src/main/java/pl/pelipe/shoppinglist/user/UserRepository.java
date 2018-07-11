package pl.pelipe.shoppinglist.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    UserEntity getById(Long id);

    UserEntity findByUsername(String username);

    Boolean existsByUsername(String username);
}
