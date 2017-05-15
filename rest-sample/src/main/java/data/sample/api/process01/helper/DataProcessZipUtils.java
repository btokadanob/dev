package data.sample.api.process01.helper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.zip.ZipFile;

import org.springframework.web.multipart.MultipartFile;

import data.sample.api.process01.DataProcess01Resource;

public class DataProcessZipUtils {

    private static final int READ_BUFF = 1024;

    public static void unzipMultipartFile(MultipartFile file, DataProcess01Resource resource) {
        if (!file.isEmpty()) {
            File zipfile = new File(
                    "/tmp/upload/" + resource.getUserId() + "_" + file.getOriginalFilename() + "-uploaded.zip");
            try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(zipfile))) {
                byte[] bytes = file.getBytes();
                stream.write(bytes);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            // 解凍
            String resultData = unzip(zipfile, "/tmp/unzip/");
            resource.setSampleData(resultData);
        } else {
            throw new RuntimeException("Fileが空だ");
        }
    }

    /**
     * @param targetFile
     *            zip-file
     * @param toUnzipPath
     *            archive to dir
     */
    public static String unzip(File targetFile, String toUnzipPath) {
        StringBuilder sb = new StringBuilder();
        try (ZipFile zipFile = new ZipFile(targetFile)) {
            Collections.list(zipFile.entries()).stream().forEach(zip -> {
                File outFile = new File(toUnzipPath + zip.getName());
                if (zip.isDirectory()) {
                    outFile.mkdir();
                } else {
                    try (BufferedInputStream in = new BufferedInputStream(zipFile.getInputStream(zip));
                            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(outFile))) {
                        byte[] buffer = new byte[READ_BUFF];
                        int readsize;
                        while ((readsize = in.read(buffer)) != -1) {
                            stream.write(buffer, 0, readsize);
                            sb.append(new String(buffer, Charset.forName("utf-8")));
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }
}
