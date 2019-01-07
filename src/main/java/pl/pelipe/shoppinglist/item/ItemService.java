package pl.pelipe.shoppinglist.item;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import pl.pelipe.shoppinglist.email.EmailService;
import pl.pelipe.shoppinglist.user.UserEntity;
import pl.pelipe.shoppinglist.user.UserService;

import java.security.Principal;
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
        addToRepository(itemDto);
    }

    void addShared(Principal principal, ItemDto itemDto) {
        UserEntity sharerUser = userService.findByUsername(principal.getName());
        ItemListEntity itemListEntity = itemListService.getById(itemDto.getListId());
        if (isSharerOfTheItemList(sharerUser.getUsername(), itemDto.getListId())) {
            itemDto.setUserId(itemListEntity.getUser().getId());
            addToRepository(itemDto);
        } else
            throw new AccessDeniedException(
                    "The user " + principal.getName() + " is not allowed to add item to list ID: " + itemDto.getListId());
    }

    List<ItemDto> findAllByUsernameAndListId(String username, Long listId) {
        return itemRepository.findAllByUser_UsernameAndRemovedFalseAndList_IdOrderByCreatedAtDesc(username, listId)
                .stream()
                .map(i -> toDto(i))
                .collect(Collectors.toList());
    }

    //TODO Add 2nd checking by principals username
    List<ItemDto> findAllBySharerUsernameAndListId(String username, Long listId) {
        return itemRepository.findAllByList_IdAndRemovedIsFalseOrderByCreatedAtDesc(listId)
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

    void findSharedDoneAndSetRemoved(Long listId, String sharerUsername) {
        if (isSharerOfTheItemList(sharerUsername, listId)) {
            List<ItemEntity> items =
                    itemRepository.findAllByListIdAndRemovedIsFalseAndDoneIsTrue(listId);
            items.forEach(i -> i.setRemoved(true));
            itemRepository.saveAll(items);
        } else throw new AccessDeniedException(
                "The user " + sharerUsername + " is not allowed to remove items of list ID: " + listId);
    }

    void findAllNotRemovedAndSetDone(Long listId, String username) {
        List<ItemEntity> items =
                itemRepository.findAllByUser_UsernameAndList_IdAndRemovedFalseAndDoneFalse(username, listId);
        items.forEach(i -> i.setDone(true));
        itemRepository.saveAll(items);
    }

    void findAllSharedNotRemovedAndSetDone(Long listId, String sharerUsername) {
        if (isSharerOfTheItemList(sharerUsername, listId)) {
            List<ItemEntity> items =
                    itemRepository.findAllByList_IdAndRemovedIsFalseOrderByCreatedAtDesc(listId);
            items.forEach(i -> i.setDone(true));
            itemRepository.saveAll(items);
        } else throw new AccessDeniedException(
                "The user " + sharerUsername + " is not allowed to change done status for items of list ID: " + listId);
    }

    void setDone(Long id, Boolean done, String username) {
        ItemEntity itemEntity = itemRepository.getByIdAndUserUsername(id, username);
        itemEntity.setDone(done);
        itemRepository.save(itemEntity);
    }

    void setDoneBySharer(Long itemListId, Long itemId, Boolean done, String sharerUsername) {
        ItemEntity itemEntity = itemRepository.getById(itemId);
        if (isSharerOfTheItemList(sharerUsername, itemListId) && itemEntity.getList().getId().equals(itemListId)) {
            itemEntity.setDone(done);
            itemRepository.save(itemEntity);
        } else
            throw new AccessDeniedException(
                    "The user " + sharerUsername + " is not allowed to set done status of this item");
    }

    void setRemoved(Long id, String username) {
        ItemEntity itemEntity = itemRepository.getByIdAndUserUsername(id, username);
        itemEntity.setRemoved(true);
        itemRepository.save(itemEntity);
    }

    void setRemovedBySharer(Long itemId, Long itemListId, String sharerUsername) {
        ItemEntity itemEntity = itemRepository.getById(itemId);
        if (isSharerOfTheItemList(sharerUsername, itemListId) && itemEntity.getList().getId().equals(itemListId)) {
            itemEntity.setRemoved(true);
            itemRepository.save(itemEntity);
        } else
            throw new AccessDeniedException(
                    "The user " + sharerUsername + " is not allowed to remove this item");
    }

    void rename(Long id, String name, String username) {
        ItemEntity entity = itemRepository.getByIdAndUserUsername(id, username);
        if (entity != null) {
            entity.setName(name);
            itemRepository.save(entity);
        } else throw new IllegalArgumentException("The item does not exist.");
    }

    private void addToRepository(ItemDto itemDto) {
        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setName(itemDto.getName());
        itemEntity.setUser(userService.findById(itemDto.getUserId()));
        itemEntity.setList(itemListService.getById(itemDto.getListId()));
        itemEntity.setDone(false);
        itemEntity.setRemoved(false);
        itemRepository.save(itemEntity);
    }

    private Boolean isSharerOfTheItemList(String sharerUsername, Long listId) {
        UserEntity sharerUser = userService.findByUsername(sharerUsername);
        ItemListEntity itemListEntity = itemListService.getById(listId);
        return itemListEntity.getSharedWithUsers().contains(sharerUser);
    }

    Boolean emailItemList(Long listId, String username) {
        ItemListDto itemList = itemListService.getByIdAndUsername(listId, username);
        List<ItemEntity> items = itemRepository.findAllByUser_UsernameAndRemovedFalseAndList_IdOrderByCreatedAtDesc(username, listId);
        return sendListByEmail(username, itemList, items);
    }

    Boolean emailSharedItemList(Long listId, String username) {
        ItemListDto itemList = itemListService.getByIdAndSharerUsername(listId, username);
        List<ItemEntity> items = itemRepository.findAllByList_IdAndRemovedIsFalseOrderByCreatedAtDesc(listId);
        return sendListByEmail(username, itemList, items);
    }

    private Boolean sendListByEmail(String username, ItemListDto itemList, List<ItemEntity> items) {
        if (itemList == null || itemList.getRemoved()) return false;
        if (items == null || items.isEmpty()) return false;
        return emailService.sendItemList(username, itemList, items);
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