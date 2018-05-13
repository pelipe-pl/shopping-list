package pl.pelipe.shoppinglist.item;

import org.springframework.stereotype.Service;

@Service
public class ItemService {

//    private final ItemRepository itemRepository;
//    private final UserServiceFacade userService;
//
//    public ItemService(ItemRepository itemRepository, UserServiceFacade userService) {
//        this.itemRepository = itemRepository;
//        this.userService = userService;
//    }
//
//    public void add(ItemDto itemDto) {
//        itemRepository.save(toEntity(itemDto));
//    }
//
//    public void setDone(Long id) {
//        ItemEntity itemEntity = itemRepository.getById(id);
//        itemEntity.setDone(true);
//        itemRepository.save(itemEntity);
//    }
//
//    private ItemEntity toEntity(ItemDto itemDto) {
//        return new ItemEntity(
//                itemDto.getId(),
//                itemDto.getName(),
//                userService.getById(itemDto.getUserId()),
//                itemDto.getDone(),
//                itemDto.getCreatedAt());
//    }
//
//    private ItemDto toDto(ItemEntity itemEntity) {
//        return new ItemDto(
//                itemEntity.getId(),
//                itemEntity.getName(),
//                itemEntity.getUser().getId(),
//                itemEntity.getDone(),
//                itemEntity.getCreatedAt());
//    }
}
