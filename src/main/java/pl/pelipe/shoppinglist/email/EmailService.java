package pl.pelipe.shoppinglist.email;

import com.sendgrid.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import pl.pelipe.shoppinglist.item.ItemEntity;
import pl.pelipe.shoppinglist.item.ItemListDto;
import pl.pelipe.shoppinglist.item.ItemListEntity;
import pl.pelipe.shoppinglist.item.ItemListLinkSharedEntity;
import pl.pelipe.shoppinglist.user.UserEntity;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import static pl.pelipe.shoppinglist.config.Keys.URL_ITEM_LIST_SHARED_LINK;

@Service
public class EmailService {

    private final static String MSG_ADMINS_EMAIL_ADDRESS_NOT_DEFINED = "Admins email address is not defined.";
    private final Environment environment;
    private final Logger logger = LoggerFactory.getLogger(EmailService.class);

    public EmailService(Environment environment) {
        this.environment = environment;
    }

    public Boolean send(String to, String subject, String content) {
        Boolean result = Boolean.FALSE;
        Email from = new Email(environment.getProperty("SENDGRID_FROM_EMAIL"));
        from.setName(environment.getProperty("EMAIL_SENDER_NAME"));
        Mail mail = new Mail(from, subject, new Email(to), new Content("text/html", content));
        SendGrid sendGrid = new SendGrid(environment.getProperty("SENDGRID_API_KEY"));
        Request request = new Request();
        try {
            logger.info("Sending e-mail to " + "****" + to.substring(4));
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);
            logger.info("Response status code: " + response.getStatusCode());
            logger.info("Response body: " + response.getBody());
            logger.info("Response headers: " + response.getHeaders());
            result = true;
        } catch (IOException ex) {
            logger.error(EmailService.class.getName() + " has failed to send email to: " + "****" + to.substring(4) + " , subject: " + subject, ex);
        }
        return result;
    }

    public Boolean sendItemList(String to, ItemListDto itemList, List<ItemEntity> items) {
        StringBuilder stringList = new StringBuilder();
        stringList.append("<STRONG>").append(itemList.getName().toUpperCase()).append("</STRONG><br>");
        for (
                ItemEntity item : items) {
            if (item.getDone()) {
                stringList.append("<DEL>").append(item.getName()).append("</DEL>").append("<BR>");
            } else
                stringList.append(item.getName()).append("<BR>");
        }

        return send(to, "Shopping list: " + itemList.getName().toUpperCase(), stringList.toString());
    }

    public Boolean sendItemListShareConfirmation(UserEntity listSharer, ItemListEntity itemList) {
        String to = listSharer.getUsername();
        String listSharerName = listSharer.getName();
        String listOwnerName = itemList.getUser().getName();
        String listName = itemList.getName();
        String subject = listOwnerName + " just shared a shopping list with you!";
        String content = "Hello " + listSharerName + "! <BR><BR>" +
                "User " + listOwnerName + " shared a shopping list named " + listName.toUpperCase() + "." + "<BR>" +
                "You can log in to your Shopping List account and check it out. <BR><BR>" +
                "Thank you.";
        return send(to, subject, content);
    }

    public void sendItemListStopSharingConfirmation(Set<UserEntity> sharedWithUsers, ItemListEntity itemList) {
        for (UserEntity listSharer : sharedWithUsers) {
            String to = listSharer.getUsername();
            String listSharerName = listSharer.getName();
            String listOwnerName = itemList.getUser().getName();
            String listName = itemList.getName();
            String subject = listOwnerName + " stopped sharing a shopping list with you!";
            String content = "Hello " + listSharerName + "! <BR><BR>" +
                    "User " + listOwnerName + " just stopped sharing his/her shopping list named " + listName.toUpperCase() + "." + "<BR>" +
                    "You will no longer be able to see it when you log in to your Shopping List account.<BR><BR>" +
                    "Thank you.";
            send(to, subject, content);
        }
    }

    public Boolean sendItemListLinkShared(ItemListLinkSharedEntity listLinkShared) {
        String to = listLinkShared.emailAddress;
        String listOwnerName = listLinkShared.getListEntity().getUser().getName();
        String listName = listLinkShared.getListEntity().getName();
        String subject = listOwnerName + " has shared a shopping list with you!";
        String content = "Hello! <BR><BR>" +
                "User " + listOwnerName + " has just shared his/her shopping list named " + listName.toUpperCase() + "." + "<BR>" +
                "<a href=" + '"'
                + environment.getRequiredProperty(URL_ITEM_LIST_SHARED_LINK)
                + listLinkShared.getToken()
                + '"' + ">Click this link to open it!</a>" +
                "<BR><BR>" +
                "Thank you.";
        return send(to, subject, content);
    }

    public void sendToAdmin(String subject, String content) {
        String to = environment.getProperty("ADMINS_EMAIL_ADDRESS");
        if (to == null) {
            logger.error(MSG_ADMINS_EMAIL_ADDRESS_NOT_DEFINED);
        }
        send(to, subject, content);
    }
}