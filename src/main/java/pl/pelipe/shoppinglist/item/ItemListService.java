package pl.pelipe.shoppinglist.item;

import org.springframework.stereotype.Service;
import pl.pelipe.shoppinglist.user.UserEntity;
import pl.pelipe.shoppinglist.user.UserRepository;

import java.util.List;

@Service
public class ItemListService {
    private final ItemListRepository itemListRepository;
    private final UserRepository userRepository;
    private final ItemListFactory itemListFactory;

    public ItemListService(ItemListRepository itemListRepository, UserRepository userRepository, ItemListFactory itemListFactory) {
        this.itemListRepository = itemListRepository;
        this.userRepository = userRepository;
        this.itemListFactory = itemListFactory;
    }

    ItemListEntity getById(Long id) {
        return itemListRepository.getOne(id);
    }

    ItemListDto getByIdAndUsername(Long id, String username) {
        return toDto(itemListRepository.getByIdAndUser_Username(id, username));
    }

    public void add(ItemListDto itemListDto) {
        ItemListEntity itemListEntity = new ItemListEntity();
        itemListEntity.setName(itemListDto.getName());
        itemListEntity.setUser(userRepository.getById(itemListDto.getUserId()));
        itemListEntity.setRemoved(false);
        itemListRepository.save(itemListEntity);
    }

    public void add(List<ItemListEntity> itemListEntityList) {
        itemListRepository.saveAll(itemListEntityList);
    }

    List<ItemListEntity> findAllByUsernameAndRemovedFalse(String username) {
        return itemListRepository.findAllByUser_UsernameAndRemovedFalse(username);
    }

    public void remove(Long id, String username) {
        ItemListEntity itemListEntity = itemListRepository.getByIdAndUser_Username(id, username);
        itemListEntity.setRemoved(true);
        itemListRepository.save(itemListEntity);
    }

    public void addSampleLists(UserEntity userEntity) {
        itemListRepository.saveAll(itemListFactory.createSampleLists(userEntity));
    }

    private ItemListDto toDto(ItemListEntity itemListEntity) {
        ItemListDto itemListDto = new ItemListDto();
        itemListDto.setId(itemListEntity.getId());
        itemListDto.setName(itemListEntity.getName());
        itemListDto.setCreatedAt(itemListEntity.getCreatedAt());
        itemListDto.setRemoved(itemListEntity.getRemoved());
        return itemListDto;
    }
}
