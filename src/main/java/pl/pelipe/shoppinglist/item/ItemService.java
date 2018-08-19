package pl.pelipe.shoppinglist.item;

import org.springframework.stereotype.Service;
import pl.pelipe.shoppinglist.email.EmailService;
import pl.pelipe.shoppinglist.user.UserService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;
    private final ItemListService itemListService;
    private final EmailService emailService;

    public ItemService(ItemRepository itemRepository, UserService userService, ItemListService itemListService, EmailService emailService) {
        this.itemRepository = itemRepository;
        this.userService = userService;
        this.itemListService = itemListService;
        this.emailService = emailService;
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

    List<ItemDto> findAllByUsernameAndListId(String username, Long listId) {
        return itemRepository.findAllByUser_UsernameAndRemovedFalseAndList_IdOrderByCreatedAtDesc(username, listId)
                .stream()
                .map(i -> toDto(i))
                .collect(Collectors.toList());
    }

    void findDoneAndSetRemoved(Long listId, String username) {
        List<ItemEntity> items =
                itemRepository.findAllByUser_UsernameAndList_IdAndRemovedFalseAndDoneTrue(username, listId);
        items.forEach(i -> i.setRemoved(true));
        itemRepository.saveAll(items);
    }

    void findAllNotRemovedAndSetDone(Long listId, String username) {
        List<ItemEntity> items =
                itemRepository.findAllByUser_UsernameAndList_IdAndRemovedFalseAndDoneFalse(username, listId);
        items.forEach(i -> i.setDone(true));
        itemRepository.saveAll(items);
    }

    void setDone(Long id, Boolean done, String username) {
        ItemEntity itemEntity = itemRepository.getByIdAndUserUsername(id, username);
        itemEntity.setDone(done);
        itemRepository.save(itemEntity);
    }

    void setRemoved(Long id, String username) {
        ItemEntity itemEntity = itemRepository.getByIdAndUserUsername(id, username);
        itemEntity.setRemoved(true);
        itemRepository.save(itemEntity);
    }

    void rename(Long id, String name, String username) {
        ItemEntity entity = itemRepository.getByIdAndUserUsername(id, username);
        if (entity != null) {
            entity.setName(name);
            itemRepository.save(entity);
        } else throw new IllegalArgumentException("The item does not exist.");
    }

    public Boolean emailItemList(Long listId, String username) {
        ItemListDto itemList = itemListService.getByIdAndUsername(listId, username);
        List<ItemEntity> items = itemRepository.findAllByUser_UsernameAndRemovedFalseAndList_IdOrderByCreatedAtDesc(username, listId);

        if (itemList == null || itemList.getRemoved()) return false;
        if (items == null || items.isEmpty()) return false;

        StringBuilder stringList = new StringBuilder();
        stringList.append("<STRONG>").append(itemList.getName().toUpperCase()).append("</STRONG><br>");

        for (ItemEntity item : items) {
            stringList.append(item.getName()).append("<BR>");
        }
        try {
            emailService.send(username, "Shopping list: " + itemList.getName().toUpperCase(), stringList.toString());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
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