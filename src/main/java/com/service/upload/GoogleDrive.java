package com.service.upload;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.FileList;

import java.io.*;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public class GoogleDrive
{

    private static String APPLICATION_NAME = "APP";
    private static final JacksonFactory JACKSON_FACTORY = JacksonFactory.getDefaultInstance();

    private static final List<String> scopes = Collections.singletonList(DriveScopes.DRIVE);

    public static Credential createCredential() throws URISyntaxException, IOException, GeneralSecurityException
    {
        File file = new File(GoogleDrive.class.getResource("/google_drive/client_secret_tranhuyhoang240398_IMGPROJECT.json").toURI());

        InputStream inputStream = new FileInputStream(file);

        GoogleClientSecrets googleClientSecrets = GoogleClientSecrets.load(JACKSON_FACTORY,new InputStreamReader(inputStream));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(),
                googleClientSecrets, scopes).setDataStoreFactory(new FileDataStoreFactory(new File("auth-google-drive"))).setAccessType("offline").build();

        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    public static void main(String[] args) throws GeneralSecurityException, IOException, URISyntaxException
    {
        System.out.println(createCredential());

        com.google.api.services.drive.Drive service = new com.google.api.services.drive.Drive.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), createCredential())
                .setApplicationName(APPLICATION_NAME).build();

        System.out.println(service.getApplicationName());
        FileList result = service.files().list().setQ("name = 'IMG_UPLOAD' and mimeType='application/vnd.google-apps.folder'").setFields("files(*)").execute();
        List<com.google.api.services.drive.model.File> files = result.getFiles();
        if (files == null || files.isEmpty()) {
            System.out.println("No files found.");
        } else {
            System.out.println("Files:");
            for (com.google.api.services.drive.model.File file : files) {
                System.out.println(file);
            }
        }

    }



}
