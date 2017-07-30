/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baz.mx.pruebas;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import org.apache.tools.ant.DirectoryScanner;

/**
 *
 * @author acruzb
 */
public class FilesRe {
    public static void main(String[] args) throws IOException {
        Path p1 = Paths.get("E:\\Users\\acruzb\\Documents\\NetBeansProjects\\VerificadorLogs\\logs");
        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setIncludes(new String[]{"**/*"});
        scanner.setBasedir(p1.toString());
        scanner.setCaseSensitive(false);
        scanner.scan();
        String[] files = scanner.getIncludedFiles();
        for (String file : files) {
            System.out.println(p1.resolve(file));
            Path path = p1.resolve(file);
            BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
            System.out.println("creationTime     = " + attr.creationTime());
            System.out.println("lastModifiedTime = " + attr.lastModifiedTime());
            System.out.println("isDirectory      = " + attr.isDirectory());
            System.out.println("size             = " + attr.size());
        }
    }
}
