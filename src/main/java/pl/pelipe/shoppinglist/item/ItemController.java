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
    public String removeList(@PathVariable Long listId, Principal principal) {
        itemListService.remove(listId, principal.getName());
        return "redirect:/lists";
    }

    @RequestMapping(value = "list/rename", method = RequestMethod.POST)
    public String renameList(ItemListDto itemListDto, Principal principal) {
        itemListService.rename(itemListDto.getId(), itemListDto.getName(), principal.getName());
        return "redirect:/lists";
    }

    @RequestMapping(value = "/item/done", method = RequestMethod.POST)
    public String setDone(ItemDto item, Principal principal) {
        itemService.setDone(item.getId(), true, principal.getName());
        return "redirect:/list" + "/" + item.getListId();
    }

    @RequestMapping(value = "/item/add", method = RequestMethod.POST)
    public String addItem(ItemDto item, Principal principal) {
        item.setUserId(userService.findByUsername(principal.getName()).getId());
        itemService.add(item);
        return "redirect:/list" + "/" + item.getListId();
    }

    @RequestMapping(value = "/item/notdone", method = RequestMethod.POST)
    public String setNotDone(ItemDto item, Principal principal) {
        itemService.setDone(item.getId(), false, principal.getName());
        return "redirect:/list" + "/" + item.getListId();
    }

    @RequestMapping(value = "/item/rename", method = RequestMethod.POST)
    public String renameItem(ItemDto item, Principal principal){
        itemService.rename(item.getId(), item.getName(), principal.getName());
        return "redirect:/list" + "/" + item.getListId();
    }

    @RequestMapping(value = "/item/remove", method = RequestMethod.POST)
    public String setRemoved(ItemDto item, Principal principal) {
        itemService.setRemoved(item.getId(), principal.getName());
        return "redirect:/list" + "/" + item.getListId();
    }

    @RequestMapping(value = "/list/removedone/{listId}", method = RequestMethod.GET)
    public String setDoneItemsRemoved(@PathVariable Long listId, Principal principal) {
        itemService.findDoneAndSetRemoved(listId, principal.getName());
        return "redirect:/list/" + listId;
    }

    @RequestMapping(value = "/list/setalldone/{listId}", method = RequestMethod.GET)
    public String setAllItemsDone(@PathVariable Long listId, Principal principal) {
        itemService.findAllNotRemovedAndSetDone(listId, principal.getName());
        return "redirect:/list/" + listId;
    }
}