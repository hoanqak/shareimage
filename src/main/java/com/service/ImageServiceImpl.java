package com.service;

import com.service.base.BaseService;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Service
public class ImageServiceImpl extends BaseService implements ImageService
{
    @Override
    public void getEmailByName(String fileName, HttpServletResponse response)
    {
        response.setContentType("image/jpg");
        File file = new File(File.separator + fileName);
        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            ImageIO.write(bufferedImage, "jpg", response.getOutputStream());
            response.getOutputStream().close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
