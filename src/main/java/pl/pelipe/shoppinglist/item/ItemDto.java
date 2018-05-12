package pl.pelipe.shoppinglist.item;

import java.time.LocalDateTime;

public class ItemDto {

    private Long id;

    private String name;

    private Long userId;

    private Boolean done;

    private LocalDateTime createdAt;

    public ItemDto() {
    }

    public ItemDto(Long id, String name, Long userId, Boolean done, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.done = done;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
