package pl.pelipe.shoppinglist.utils.dbclean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pelipe.shoppinglist.item.ItemListLinkSharedRepository;
import pl.pelipe.shoppinglist.item.ItemListRepository;
import pl.pelipe.shoppinglist.item.ItemRepository;
import pl.pelipe.shoppinglist.user.PasswordResetTokenRepository;
import pl.pelipe.shoppinglist.utils.email.EmailService;

import java.time.LocalDateTime;

@Service
@Transactional
public class DbCleanService {

    private final static String ADMIN_EMAIL_SUBJECT_DBCLEAN_SUCCESS = "System message: DbCleanService task succeeded";
    private final static String ADMIN_EMAIL_SUBJECT_DBCLEAN_FAIL = "System message: DbCleanService failed";
    private final ItemListRepository itemListRepository;
    private final ItemRepository itemRepository;
    private final ItemListLinkSharedRepository itemListLinkSharedRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;
    private final Logger logger = LoggerFactory.getLogger(DbCleanService.class);
    @Value("#{environment.ENVIRONMENT_TAG}")
    private String environmentTag;

    public DbCleanService(ItemListRepository itemListRepository, ItemRepository itemRepository,
                          ItemListLinkSharedRepository itemListLinkSharedRepository,
                          PasswordResetTokenRepository passwordResetTokenRepository, EmailService emailService) {
        this.itemListRepository = itemListRepository;
        this.itemRepository = itemRepository;
        this.itemListLinkSharedRepository = itemListLinkSharedRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.emailService = emailService;
    }

    @Scheduled(cron = "00 00 20 * * *")
    public void cleanObsoleteDbRecords() {
        try {
            long startTime = System.currentTimeMillis();
            logger.info("Starting cleanObsoleteDbRecords scheduled task.");
            Integer cleanedObsoleteItems = cleanObsoleteItems();
            Integer cleanedObsoleteItemLists = cleanObsoleteItemLists();
            Integer cleanedObsoletePasswordResetTokens = cleanObsoletePasswordResetTokens();
            Integer cleanedTotal =
                    cleanedObsoleteItemLists + cleanedObsoleteItems + cleanedObsoletePasswordResetTokens;
            long elapsedTime = System.currentTimeMillis() - startTime;
            logger.info("Finished cleanObsoleteDbRecords scheduled task.");
            logger.info("Total deleted records: " + cleanedTotal);
            logger.info("Total execution time: " + elapsedTime + " milliseconds");

            emailService.sendToAdmin(
                    environmentTag + ": " + ADMIN_EMAIL_SUBJECT_DBCLEAN_SUCCESS,
                    "Report date: " + LocalDateTime.now().withNano(0) + "<br>" +
                            "cleanedObsoleteItems: " + cleanedObsoleteItems + "<br>" +
                            "cleanedObsoleteItemLists: " + cleanedObsoleteItemLists + "<br>" +
                            "cleanedObsoletePasswordResetTokens: " + cleanedObsoletePasswordResetTokens + "<br>" +
                            "cleanedTotal: " + cleanedTotal + "<br>" +
                            "elapsedTime: " + elapsedTime + " milliseconds");
        } catch (Exception e) {
            logger.error("DbCleanService task failed.", e);
            emailService.sendExceptionNotifyToAdmin(environmentTag + ": " + ADMIN_EMAIL_SUBJECT_DBCLEAN_FAIL, e);
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

    private Integer cleanObsoletePasswordResetTokens() {
        logger.info("PasswordResetToken records: starting cleaning...");
        Integer deletedRecords = passwordResetTokenRepository.deleteAllByExpiryDateBefore(LocalDateTime.now());
        logger.info("PasswordResetToken records: finished cleaning...");
        logger.info("PasswordResetToken records: " + deletedRecords + " rows.");
        return deletedRecords;
    }
}