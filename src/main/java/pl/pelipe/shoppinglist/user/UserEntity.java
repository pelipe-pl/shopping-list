package pl.pelipe.shoppinglist.user;

import org.hibernate.annotations.DynamicUpdate;
import pl.pelipe.shoppinglist.item.ItemListEntity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_table")
@DynamicUpdate
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String lastName;

    @Email
    @Column(unique = true, nullable = false)
    private String username;

    @NotNull
    private String password;

    @Transient
    private String passwordConfirm;

    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToMany
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @ManyToMany(mappedBy = "sharedToUsers")
    private Set<ItemListEntity> sharedItemLists = new HashSet<>();

    public UserEntity() {
    }

    public UserEntity(String name, String lastName, @Email String username, @NotNull String password, String passwordConfirm, LocalDateTime createdAt, Set<Role> roles) {
        this.name = name;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.createdAt = createdAt;
        this.roles = roles;
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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<ItemListEntity> getSharedItemLists() {
        return sharedItemLists;
    }

    public void setSharedItemLists(Set<ItemListEntity> sharedItemLists) {
        this.sharedItemLists = sharedItemLists;
    }
}
