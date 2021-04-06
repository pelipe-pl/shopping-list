package pl.pelipe.shoppinglist.item;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import pl.pelipe.shoppinglist.user.UserEntity;
import pl.pelipe.shoppinglist.user.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase
//@DataJpaTest

public class ItemRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

//    @Autowired
//    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

//    @Autowired
//    private ItemListRepository itemListRepository;

    @Test
    public void testDeleteAllByRemovedIsTrue() {

        //given
        UserEntity givenUser = new UserEntity();
        givenUser.setUsername("test@user.com");
        givenUser.setName("Johny");
        givenUser.setLastName("Bravo");
        givenUser.setPassword("$2a$10$zV6jdFDkNQwd4gKPyq2IuR6AZ.yYPjod4dgj.aR0Mhu3wSC98QkUG2");
        entityManager.persist(givenUser);
        entityManager.flush();

        ItemEntity givenItem = new ItemEntity();
        givenItem.setId(1L);
        givenItem.setUser(givenUser);
        givenItem.setName("Test item");
        entityManager.persist(givenItem);
        entityManager.flush();

        //when
        ItemEntity foundItem = itemRepository.getByIdAndUserUsername(1L, "test@user.com");

        //then
        assertThat(foundItem.getName()).isEqualTo(givenItem.getName());

    }
}