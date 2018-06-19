package pl.pelipe.shoppinglist.item;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

@Controller
public class ItemController {

    final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String login(Model model, Principal principal){
        model.addAttribute("items", itemService.findAllByUsername(principal.getName()));
        return "list";

    }


}
