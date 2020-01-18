package pl.pelipe.shoppinglist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import pl.pelipe.shoppinglist.utils.dbclean.DbCleanService;

@SpringBootApplication
public class ShoppingListApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ShoppingListApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(ShoppingListApplication.class, args);
    }
}