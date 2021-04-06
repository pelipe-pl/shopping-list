package pl.pelipe.shoppinglist.utils.dbclean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class DbCleanController {

    private final DbCleanService dbCleanService;
    private final Logger logger = LoggerFactory.getLogger(DbCleanController.class);

    public DbCleanController(DbCleanService dbCleanService) {
        this.dbCleanService = dbCleanService;
    }

    @RequestMapping(value = "/dbclean", method = RequestMethod.GET)
    private String triggerDbClean() {
        logger.info("DbClean has been triggered with url.");
        return dbCleanService.cleanObsoleteDbRecords();
    }
}