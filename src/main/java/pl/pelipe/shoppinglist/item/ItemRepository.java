package pl.pelipe.shoppinglist.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, Integer> {

    List<ItemEntity> findAllByUser_UsernameOrderByDone(String username);

    ItemEntity getById(Long id);
}
