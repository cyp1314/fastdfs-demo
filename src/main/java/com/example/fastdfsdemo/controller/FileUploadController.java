package com.example.fastdfsdemo.controller;

import com.example.fastdfsdemo.component.FastDFSClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

@RestController
@RequestMapping("/file")
public class FileUploadController {

    @Autowired
    private FastDFSClient fastDFSClient;

    /**
     * 上传
     * @param file
     * @return
     * @throws IOException
     */
    @RequestMapping("/upload")
    public String uploadFile(MultipartFile file) throws IOException {
        return fastDFSClient.uploadFile(file);
    }

    /**
     * 下载 http://127.0.0.1:8080/file/download?fileUrl=group1/M00/00/00/wKhECmA4mlCAFqsmAAATuwqK250631.jpg
     * @param fileUrl
     * @param response
     * @throws IOException
     */
    @RequestMapping("/download")
    public void downloadFile(String fileUrl, HttpServletResponse response) throws IOException {
        byte[] bytes = fastDFSClient.downloadFile(fileUrl);

        String[] split = fileUrl.split("\\.");
        String type = split[split.length-1];

        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("temp."+type, "UTF-8"));
        response.setCharacterEncoding("UTF-8");
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            outputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
