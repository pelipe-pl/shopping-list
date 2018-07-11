package pl.pelipe.shoppinglist.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, Integer> {

    ItemEntity getById(Long id);

    List<ItemEntity> findAllByUser_UsernameAndRemovedFalseOrderByDone(String username);

    List<ItemEntity> findAllByUser_UsernameAndRemovedFalseAndDoneTrue(String username);

    List<ItemEntity> findAllByUser_UsernameAndRemovedFalseAndDoneFalse(String username);

    List<ItemEntity> findAllByUser_UsernameAndRemovedFalseAndList_Id(String username, Long ListId);
}
