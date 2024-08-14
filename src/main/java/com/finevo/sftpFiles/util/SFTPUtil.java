package com.finevo.sftpFiles.util;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.finevo.sftpFiles.config.SftpProperties.Server;

@Slf4j
@Component
public class SFTPUtil {

    JSch jsch = new JSch();
    Session session = null;
    ChannelSftp channelSftp = null;

    public ChannelSftp connect(Server server) throws JSchException {

        boolean serverKey = true;

        session = jsch.getSession(server.getUser(), server.getHost(), server.getPort());
        session.setPassword(server.getPassword());
        session.setConfig("StrictHostKeyChecking", "no");

        if (server.getPrikey() == null || server.getPrikey().isEmpty()) {
            serverKey = false;
        }

        if (!serverKey) {
            session.setPassword(server.getPassword());
        } else {
            jsch.addIdentity(server.getPrikey());
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
