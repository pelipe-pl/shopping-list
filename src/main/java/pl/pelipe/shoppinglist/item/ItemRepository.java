package pl.pelipe.shoppinglist.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pelipe.shoppinglist.user.UserEntity;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, Integer> {

    ItemEntity getById(Long id);

    ItemEntity getByIdAndUserUsername(Long id, String username);

    List<ItemEntity> findAllByUser_UsernameAndList_IdAndRemovedFalseAndDoneTrue(String username, Long listId);

    List<ItemEntity> findAllByUser_UsernameAndList_IdAndRemovedFalseAndDoneFalse(String username, Long listId);

    List<ItemEntity> findAllByUser_UsernameAndRemovedFalseAndList_IdOrderByCreatedAtDesc(String username, Long ListId);

    List<ItemEntity> findAllByList_IdAndRemovedIsFalseOrderByCreatedAtDesc(Long id);
}
