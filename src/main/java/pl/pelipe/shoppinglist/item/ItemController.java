package pl.pelipe.shoppinglist.item;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.pelipe.shoppinglist.user.UserService;

import java.security.Principal;

@Controller
public class ItemController {

    private final ItemService itemService;
    private final UserService userService;

    public ItemController(ItemService itemService, UserService userService) {
        this.itemService = itemService;
        this.userService = userService;
    }

    @ModelAttribute
    public void addAttributes(Model model, Principal principal) {
        model.addAttribute("items", itemService.findAllByUsername(principal.getName()));
        model.addAttribute("item", new ItemDto());
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model) {
        return "list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addItem(Model model, ItemDto item, Principal principal) {
        item.setUserId(userService.findByUsername(principal.getName()).getId());
        itemService.add(item);
        model.addAttribute("items", itemService.findAllByUsername(principal.getName()));
        return "list";
    }

    @RequestMapping(value = "/done", method = RequestMethod.POST)
    public String setDone(Model model, ItemDto item, Principal principal) {
        itemService.setDone(item.getId(), true);
        model.addAttribute("items", itemService.findAllByUsername(principal.getName()));
        return "list";
    }

    @RequestMapping(value = "/notdone", method = RequestMethod.POST)
    public String setNotDone(Model model, ItemDto item, Principal principal) {
        itemService.setDone(item.getId(), false);
        model.addAttribute("items", itemService.findAllByUsername(principal.getName()));
        return "List";
    }
}
