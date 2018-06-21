package pl.pelipe.shoppinglist.item;

import java.time.LocalDateTime;

public class ItemDto {

    private Long id;

    private String name;

    private Integer userId;

    private Boolean done;

    private LocalDateTime createdAt = LocalDateTime.now();

    public ItemDto() {
    }

    public ItemDto(Long id, String name, Integer userId, Boolean done, LocalDateTime createdAt) {
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
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
