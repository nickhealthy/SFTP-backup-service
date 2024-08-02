package com.finevo.sftpFiles;

import com.jcraft.jsch.JSchException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

@SpringBootApplication
@EnableScheduling
public class SftpFilesApplication {

    public static void main(String[] args) throws JSchException, IOException {
        SpringApplication.run(SftpFilesApplication.class, args);

    }
}
