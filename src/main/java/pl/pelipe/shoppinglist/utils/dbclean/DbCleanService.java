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

    @Scheduled(cron = "${dbclean.cron}")
    public void cleanObsoleteDbRecordsScheduled() {
        cleanObsoleteDbRecords();
    }

    public String cleanObsoleteDbRecords() {
        try {
            long startTime = System.currentTimeMillis();
            logger.info("Starting cleanObsoleteDbRecords task.");
            Integer cleanedObsoleteItemLists = cleanObsoleteItemLists();
            Integer cleanedObsoleteItems = cleanObsoleteItems();
            Integer cleanedObsoletePasswordResetTokens = cleanObsoletePasswordResetTokens();
            Integer cleanedItemListLinkShared = cleanItemListLinkShared();
            Integer cleanedTotal = cleanedObsoleteItemLists + cleanedObsoleteItems + cleanedObsoletePasswordResetTokens + cleanedItemListLinkShared;
            long elapsedTime = System.currentTimeMillis() - startTime;
            logger.info("Finished cleanObsoleteDbRecords task.");
            logger.info("Total deleted records: " + cleanedTotal);
            logger.info("Total execution time: " + elapsedTime + " milliseconds");

            StringBuilder report = new StringBuilder();
            report.append("Report date: ").append(LocalDateTime.now().withNano(0)).append("<br>");
            report.append("cleanedObsoleteItemLists: ").append(cleanedObsoleteItemLists).append("<br>");
            report.append("cleanedObsoleteItems: ").append(cleanedObsoleteItems).append("<br>");
            report.append("cleanedObsoletePasswordResetTokens: ").append(cleanedObsoletePasswordResetTokens).append("<br>");
            report.append("cleanedItemListLinkShared: ").append(cleanedItemListLinkShared).append("<br>");
            report.append("cleanedTotal: ").append(cleanedTotal).append("<br>");
            report.append("elapsedTime: ").append(elapsedTime).append(" milliseconds");

//            emailService.sendToAdmin(
//                    environmentTag + ": " + ADMIN_EMAIL_SUBJECT_DBCLEAN_SUCCESS, report.toString());

            return report.toString();

        } catch (Exception e) {
            logger.error("DbCleanService task failed.", e);
            emailService.sendExceptionNotifyToAdmin(environmentTag + ": " + ADMIN_EMAIL_SUBJECT_DBCLEAN_FAIL, e);
            return e.getMessage();
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
        logger.info("PasswordResetToken records deleted: " + deletedRecords + " rows.");
        return deletedRecords;
    }

    private Integer cleanItemListLinkShared() {
        logger.info("ItemListLinkShared records: starting cleaning...");
        Integer deletedRecords = itemListLinkSharedRepository.deleteAllByExpiryDateBefore(LocalDateTime.now());
        logger.info("ItemListLinkShared records: finished cleaning...");
        logger.info("ItemListLinkShared records: " + deletedRecords + " rows.");
        return deletedRecords;
    }
}