package pl.pelipe.shoppinglist.item;

import org.springframework.stereotype.Service;
import pl.pelipe.shoppinglist.user.UserEntity;
import pl.pelipe.shoppinglist.user.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    List<ItemListDto> findAllByUsernameAndRemovedFalse(String username) {
        return toDtoList(itemListRepository.findAllByUser_UsernameAndRemovedFalseOrderByCreatedAtDesc(username));
    }

    List<ItemListDto> findAllShared(String username) {
        UserEntity userEntity = userRepository.findByUsername(username);
        return toDtoList(itemListRepository.findAllBySharedWithUsersContaining(userEntity));
    }

    public void remove(Long id, String username) {
        ItemListEntity itemListEntity = itemListRepository.getByIdAndUser_Username(id, username);
        itemListEntity.setRemoved(true);
        itemListRepository.save(itemListEntity);
    }

    void rename(Long id, String name, String username) {
        ItemListEntity itemListEntity = itemListRepository.getByIdAndUser_Username(id, username);
        itemListEntity.setName(name);
        itemListRepository.save(itemListEntity);
    }

    Integer share(Long id, String listOwnerUsername, String listSharerUsername) {
        if (listOwnerUsername.toLowerCase().equals(listSharerUsername.toLowerCase())) return 0;
        ItemListEntity itemList = itemListRepository.getByIdAndUser_Username(id, listOwnerUsername);
        if (itemList == null) throw new IllegalArgumentException("The user does not have item list with this id");
        UserEntity listSharerUser = userRepository.findByUsername(listSharerUsername);
        if (listSharerUser == null)
            return 1;
        Set<UserEntity> sharers = itemList.getSharedWithUsers();
        if (sharers.contains(listSharerUser))
            return 2;
        sharers.add(listSharerUser);
        itemList.setSharedWithUsers(sharers);
        itemListRepository.save(itemList);
        return 3;
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
        itemListDto.setTotalSize(itemListEntity.getItemList()
                .stream()
                .filter(itemEntity -> !itemEntity.getRemoved())
                .count());
        itemListDto.setUndoneSize(itemListEntity.getItemList()
                .stream()
                .filter(itemEntity -> !itemEntity.getRemoved() && !itemEntity.getDone())
                .count());

        Set<String> sharedWithUsersDtoSet = new HashSet<>();
        for (UserEntity user : itemListEntity.getSharedWithUsers()) {
            sharedWithUsersDtoSet.add(user.getUsername());
        }
        itemListDto.setSharedWithUsers(sharedWithUsersDtoSet);
        return itemListDto;
    }

    private List<ItemListDto> toDtoList(List<ItemListEntity> listEntities) {
        return listEntities.stream()
                .map(itemListEntity -> toDto(itemListEntity))
                .collect(Collectors.toList());
    }
}
