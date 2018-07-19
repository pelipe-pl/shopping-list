package pl.pelipe.shoppinglist.item;

import org.springframework.stereotype.Service;
import pl.pelipe.shoppinglist.user.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;
    private final ItemListService itemListService;

    public ItemService(ItemRepository itemRepository, UserService userService, ItemListService itemListService) {
        this.itemRepository = itemRepository;
        this.userService = userService;
        this.itemListService = itemListService;
    }

    public void add(ItemDto itemDto) {
        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setName(itemDto.getName());
        itemEntity.setUser(userService.findById(itemDto.getUserId()));
        itemEntity.setList(itemListService.getById(itemDto.getListId()));
        itemEntity.setDone(false);
        itemEntity.setRemoved(false);
        itemRepository.save(itemEntity);
    }

    public List<ItemDto> findAllByUsernameAndListId(String username, Long listId) {
        return itemRepository.findAllByUser_UsernameAndRemovedFalseAndList_IdOrderByDone(username, listId)
                .stream()
                .map(i -> toDto(i))
                .collect(Collectors.toList());
    }

    public void findDoneAndSetRemoved(String username) {
        List<ItemEntity> items = itemRepository.findAllByUser_UsernameAndRemovedFalseAndDoneTrue(username);
        items.forEach(i -> i.setRemoved(true));
        itemRepository.saveAll(items);
    }

    public void findAllNotRemovedAndSetDone(String username) {
        List<ItemEntity> items = itemRepository.findAllByUser_UsernameAndRemovedFalseAndDoneFalse(username);
        items.forEach(i -> i.setDone(true));
        itemRepository.saveAll(items);
    }

    public void setDone(Long id, Boolean done) {
        ItemEntity itemEntity = itemRepository.getById(id);
        itemEntity.setDone(done);
        itemRepository.save(itemEntity);
    }

    public void setRemoved(Long id) {
        ItemEntity itemEntity = itemRepository.getById(id);
        itemEntity.setRemoved(true);
        itemRepository.save(itemEntity);
    }

    private ItemDto toDto(ItemEntity itemEntity) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(itemEntity.getId());
        itemDto.setUserId(itemEntity.getUser().getId());
        itemDto.setCreatedAt(itemEntity.getCreatedAt());
        itemDto.setListId(itemEntity.getId());
        itemDto.setDone(itemEntity.getDone());
        itemDto.setRemoved(itemEntity.getRemoved());
        itemDto.setName(itemEntity.getName());
        return itemDto;
    }
}