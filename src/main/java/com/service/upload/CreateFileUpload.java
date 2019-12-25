package com.service.upload;

import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

@Service
class UploadToServer
{
    @Value("${drive.google.folder.image-upload}")
    private String folderID;
    /**
     * Upload to folder default
     * */
    public File upload(java.io.File file, String fileName){
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
    * */
    public File upload(java.io.File file, String fileName, String folderID){
        try {
            return uploadToGoogleDrive(folderID, fileName, new InputStreamContent(null, new FileInputStream(file)));
        }
        catch (IOException e) {
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
        return drive.files().create(file, inputStreamContent).execute();
    }

}
