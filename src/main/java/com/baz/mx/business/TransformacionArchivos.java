/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baz.mx.business;

import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.ImageEncoder;
import com.sun.media.jai.codec.JPEGDecodeParam;
import com.sun.media.jai.codec.JPEGEncodeParam;
import com.sun.media.jai.codec.SeekableStream;
import com.sun.media.jai.codec.TIFFDecodeParam;
import com.sun.media.jai.codec.TIFFEncodeParam;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

/**
 *
 * @author acruzb
 */
public class TransformacionArchivos {

    private static final Logger LOGGER = Logger.getLogger(TransformacionArchivos.class);
    
    public static synchronized  byte[] decodeBase64(String base64){
        return Base64.decodeBase64(base64.getBytes());
    }

    public static synchronized  byte[] convertirBase64APDF(String pdfBase64) {
        LOGGER.info("Se realiza la conversión del archivo pdf a Base 64.");
        return Base64.decodeBase64(pdfBase64.getBytes());
    }

    public static synchronized String convertirPDFABase64(byte[] pdfBytes) {
        LOGGER.info("Se realiza la conversión del archivo pdf a Base 64: " + new String(Base64.encodeBase64(pdfBytes)));
        return new String(Base64.encodeBase64(pdfBytes));
    }
    
    public static synchronized String convertTIFFBase64ToJPGBase64(String imageTIFFBase64) {
        try{
            InputStream inputStream = new ByteArrayInputStream(Base64.decodeBase64(imageTIFFBase64.getBytes()));
            SeekableStream s = SeekableStream.wrapInputStream(inputStream, true);
            TIFFDecodeParam param = null;
            ImageDecoder dec = ImageCodec.createImageDecoder("tiff", s, param);
            RenderedImage op = dec.decodeAsRenderedImage(0);

            JPEGEncodeParam jpgparam = new JPEGEncodeParam();
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream();) {
                ImageEncoder en = ImageCodec.createImageEncoder("jpeg", baos, jpgparam);
                en.encode(op);
                return new String(Base64.encodeBase64(baos.toByteArray()));
            }
        }catch(IOException e){
            LOGGER.info("No se pudo convertir de tiff a jpg: " + e.getMessage());
            return null;
        }
    }
    
    public static synchronized String convertJPGBase64ToTIFFBase64(String imageJPGBase64) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(Base64.decodeBase64(imageJPGBase64.getBytes()));
        SeekableStream s = SeekableStream.wrapInputStream(inputStream, true);
        JPEGDecodeParam param = null;
        ImageDecoder dec = ImageCodec.createImageDecoder("jpeg", s, param);
        RenderedImage op = dec.decodeAsRenderedImage(0);

        TIFFEncodeParam tiffparam = new TIFFEncodeParam();
        tiffparam.setCompression(TIFFEncodeParam.COMPRESSION_JPEG_TTN2);
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();) {
                ImageEncoder en = ImageCodec.createImageEncoder("tiff", baos, tiffparam);
                en.encode(op);
                return new String(Base64.encodeBase64(baos.toByteArray()));
        }
    }

}
