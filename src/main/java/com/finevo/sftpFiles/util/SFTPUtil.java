package com.finevo.sftpFiles.util;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SFTPUtil {

    JSch jsch = new JSch();
    Session session = null;
    ChannelSftp channelSftp = null;

    @Value("${sftp.host}")
    private String host;

    @Value("${sftp.port}")
    private int port;

    @Value("${sftp.username}")
    private String username;

    @Value("${sftp.password}")
    private String password;

    @Value("${sftp.prikey}")
    private String prikey;

    public ChannelSftp connect() throws JSchException {
        boolean serverKey = true;

        session = jsch.getSession(username, host, port);
        session.setPassword(password);
        session.setConfig("StrictHostKeyChecking", "no");

        if (prikey == null || prikey.isEmpty()) {
            serverKey = false;
        }

        if (!serverKey) {
            session.setPassword(password);
        } else {
            jsch.addIdentity(prikey);
        }

        session.connect();

        channelSftp = (ChannelSftp) session.openChannel("sftp");
        channelSftp.connect();

        return channelSftp;

    }

    public void close() {
        if (channelSftp != null) {
            channelSftp.disconnect();
        }
        if (session != null) {
            session.disconnect();
        }
    }
}
