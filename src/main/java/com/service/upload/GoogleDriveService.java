package com.service.upload;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class GoogleDriveService
{
    public List<File> getListFileByName(String fileName) throws IOException
    {
        Drive drive = GoogleDriveUtils.getDrive();
        FileList result = drive.files().list().setQ("name = '" + fileName +"'").setFields("files(thumbnailLink, name, size, mimeType)").execute();
        List<File> files = result.getFiles();
        return files;
    }

    public File getFileByName(String fileName){
        List<File> files = null;
        try
        {
            files = getListFileByName(fileName);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        if(files != null && files.size() > 0){
            return files.get(0);
        }
        return null;
    }

}
