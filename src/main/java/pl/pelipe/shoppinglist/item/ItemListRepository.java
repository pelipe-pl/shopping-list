package pl.pelipe.shoppinglist.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemListRepository extends JpaRepository<ItemListEntity, Long> {

    List<ItemListEntity> findAllByUser_IdAndRemovedFalse(Long id);

}
