package pl.pelipe.shoppinglist.item;

import pl.pelipe.shoppinglist.user.UserEntity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "item_list")
public class ItemListEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty
    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private Boolean removed = false;

    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER, mappedBy = "list")
    private List<ItemEntity> itemList = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "shared_item_list",
            joinColumns = {@JoinColumn(name = "item_list_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_table_id")})
    private Set<UserEntity> sharedWithUsers = new HashSet<>();

    public ItemListEntity() {
    }

    public ItemListEntity(@NotEmpty String name, UserEntity user) {
        this.name = name;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Boolean getRemoved() {
        return removed;
    }

    public void setRemoved(Boolean removed) {
        this.removed = removed;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<ItemEntity> getItemList() {
        return itemList;
    }

    public void setItemList(List<ItemEntity> itemList) {
        this.itemList = itemList;
    }

    public Set<UserEntity> getSharedWithUsers() {
        return sharedWithUsers;
    }

    public void setSharedWithUsers(Set<UserEntity> sharedWithUsers) {
        this.sharedWithUsers = sharedWithUsers;
    }
}