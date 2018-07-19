package pl.pelipe.shoppinglist.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, Integer> {

    ItemEntity getById(Long id);

    List<ItemEntity> findAllByUser_UsernameAndList_IdAndRemovedFalseAndDoneTrue(String username, Long listId);

    List<ItemEntity> findAllByUser_UsernameAndList_IdAndRemovedFalseAndDoneFalse(String username, Long listId);

    List<ItemEntity> findAllByUser_UsernameAndRemovedFalseAndList_IdOrderByDone(String username, Long ListId);
}
