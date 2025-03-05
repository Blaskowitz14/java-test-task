package ru.blaskowitz.java.test.task.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.blaskowitz.java.test.task.service.AccountService;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateBalanceScheduler {

    private final AccountService accountService;

    @Scheduled(fixedRate = 30000)
    @SchedulerLock(name = "updateBalanceTask", lockAtLeastFor = "29s")
    public void performBalanceUpdate() {
        log.info("Starting updateBalancesTask");
        try {
            accountService.updateBalances();
            log.info("updateBalancesTask completed successfully");
        } catch (Exception e) {
            log.error("Error occurred during updateBalancesTask", e);
        }
    }
}
