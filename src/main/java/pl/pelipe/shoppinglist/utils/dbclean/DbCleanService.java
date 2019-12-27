package pl.pelipe.shoppinglist.utils.dbclean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pelipe.shoppinglist.email.EmailService;
import pl.pelipe.shoppinglist.item.ItemListLinkSharedRepository;
import pl.pelipe.shoppinglist.item.ItemListRepository;
import pl.pelipe.shoppinglist.item.ItemRepository;
import pl.pelipe.shoppinglist.user.PasswordResetTokenRepository;

import java.time.LocalDateTime;

@Service
@Transactional
public class DbCleanService {

    private final static String ADMIN_EMAIL_SUBJECT_SUCCESS = "System message: DbCleanService task succeeded";
    private final static String ADMIN_EMAIL_SUBJECT_FAIL = "System message: DbCleanService failed";

    private final ItemListRepository itemListRepository;
    private final ItemRepository itemRepository;
    private final ItemListLinkSharedRepository itemListLinkSharedRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;
    private final Logger logger = LoggerFactory.getLogger(DbCleanService.class);

    public DbCleanService(ItemListRepository itemListRepository, ItemRepository itemRepository, ItemListLinkSharedRepository itemListLinkSharedRepository, PasswordResetTokenRepository passwordResetTokenRepository, EmailService emailService) {
        this.itemListRepository = itemListRepository;
        this.itemRepository = itemRepository;
        this.itemListLinkSharedRepository = itemListLinkSharedRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.emailService = emailService;
    }

    @Scheduled(cron = "00 00 21 * * *")
    public void cleanObsoleteDbRecords() {
        try {
            long startTime = System.currentTimeMillis();
            logger.info("Starting cleanObsoleteDbRecords scheduled task.");
            Integer cleanedObsoleteItems = cleanObsoleteItems();
            Integer cleanedObsoleteItemLists = cleanObsoleteItemLists();
            Integer cleanedObsoleteListLinkShared = cleanObsoleteListLinkShared();
            Integer cleanedObsoletePasswordResetTokens = cleanObsoletePasswordResetTokens();
            Integer cleanedTotal =
                    cleanedObsoleteItemLists + cleanedObsoleteItems + cleanedObsoleteListLinkShared + cleanedObsoletePasswordResetTokens;
            long elapsedTime = System.currentTimeMillis() - startTime;
            logger.info("Finished cleanObsoleteDbRecords scheduled task.");
            logger.info("Total deleted records: " + cleanedTotal);
            logger.info("Total execution time: " + elapsedTime + " milliseconds");

            emailService.sendToAdmin(
                    ADMIN_EMAIL_SUBJECT_SUCCESS,
                    "cleanedObsoleteItems: " + cleanedObsoleteItems + "<br>" +
                            "cleanedObsoleteItemLists: " + cleanedObsoleteItemLists + "<br>" +
                            "cleanedObsoleteListLinkShared: " + cleanedObsoleteListLinkShared + "<br>" +
                            "cleanedObsoletePasswordResetTokens: " + cleanedObsoletePasswordResetTokens + "<br>" +
                            "cleanedTotal: " + cleanedTotal + "<br>" +
                            "elapsedTime: " + elapsedTime + " milliseconds");
        } catch (Exception e) {
            logger.error("DbCleanService task failed.", e);
            emailService.sendToAdmin(ADMIN_EMAIL_SUBJECT_FAIL, "DbCleanService task failed." + "<br>" + e.toString()
            );
        }
    }

    private Integer cleanObsoleteItems() {
        logger.info("Item records: starting cleaning...");
        Integer deletedRecords = itemRepository.deleteAllByRemovedIsTrue();
        logger.info("Item records: finished cleaning...");
        logger.info("Item records: deleted " + deletedRecords + " rows.");
        return deletedRecords;
    }

    private Integer cleanObsoleteItemLists() {
        logger.info("ItemList records: starting cleaning...");
        Integer deletedRecords = itemListRepository.deleteAllByRemovedIsTrue();
        logger.info("ItemList records: finished cleaning...");
        logger.info("ItemList records: deleted " + deletedRecords + " rows.");
        return deletedRecords;
    }

    private Integer cleanObsoleteListLinkShared() {
        logger.info("ListLinkShared records: starting cleaning...");
        Integer deletedRecords = itemListLinkSharedRepository.deleteAllByExpiryDateBefore(LocalDateTime.now());
        logger.info("ListLinkShared records: finished cleaning...");
        logger.info("ListLinkShared records: deleted " + deletedRecords + " rows.");
        return deletedRecords;
    }

    private Integer cleanObsoletePasswordResetTokens() {
        logger.info("PasswordResetToken records: starting cleaning...");
        Integer deletedRecords = passwordResetTokenRepository.deleteAllByExpiryDateBefore(LocalDateTime.now());
        logger.info("PasswordResetToken records: finished cleaning...");
        logger.info("PasswordResetToken records: " + deletedRecords + " rows.");
        return deletedRecords;
    }
}