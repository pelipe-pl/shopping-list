package pl.pelipe.shoppinglist.email;

import com.sendgrid.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import pl.pelipe.shoppinglist.item.ItemEntity;
import pl.pelipe.shoppinglist.item.ItemListDto;
import pl.pelipe.shoppinglist.item.ItemListEntity;
import pl.pelipe.shoppinglist.user.UserEntity;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Service
public class EmailService {

    private final Environment environment;

    @Autowired
    public EmailService(Environment environment) {
        this.environment = environment;
    }

    public void send(String to, String subject, String content) {
        Email from = new Email(environment.getProperty("SENDGRID_FROM_EMAIL"));
        from.setName("Shopping List");
        Mail mail = new Mail(from, subject, new Email(to), new Content("text/html", content));
        SendGrid sendGrid = new SendGrid(environment.getProperty("SENDGRID_API_KEY"));
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
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

        send(to, "Shopping list: " + itemList.getName().toUpperCase(), stringList.toString());
        return true;
    }

    public void sendItemListShareConfirmation(UserEntity listSharer, ItemListEntity itemList) {
        String to = listSharer.getUsername();
        String listSharerName = listSharer.getName();
        String listOwnerName = itemList.getUser().getName();
        String listName = itemList.getName();
        String subject = listOwnerName + " just shared a shopping list with you!";
        String content = "Hello " + listSharerName + "! <BR><BR>" +
                "User " + listOwnerName + " shared a shopping list named " + listName.toUpperCase() + "." + "<BR>" +
                "You can log in to your Shopping List account and check it out. <BR><BR>" +
                "Thank you.";
        send(to, subject, content);
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
}

