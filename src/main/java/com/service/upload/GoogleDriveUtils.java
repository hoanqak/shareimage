package com.service.upload;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

import java.io.*;
import java.util.Collections;

public class GoogleDriveUtils
{

    private final static String APPLICATION_NAME = "UPLOAD IMAGE";

    public static Credential getCredential() {
        try {
            File file = new File(GoogleDriveUtils.class.getResource("/google_drive/client_secret_tranhuyhoang240398_IMGPROJECT.json").toURI());

            InputStream inputStream = new FileInputStream(file);

            GoogleClientSecrets clientSecrets = new GoogleClientSecrets().load(JacksonFactory.getDefaultInstance(), new InputStreamReader(inputStream));

            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(),
                    clientSecrets, Collections.singletonList(DriveScopes.DRIVE)).setDataStoreFactory(new FileDataStoreFactory(new File("auth-google-drive"))).setAccessType("offline").build();
            Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");

            return credential;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static Drive getDrive() {
        try {
            return new Drive.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), getCredential()).setApplicationName(APPLICATION_NAME).build();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
