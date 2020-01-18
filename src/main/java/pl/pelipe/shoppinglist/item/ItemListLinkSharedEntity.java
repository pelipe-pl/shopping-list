package pl.pelipe.shoppinglist.item;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDateTime;

@Entity
@Table(name = "item_list_link_shared")

public class ItemListLinkSharedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @ManyToOne(targetEntity = ItemListEntity.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(nullable = false, name = "item_list_id")
    public ItemListEntity listEntity;

    @Email
    public String emailAddress;

    public String token;

    public Boolean active;

    public LocalDateTime creationDate = LocalDateTime.now();

    public LocalDateTime expiryDate;

    public ItemListLinkSharedEntity() {
    }

    public ItemListLinkSharedEntity(ItemListEntity listEntity, String token, Boolean active, LocalDateTime expiryDate) {
        this.listEntity = listEntity;
        this.token = token;
        this.active = active;
        this.expiryDate = expiryDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ItemListEntity getListEntity() {
        return listEntity;
    }

    public void setListEntity(ItemListEntity listEntity) {
        this.listEntity = listEntity;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}