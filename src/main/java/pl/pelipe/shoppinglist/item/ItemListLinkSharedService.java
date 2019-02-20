package pl.pelipe.shoppinglist.item;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import pl.pelipe.shoppinglist.email.EmailService;

import javax.mail.internet.InternetAddress;
import java.time.LocalDateTime;

@Service
public class ItemListLinkSharedService {

    private final ItemListLinkSharedRepository itemListLinkSharedRepository;
    private final ItemListRepository itemListRepository;
    private final EmailService emailService;

    public ItemListLinkSharedService(ItemListLinkSharedRepository itemListLinkSharedRepository, ItemListRepository itemListRepository, EmailService emailService) {
        this.itemListLinkSharedRepository = itemListLinkSharedRepository;
        this.itemListRepository = itemListRepository;
        this.emailService = emailService;
    }

    public Boolean share(Long listId, String listOwnerUsername, String email) {

        ItemListEntity itemListEntity = itemListRepository.getByIdAndUser_Username(listId, listOwnerUsername);
        if (itemListEntity == null) return false;
        else if (!itemListEntity.getUser().getUsername().equals(listOwnerUsername)) return false;
        else if (email == null) return false;
        else if (!validateEmailAddress(email)) return false;
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

    private boolean validateEmailAddress(String email) {
        boolean isValid = false;
        try {
            InternetAddress internetAddress = new InternetAddress(email);
            internetAddress.validate();
            isValid = true;
        } catch (javax.mail.internet.AddressException e) {
            System.out.println("The given email adress is invalid [" + email + "]");
            e.printStackTrace();
        }
        return isValid;
    }
}