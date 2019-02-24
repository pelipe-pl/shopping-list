package pl.pelipe.shoppinglist.item;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.pelipe.shoppinglist.user.UserService;

import java.security.Principal;

@Controller
public class ItemListLinkSharedController {

    private final ItemListLinkSharedService itemListLinkSharedService;
    private final ItemService itemService;
    private final UserService userService;

    public ItemListLinkSharedController(ItemListLinkSharedService itemListLinkSharedService, ItemService itemService, UserService userService) {
        this.itemListLinkSharedService = itemListLinkSharedService;
        this.itemService = itemService;
        this.userService = userService;
    }

    @RequestMapping(value = "/list/share/token/{listId}", method = RequestMethod.POST)
    public String shareByLink(@PathVariable Long listId, @RequestParam("email") String email, Principal principal, RedirectAttributes redirectAttributes) {

        Boolean result = itemListLinkSharedService.share(listId, principal.getName(), email);
        if (result) redirectAttributes.addFlashAttribute("message", "The link to the list has been sent to " + email);
        else redirectAttributes.addFlashAttribute("error", "Oops! Something went wrong :(");
        return "redirect:/lists";
    }

    @RequestMapping(value = "/list/share/token/{token}", method = RequestMethod.GET)
    public String getByLink(Model model, @PathVariable String token, RedirectAttributes redirectAttributes) {

        ItemListDto list = itemListLinkSharedService.get(token);
        if (list != null) {
            model.addAttribute("list", list);
            model.addAttribute("items", itemService.findAllByUserIdAndListId(list.getUserId(), list.getId()));
            model.addAttribute("listOwner", userService.getNameById(list.getUserId()));
            return "sharedbylinklist";
        } else {
            redirectAttributes.addFlashAttribute("error", "Oops! The link is not valid.");
            return "redirect:/";
        }
    }
}