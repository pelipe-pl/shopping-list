package pl.pelipe.shoppinglist.item;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.pelipe.shoppinglist.user.UserService;

import java.security.Principal;

@Controller
public class ItemController {

    private final ItemService itemService;
    private final ItemListService itemListService;
    private final UserService userService;

    public ItemController(ItemService itemService, UserService userService, ItemListService itemListService) {
        this.itemService = itemService;
        this.userService = userService;
        this.itemListService = itemListService;
    }

    @ModelAttribute
    public void addAttributes(Model model, Principal principal) {
        model.addAttribute("lists", itemListService.findAllByUsernameAndRemovedFalse(principal.getName()));
        model.addAttribute("item", new ItemDto());
    }

    @RequestMapping(value = "/lists", method = RequestMethod.GET)
    public String lists() {
        return "lists";
    }

    @RequestMapping(value = "/list/{itemListId}", method = RequestMethod.GET)
    public String list(@PathVariable Long itemListId, Principal principal, Model model) {
        ItemListDto list = itemListService.getByIdAndUsername(itemListId, principal.getName());
        model.addAttribute("list", list);
        model.addAttribute("items", itemService.findAllByUsernameAndListId(principal.getName(), itemListId));
        return "list";
    }

    @RequestMapping(value = "/list/add", method = RequestMethod.POST)
    public String addList(ItemListDto itemListDto, Principal principal) {
        itemListDto.setUserId(userService.findByUsername(principal.getName()).getId());
        itemListService.add(itemListDto);
        return "redirect:/lists";
    }

    @RequestMapping(value = "/list/remove/{listId}", method = RequestMethod.POST)
    public String removeList(@PathVariable Long listId, Principal principal){
        itemListService.remove(listId, principal.getName());
        return "redirect:/lists";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addItem(ItemDto item, Principal principal) {
        item.setUserId(userService.findByUsername(principal.getName()).getId());
        itemService.add(item);
        return "redirect:list" + "/" + item.getListId();
    }

    @RequestMapping(value = "/done", method = RequestMethod.POST)
    public String setDone(ItemDto item) {
        itemService.setDone(item.getId(), true);
        return "redirect:list" + "/" + item.getListId();
    }

    @RequestMapping(value = "/notdone", method = RequestMethod.POST)
    public String setNotDone(ItemDto item) {
        itemService.setDone(item.getId(), false);
        return "redirect:list" + "/" + item.getListId();
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public String setRemoved(ItemDto item) {
        itemService.setRemoved(item.getId());
        return "redirect:list" + "/" + item.getListId();
    }

    @RequestMapping(value = "/removedone", method = RequestMethod.GET)
    public String setDoneItemsRemoved(Principal principal) {
        itemService.findDoneAndSetRemoved(principal.getName());
        return "redirect:list";
    }

    @RequestMapping(value = "/setalldone", method = RequestMethod.GET)
    public String setAllItemsDone(Principal principal) {
        itemService.findAllNotRemovedAndSetDone(principal.getName());
        return "redirect:list";
    }
}