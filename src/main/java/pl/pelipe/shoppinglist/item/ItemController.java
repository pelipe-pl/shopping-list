package pl.pelipe.shoppinglist.item;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.pelipe.shoppinglist.user.UserEntity;
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
        model.addAttribute("sharedLists", itemListService.findAllShared(principal.getName()));
        model.addAttribute("item", new ItemDto());
        model.addAttribute("user", new UserEntity());
    }

    @RequestMapping(value = "/lists", method = RequestMethod.GET)
    public String lists() {
        return "lists";
    }

    @RequestMapping(value = "/lists/shared", method = RequestMethod.GET)
    public String sharedList() {
        return "sharedlists";
    }

    @RequestMapping(value = "/list/{itemListId}", method = RequestMethod.GET)
    public String list(@PathVariable Long itemListId, Principal principal, Model model) {
        ItemListDto list = itemListService.getByIdAndUsername(itemListId, principal.getName());
        model.addAttribute("list", list);
        model.addAttribute("items", itemService.findAllByUsernameAndListId(principal.getName(), itemListId));
        return "list";
    }

    @RequestMapping(value = "/list/shared/{itemListId}", method = RequestMethod.GET)
    public String sharedList(@PathVariable Long itemListId, Principal principal, Model model) {
        ItemListDto list = itemListService.getByIdAndSharerUsername(itemListId, principal.getName());
        model.addAttribute("list", list);
        model.addAttribute("items", itemService.findAllBySharerUsernameAndListId(principal.getName(), itemListId));
        model.addAttribute("listOwner", userService.getUsernameById(list.getUserId()));
        return "sharedlist";
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

    @RequestMapping(value = "list/email/{itemListId}", method = RequestMethod.GET)
    public String sendListOnEmail(Model model, @PathVariable Long itemListId, Principal principal) {
        Boolean result = itemService.emailItemList(itemListId, principal.getName());
        if (result) model.addAttribute("message", "The list has been sent to " + principal.getName() + ".");
        else model.addAttribute("error", "Oops! Something went wrong :(");
        return "lists";
    }

    @RequestMapping(value = "list/shared/email/{itemListId}", method = RequestMethod.GET)
    public String sendSharedListOnEmail(Model model, @PathVariable Long itemListId, Principal principal) {
        Boolean result = itemService.emailSharedItemList(itemListId, principal.getName());
        if (result) model.addAttribute("message", "The list has been sent to " + principal.getName() + ".");
        else model.addAttribute("error", "Oops! Something went wrong :(");
        return "sharedlists";
    }

    @RequestMapping(value = "/item/add", method = RequestMethod.POST)
    public String addItem(ItemDto item, Principal principal) {
        item.setUserId(userService.findByUsername(principal.getName()).getId());
        itemService.add(item);
        return "redirect:/list" + "/" + item.getListId();
    }

    @RequestMapping(value = "/list/shared/item/add", method = RequestMethod.POST)
    public String addItemToSharedList(ItemDto item, Principal principal) {

        itemService.addShared(principal, item);
        return "redirect:/list/shared" + "/" + item.getListId();
    }

    @RequestMapping(value = "/item/done", method = RequestMethod.POST)
    public String setDone(ItemDto item, Principal principal) {
        itemService.setDone(item.getId(), true, principal.getName());
        return "redirect:/list" + "/" + item.getListId();
    }

    @RequestMapping(value = "/list/shared/item/done", method = RequestMethod.POST)
    public String setSharedItemDone(ItemDto item, Principal principal) {
        itemService.setDoneBySharer(item.getListId(), item.getId(), true, principal.getName());
        return "redirect:/list/shared" + "/" + item.getListId();
    }

    @RequestMapping(value = "/item/notdone", method = RequestMethod.POST)
    public String setNotDone(ItemDto item, Principal principal) {
        itemService.setDone(item.getId(), false, principal.getName());
        return "redirect:/list" + "/" + item.getListId();
    }

    @RequestMapping(value = "/list/shared/item/notdone", method = RequestMethod.POST)
    public String setSharedItemNotDone(ItemDto item, Principal principal) {
        itemService.setDoneBySharer(item.getListId(), item.getId(), false, principal.getName());
        return "redirect:/list/shared" + "/" + item.getListId();
    }

    @RequestMapping(value = "/item/rename", method = RequestMethod.POST)
    public String renameItem(ItemDto item, Principal principal) {
        itemService.rename(item.getId(), item.getName(), principal.getName());
        return "redirect:/list" + "/" + item.getListId();
    }

    @RequestMapping(value = "/list/shared/item/rename", method = RequestMethod.POST)
    public String renameSharedItem(ItemDto item, Principal principal) {
        itemService.renameShared(item.getId(), item.getName(), principal.getName());
        return "redirect:/list/shared" + "/" + item.getListId();
    }

    @RequestMapping(value = "/item/remove", method = RequestMethod.POST)
    public String setRemoved(ItemDto item, Principal principal) {
        itemService.setRemoved(item.getId(), principal.getName());
        return "redirect:/list" + "/" + item.getListId();
    }

    @RequestMapping(value = "/list/shared/item/remove", method = RequestMethod.POST)
    public String setSharedItemRemoved(ItemDto item, Principal principal) {
        itemService.setRemovedBySharer(item.getId(), item.getListId(), principal.getName());
        return "redirect:/list/shared" + "/" + item.getListId();
    }

    @RequestMapping(value = "/list/removedone/{listId}", method = RequestMethod.GET)
    public String setDoneItemsRemoved(@PathVariable Long listId, Principal principal) {
        itemService.findDoneAndSetRemoved(listId, principal.getName());
        return "redirect:/list/" + listId;
    }

    @RequestMapping(value = "/list/shared/removedone/{listId}", method = RequestMethod.GET)
    public String setDoneSharedItemsRemoved(@PathVariable Long listId, Principal principal) {
        itemService.findSharedDoneAndSetRemoved(listId, principal.getName());
        return "redirect:/list/shared" + "/" + listId;
    }

    @RequestMapping(value = "/list/setalldone/{listId}", method = RequestMethod.GET)
    public String setAllItemsDone(@PathVariable Long listId, Principal principal) {
        itemService.findAllNotRemovedAndSetDone(listId, principal.getName());
        return "redirect:/list/" + listId;
    }

    @RequestMapping(value = "/list/shared/setalldone/{listId}", method = RequestMethod.GET)
    public String setAllSharedItemsDone(@PathVariable Long listId, Principal principal) {
        itemService.findAllSharedNotRemovedAndSetDone(listId, principal.getName());
        return "redirect:/list/shared" + "/" + listId;
    }

    @RequestMapping(value = "/list/share/{listId}", method = RequestMethod.POST)
    public String shareItemList(@PathVariable Long listId, UserEntity listSharer, Principal principal,
                                RedirectAttributes redirectAttributes) {
        if (listSharer == null || listSharer.getUsername() == null) {
            redirectAttributes.addFlashAttribute("error", "The sharer username has not been provided.");
            return "redirect:/lists";
        } else {
            int result = itemListService.share(listId, principal.getName(), listSharer.getUsername());
            if (result == 0) {
                redirectAttributes.addFlashAttribute("error", "You cannot share the list with yourself");
                return "redirect:/lists";
            }
            if (result == 1) {
                redirectAttributes.addFlashAttribute("error", "The user " + listSharer.getUsername() + " not found");
                return "redirect:/lists";
            }
            if (result == 2) {
                redirectAttributes.addFlashAttribute("error", "You already share this list with " + listSharer.getUsername());
                return "redirect:/lists";
            }
            if (result == 3) {
                redirectAttributes.addFlashAttribute("message", "The list has been successfully shared with " + listSharer.getUsername());
                return "redirect:/lists";
            }
        }
        return "redirect:lists";
    }

    @RequestMapping(value = "/list/stopsharing/{listId}", method = RequestMethod.GET)
    public String stopSharingList(Principal principal, @PathVariable Long listId){
        itemListService.stopSharingList(listId, principal.getName());
        return "redirect:/list" + "/" + listId;
    }

    @RequestMapping(value = "/list/shared/stopwatching/{listId}", method = RequestMethod.POST)
    public String stopWatchingList(Model model, Principal principal, @PathVariable Long listId) {
        String result = itemListService.stopWatchingList(listId, principal.getName());
        model.addAttribute("message", result);
        return "redirect:/lists/shared";
    }
}