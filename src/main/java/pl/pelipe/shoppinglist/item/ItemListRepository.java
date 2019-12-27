package pl.pelipe.shoppinglist.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.pelipe.shoppinglist.user.UserEntity;

import java.util.List;

@Repository
public interface ItemListRepository extends JpaRepository<ItemListEntity, Long> {

    List<ItemListEntity> findAllByUser_UsernameAndRemovedFalseOrderByCreatedAtDesc(String username);

    ItemListEntity getByIdAndUser_Username(Long id, String username);

    List<ItemListEntity> findAllBySharedWithUsersContaining(UserEntity userEntity);

    ItemListEntity getByIdAndSharedWithUsersContaining(Long id, UserEntity username);

    @Modifying
    @Query("delete from ItemListEntity where removed = true")
    Integer deleteAllByRemovedIsTrue();
}
