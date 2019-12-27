package pl.pelipe.shoppinglist.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ItemListLinkSharedRepository extends JpaRepository<ItemListLinkSharedEntity, Long> {

    ItemListLinkSharedEntity getByTokenAndActiveTrueAndExpiryDateIsAfter(String token, LocalDateTime expiryDate);

    Integer deleteAllByExpiryDateBefore(LocalDateTime expiryDate);
}
