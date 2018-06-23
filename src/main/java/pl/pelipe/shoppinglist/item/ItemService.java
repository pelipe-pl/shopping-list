package pl.pelipe.shoppinglist.item;

import org.springframework.stereotype.Service;
import pl.pelipe.shoppinglist.user.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;

    public ItemService(ItemRepository itemRepository, UserService userService) {
        this.itemRepository = itemRepository;
        this.userService = userService;
    }

    public void add(ItemDto itemDto) {
        itemRepository.save(toEntity(itemDto));
    }

    public List<ItemDto> findAll(){return itemRepository.findAll()
            .stream()
            .map(i -> toDto(i))
            .collect(Collectors.toList());}

    public List<ItemDto> findAllByUsername(String username) {
        return itemRepository.findAllByUser_UsernameOrderByDone(username)
                .stream()
                .map(i -> toDto(i))
                .collect(Collectors.toList());
    }

    public void setDone(Long id, Boolean done) {
        ItemEntity itemEntity = itemRepository.getById(id);
        itemEntity.setDone(done);
        itemRepository.save(itemEntity);
    }

    private ItemEntity toEntity(ItemDto itemDto) {
        return new ItemEntity(
                itemDto.getId(),
                itemDto.getName(),
                userService.findById(itemDto.getUserId()),
                itemDto.getDone(),
                itemDto.getCreatedAt());
    }

    private ItemDto toDto(ItemEntity itemEntity) {
        return new ItemDto(
                itemEntity.getId(),
                itemEntity.getName(),
                itemEntity.getUser().getId(),
                itemEntity.getDone(),
                itemEntity.getCreatedAt());
    }
}
