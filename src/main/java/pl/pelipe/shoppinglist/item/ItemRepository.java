package pl.pelipe.shoppinglist.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, Integer> {

    ItemEntity getById(Long id);

    ItemEntity getByIdAndUserUsername(Long id, String username);

    List<ItemEntity> findAllByUser_UsernameAndList_IdAndRemovedFalseAndDoneTrue(String username, Long listId);

    List<ItemEntity> findAllByUser_UsernameAndList_IdAndRemovedFalseAndDoneFalse(String username, Long listId);

    List<ItemEntity> findAllByUser_UsernameAndRemovedFalseAndList_IdOrderByCreatedAtDesc(String username, Long listId);

    List<ItemEntity> findAllByUser_IdAndList_IdAndRemovedFalseOrderByCreatedAtDesc(Long userId, Long listId);

    List<ItemEntity> findAllByList_IdAndRemovedIsFalseOrderByCreatedAtDesc(Long id);

    List<ItemEntity> findAllByListIdAndRemovedIsFalseAndDoneIsTrue(Long id);

    @Modifying
    @Query("delete from ItemEntity where removed = true")
    Integer deleteAllByRemovedIsTrue();
}