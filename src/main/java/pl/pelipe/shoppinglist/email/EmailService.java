package pl.pelipe.shoppinglist.email;

import com.sendgrid.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {

    private final Environment environment;

    @Autowired
    public EmailService(Environment environment) {
        this.environment = environment;
    }

    public void send(String to, String subject, String content) throws IOException {
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
}
