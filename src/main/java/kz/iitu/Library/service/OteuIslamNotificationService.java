package kz.iitu.Library.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class OteuIslamNotificationService {

    private static final Logger log = LoggerFactory.getLogger(OteuIslamNotificationService.class);

    @Async("taskExecutor")
    public CompletableFuture<Void> sendBorrowNotification(String email, String bookTitle) {
        log.info("[ASYNC] Sending borrow notification to {} for book '{}'", email, bookTitle);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("[ASYNC] Borrow notification sent to {}", email);
        return CompletableFuture.completedFuture(null);
    }

    @Async("taskExecutor")
    public CompletableFuture<Void> sendReturnNotification(String email, String bookTitle) {
        log.info("[ASYNC] Sending return notification to {} for book '{}'", email, bookTitle);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("[ASYNC] Return notification sent to {}", email);
        return CompletableFuture.completedFuture(null);
    }

    @Async("taskExecutor")
    public CompletableFuture<Long> calculateBorrowStats(Long userId) {
        log.info("[ASYNC] Calculating borrow stats for user {}", userId);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("[ASYNC] Stats calculated for user {}", userId);
        return CompletableFuture.completedFuture(userId);
    }
}