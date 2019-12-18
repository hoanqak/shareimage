package com.service.mail;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class TemplateMail
{

    public static String getTemplateVerifyAccount(){
        StringBuilder template  = new StringBuilder();
        try
        {
            File file = new File(TemplateMail.class.getResource("/mail/verify.html").toURI());
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()){
                template.append(scanner.nextLine());
            }
        }
        catch (URISyntaxException e)
        {
            e.printStackTrace();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return template.toString();
    }

}
