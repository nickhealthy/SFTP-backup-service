package com.finevo.sftpFiles.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "sftp")
@Getter
@Setter
public class SftpProperties {

    private List<Server> servers;

    @Getter
    @Setter
    public static class Server {
        private String host;
        private int port;
        private String user;
        private String password;
        private String remoteDir;
        private String localDir;
        private String prikey;
    }

}
