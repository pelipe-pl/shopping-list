package pl.pelipe.shoppinglist.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemListLinkSharedRepository extends JpaRepository<ItemListLinkSharedEntity, Long> {

    ItemListLinkSharedEntity getByToken(String token);

}
