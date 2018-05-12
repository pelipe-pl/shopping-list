package pl.pelipe.shoppinglist.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, Integer> {

    List<ItemEntity> findAllByUser_Id(Long userId);

    List<ItemEntity> findAllByUser_IdAndDone(Long userId, Boolean done);

    ItemEntity getById(Long id);
}
