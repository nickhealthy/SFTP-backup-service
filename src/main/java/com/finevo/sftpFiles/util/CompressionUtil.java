package com.finevo.sftpFiles.util;

import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


@Component
public class CompressionUtil {

    private static final int SIZE = 1024;

    /**
     * 백업 파일을 ZIP으로 압축
     *
     * @param file          백업 대상 파일
     * @param outputZipFile 백업 파일명.zip
     * @throws IOException
     */
    public static void compressFile(File file, String outputZipFile) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(outputZipFile);
             ArchiveOutputStream aos = new ArchiveStreamFactory()
                     .createArchiveOutputStream(ArchiveStreamFactory.ZIP, fos)) {

            aos.putArchiveEntry(new ZipArchiveEntry(file, file.getName()));

            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] buffer = new byte[SIZE];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    aos.write(buffer, 0, length);
                }
            }

            aos.closeArchiveEntry();
        } catch (Exception e) {
            throw new IOException("Error while compressing file: " + file.getName(), e);
        }
    }
}
