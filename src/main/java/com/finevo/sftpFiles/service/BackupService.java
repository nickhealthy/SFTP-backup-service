package com.finevo.sftpFiles.service;

import com.finevo.sftpFiles.util.CompressionUtil;
import com.finevo.sftpFiles.util.SFTPUtil;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Vector;

@Slf4j
@RequiredArgsConstructor
@Service
public class BackupService {

    private final SFTPUtil sftpUtil;
    private ChannelSftp channelSftp = null;


    /**
     * 파일과 하위 디렉토리의 모든 파일을 백업/압축/삭제
     *
     * @param remoteDir remote 서버 백업 대상 경로
     * @param localDir  local 서버 백업 저장 경로
     */
    public void backupFiles(String remoteDir, String localDir) {
        try {
            channelSftp = sftpUtil.connect();
            // remote 백업 파일 다운로드 후, local 원본 백업 파일 압축/삭제
            backupDirectory(channelSftp, remoteDir, localDir);
            // remote 디렉토리 안의 백업 파일을 재귀적으로 삭제
            deleteFiles(channelSftp, remoteDir, remoteDir);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sftpUtil.close();
        }
    }

    /**
     * remote 백업 파일 local에 다운로드 후, 백업 파일 압축/삭제
     *
     * @param channelSftp SFTP 채널 소켓
     * @param remoteDir   remote 서버 백업 대상 경로
     * @param localDir    local 서버 백업 저장 경로
     * @throws Exception
     */
    private void backupDirectory(ChannelSftp channelSftp, String remoteDir, String localDir) throws Exception {
        Vector<ChannelSftp.LsEntry> list = channelSftp.ls(remoteDir);

        for (ChannelSftp.LsEntry entry : list) {
            if (entry.getFilename().equals(".") || entry.getFilename().equals("..")) {
                continue;
            }

            String remoteFilePath = remoteDir + "/" + entry.getFilename();
            String localFilePath = localDir + "/" + entry.getFilename();
            File localFile = new File(localFilePath);

            if (entry.getAttrs().isDir()) {
                if (!localFile.exists()) {
                    localFile.mkdirs();
                }
                backupDirectory(channelSftp, remoteFilePath, localFilePath);
            } else {
                if (!localFile.exists()) {
                    try (OutputStream outputStream = new FileOutputStream(localFile)) {
                        channelSftp.get(remoteFilePath, outputStream);
                    }
                    CompressionUtil.compressFile(localFile, localFilePath + ".zip");
                    localFile.delete(); // 원본 파일 삭제
                }
            }
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