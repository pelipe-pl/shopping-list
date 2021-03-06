package pl.pelipe.shoppinglist.item;

import java.time.LocalDateTime;

public class ItemDto {

    private Long id;

    private String name;

    private Long userId;

    private Long listId;

    private Boolean done = false;

    private Boolean removed = false;

    private LocalDateTime createdAt = LocalDateTime.now();

    public ItemDto() {
    }

    public ItemDto(Long id, String name, Long userId, Boolean done, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.done = done;
        this.createdAt = createdAt;
    }

    public ItemDto(Long id, String name, Long userId, Long listId, Boolean done, Boolean removed, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.listId = listId;
        this.done = done;
        this.removed = removed;
        this.createdAt = createdAt;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
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

    public Long getListId() {
        return listId;
    }

    public void setListId(Long listId) {
        this.listId = listId;
    }
}
