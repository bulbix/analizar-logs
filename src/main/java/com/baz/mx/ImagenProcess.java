/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baz.mx;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.imageio.spi.IIORegistry;
//import org.geotoolkit.image.io.plugin.RawTiffImageReader;

/**
 *
 * @author acruzb
 */
public class ImagenProcess {
    public static void main(String[] args) throws IOException {
        IIORegistry registry = IIORegistry.getDefaultInstance();   
        //registry.registerServiceProvider(new RawTiffImageReader.Spi());            

        String[] a = ImageIO.getReaderFileSuffixes();    
        System.out.println("Iniciando...");
        for (int i=0; i<a.length; i++) {
            System.out.println(a[i]);
        }   

        BufferedImage image = ImageIO.read(new File("E:\\myImg.jpg"));

//        ImageIO.write(image, "jpg",new File("E:\\out.jpg"));
        ImageIO.write(image, "gif",new File("E:\\out.gif"));
        ImageIO.write(image, "png",new File("E:\\out.png"));
        ImageIO.write(image, "tif",new File("E:\\out.tiff"));
    }
}
