package pl.pelipe.shoppinglist.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, Integer> {

    ItemEntity getByIdAndUserUsername(Long id, String username);

    List<ItemEntity> findAllByUser_UsernameAndList_IdAndRemovedFalseAndDoneTrue(String username, Long listId);

    List<ItemEntity> findAllByUser_UsernameAndList_IdAndRemovedFalseAndDoneFalse(String username, Long listId);

    List<ItemEntity> findAllByUser_UsernameAndRemovedFalseAndList_IdOrderByCreatedAtDesc(String username, Long ListId);
}
