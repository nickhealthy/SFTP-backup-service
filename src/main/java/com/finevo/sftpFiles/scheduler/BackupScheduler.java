package com.finevo.sftpFiles.scheduler;

import com.finevo.sftpFiles.service.BackupService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BackupScheduler {

    private final BackupService backupService;

    @Value("${backup.remoteDir}")
    private String remoteDir;

    @Value("${backup.localDir}")
    private String localDir;

    //    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    @Scheduled(fixedDelay = 5000)
    public void performBackup() {
        System.out.println("sftpService = " + backupService);
        backupService.backupFiles(remoteDir, localDir);
        System.out.println("DONE");
    }
}
