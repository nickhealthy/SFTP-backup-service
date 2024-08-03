package com.finevo.sftpFiles.service;

import com.finevo.sftpFiles.util.SFTPUtil;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Vector;

@Slf4j
@RequiredArgsConstructor
@Service
public class BackupService {

    private final SFTPUtil sftpUtil;
    private ChannelSftp channelSftp = null;
    
    public void backupFiles(String remoteDir, String localDir) {
        try {
            channelSftp = sftpUtil.connect();
            // remote 디렉토리 안의 백업 파일을 재귀적으로 삭제
            deleteFiles(channelSftp, remoteDir, remoteDir);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sftpUtil.close();
        }
    }

    /**
     * remote 디렉토리 안의 백업 파일을 재귀적으로 삭제
     *
     * @param channelSftp SFTP 채널 소켓
     * @param remoteDir   remote 서버 백업 대상 삭제 경로
     * @throws SftpException
     */
    private void deleteFiles(ChannelSftp channelSftp, String remoteDir, String originRemotePath) throws SftpException {
        Vector<ChannelSftp.LsEntry> files = channelSftp.ls(remoteDir);

        for (ChannelSftp.LsEntry entry : files) {
            String remoteFilePath = remoteDir + "/" + entry.getFilename();

            if (entry.getAttrs().isDir()) {
                // 디렉토리인 경우 재귀 호출
                if (!entry.getFilename().equals(".") && !entry.getFilename().equals("..")) {
                    deleteFiles(channelSftp, remoteFilePath, originRemotePath);
                }
            } else {
                // 파일인 경우 파일 삭제
                channelSftp.rm(remoteFilePath);
            }
        }
        if (!originRemotePath.equals(remoteDir)) {
            channelSftp.rmdir(remoteDir);
        }
    }
}