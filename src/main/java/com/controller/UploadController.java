package com.controller;

import com.service.upload.UploadDriveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;


@RestController
@RequestMapping("/api/upload")
public class UploadController
{

    @Autowired UploadDriveService uploadToServer;

    @RequestMapping
    public ResponseEntity uploadFile(@RequestParam("file")MultipartFile multipartFile)
    {
        try {
                return ResponseEntity.ok(uploadToServer.upload(multipartFile.getInputStream(),
                        new Date().getTime() + multipartFile.getOriginalFilename()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("Fail");
    }
}
