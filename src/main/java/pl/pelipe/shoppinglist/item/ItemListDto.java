package pl.pelipe.shoppinglist.item;

import java.time.LocalDateTime;
import java.util.Objects;

public class ItemListDto {

    private Long id;

    private String name;

    private Long userId;

    private Boolean removed;

    private LocalDateTime createdAt;

    public ItemListDto() {
    }

    public ItemListDto(Long id, String name, Long userId, Boolean removed, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.userId = userId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemListDto that = (ItemListDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, userId);
    }
}