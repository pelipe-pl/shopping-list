package pl.pelipe.shoppinglist.item;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import pl.pelipe.shoppinglist.utils.email.EmailService;

import java.time.LocalDateTime;

@Service
public class ItemListLinkSharedService {

    private final ItemListLinkSharedRepository itemListLinkSharedRepository;
    private final ItemListRepository itemListRepository;
    private final EmailService emailService;
    private final ItemListService itemListService;

    public ItemListLinkSharedService(ItemListLinkSharedRepository itemListLinkSharedRepository, ItemListRepository itemListRepository, EmailService emailService, ItemListService itemListService) {
        this.itemListLinkSharedRepository = itemListLinkSharedRepository;
        this.itemListRepository = itemListRepository;
        this.emailService = emailService;
        this.itemListService = itemListService;
    }

    public Boolean share(Long listId, String listOwnerUsername, String email) {

        ItemListEntity itemListEntity = itemListRepository.getByIdAndUser_Username(listId, listOwnerUsername);
        if (itemListEntity == null) return false;
        else if (!itemListEntity.getUser().getUsername().equals(listOwnerUsername)) return false;
        else if (email == null) return false;
        else {
            String token = RandomStringUtils.randomAlphabetic(25);
            ItemListLinkSharedEntity listLinkShared = new ItemListLinkSharedEntity();
            listLinkShared.setListEntity(itemListEntity);
            listLinkShared.setEmailAddress(email);
            listLinkShared.setExpiryDate(LocalDateTime.now().plusDays(90));
            listLinkShared.setActive(true);
            listLinkShared.setToken(token);
            itemListLinkSharedRepository.save(listLinkShared);
            return emailService.sendItemListLinkShared(listLinkShared);
        }
    }

    public ItemListDto get(String token) {

        ItemListLinkSharedEntity itemListLinkSharedEntity =
                itemListLinkSharedRepository.getByTokenAndActiveTrueAndExpiryDateIsAfter(token, LocalDateTime.now());
        if (itemListLinkSharedEntity == null) return null;
        else {
            ItemListEntity itemListEntity = itemListLinkSharedEntity.getListEntity();
            if (!itemListEntity.getRemoved()) return itemListService.toDto(itemListEntity);
            else return null;
        }
    }
}