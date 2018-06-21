package pl.pelipe.shoppinglist.item;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.pelipe.shoppinglist.user.UserService;

import java.security.Principal;

@Controller
public class ItemController {

    final ItemService itemService;
    final UserService userService;

    public ItemController(ItemService itemService, UserService userService) {
        this.itemService = itemService;
        this.userService = userService;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model, Principal principal){
        model.addAttribute("items", itemService.findAllByUsername(principal.getName()));
        model.addAttribute("item", new ItemDto());
        return "list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addItem(Model model, ItemDto item, Principal principal){
        item.setUserId(userService.findByUsername(principal.getName()).getId());
        itemService.add(item);
        model.addAttribute("items", itemService.findAllByUsername(principal.getName()));
        return "list";
    }


}
