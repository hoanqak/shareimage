package com.service.upload;

import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.logging.Logger;

@Service
public class UploadDriveService
{
    private final Logger LOGGER = Logger.getLogger(UploadDriveService.class.toString());
    @Value("${drive.google.folder.image-upload}")
    private String folderID;

    @Autowired GoogleDriveService googleDriveService;
    /**
     * Upload to folder default
     */
    public File upload(java.io.File file, String fileName)
    {
        try
        {
            return uploadToGoogleDrive(folderID, fileName, new InputStreamContent(null, new FileInputStream(file)));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Upload to folder id
     */
    public File upload(java.io.File file, String fileName, String folderID)
    {
        try
        {
            return uploadToGoogleDrive(folderID, fileName, new InputStreamContent(null, new FileInputStream(file)));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public File upload(InputStream fileInputStream, String fileName, String folderID)
    {
        try
        {
            return uploadToGoogleDrive(folderID, fileName, new InputStreamContent(null, fileInputStream));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public File upload(InputStream fileInputStream, String fileName)
    {
        try
        {
            return uploadToGoogleDrive(folderID, fileName, new InputStreamContent(null, fileInputStream));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private File uploadToGoogleDrive(String googleFolderId, String fileName, AbstractInputStreamContent inputStreamContent) throws IOException
    {
        File file = new File();
        file.setParents(Arrays.asList(googleFolderId));
        file.setName(fileName);
        Drive drive = GoogleDriveUtils.getDrive();
        LOGGER.info("Start upload ..........");
        drive.files().create(file, inputStreamContent).execute();
        LOGGER.info("Done !!!!");
        File result = googleDriveService.getFileByName(fileName);
        new Thread(){
            @Override
            public void run()
            {
                GoogleDriveUtils.createPermissionPublic(result.getId());
            }
        }.start();
        return result;
    }

}
