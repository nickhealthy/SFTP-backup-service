package com.finevo.sftpFiles.scheduler;

import com.finevo.sftpFiles.service.BackupService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BackupScheduler {

    private final BackupService backupService;

    //    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    @Scheduled(fixedDelay = 100000)
    public void performBackup() {
        backupService.backupFiles();
    }
}
