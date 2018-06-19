package pl.pelipe.shoppinglist.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, Integer> {

    List<ItemEntity> findAllByUser_Id(Integer userId);

    List<ItemEntity> findAllByUser_Username(String username);

    List<ItemEntity> findAllByUser_IdAndDone(Integer userId, Boolean done);

    ItemEntity getById(Long id);
}
