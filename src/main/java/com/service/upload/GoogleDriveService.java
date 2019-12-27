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
    private final String FIELDS = "files(id, thumbnailLink, name, size, mimeType, webContentLink, webViewLink, createdTime)";

    public List<File> getListFileByName(String fileName) throws IOException
    {
        Drive drive = GoogleDriveUtils.getDrive();
        FileList result = drive.files().list().setQ("name = '" + fileName +"'").setFields(FIELDS).execute();
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

    public List<File> getListFileByUserIDLike(String userId, int size){
        Drive drive = GoogleDriveUtils.getDrive();
        try {
            FileList fileList = drive.files().list().setQ("name contains '" + userId +"'").setFields(FIELDS).setPageSize(size).execute();
            List<File> files = fileList.getFiles();
            return files;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args)
    {
        System.out.println(new GoogleDriveService().getListFileByUserIDLike("1577354271703", 10));
    }

}
