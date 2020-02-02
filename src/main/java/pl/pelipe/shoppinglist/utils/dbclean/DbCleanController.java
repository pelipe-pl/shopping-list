package pl.pelipe.shoppinglist.utils.dbclean;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class DbCleanController {

    private DbCleanService dbCleanService;

    public DbCleanController(DbCleanService dbCleanService) {
        this.dbCleanService = dbCleanService;
    }

    @RequestMapping(value = "/dbclean", method = RequestMethod.GET)
    private String triggerDbClean() {

        return dbCleanService.cleanObsoleteDbRecords();
    }
}