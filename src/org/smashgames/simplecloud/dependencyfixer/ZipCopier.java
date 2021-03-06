package org.smashgames.simplecloud.dependencyfixer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipCopier {


    public static void addFilesToZip(File source, File destination, ArrayList<File> files, String[] changedDirs) throws Exception {
       ZipOutputStream stream = copyZipWithExcludedDirs(source, destination, changedDirs, null);
       for(File f : files)
       {
           stream = copyZipWithIncludedDirs(f, destination, changedDirs, stream);
       }
       stream.finish();
    }

    public static ZipOutputStream copyZipWithIncludedDirs(File source, File destination, String[] includedInCopy, ZipOutputStream stream) throws IOException {
        ArrayList<String> existingFiles = new ArrayList<>();
        ZipInputStream original = new ZipInputStream(new FileInputStream(source));
        ZipOutputStream outputStream;
        if(stream == null)
            outputStream = new ZipOutputStream(new FileOutputStream(destination));
        else
            outputStream = stream;
        byte[] buffer = new byte[1024];
        ZipEntry entry;
        try {
            while ((entry = original.getNextEntry()) != null) {
                if (entry.isDirectory()) continue;
                ZipEntry finalEntry = entry;
                if (Arrays.stream(includedInCopy).noneMatch(string -> finalEntry.getName().startsWith(string))) continue;
                if (existingFiles.contains(entry.getName())) continue;
                ZipEntry newEntry = new ZipEntry(entry.getName());
                System.out.println(entry.getName());
                outputStream.putNextEntry(newEntry);
                try {
                    int len = 0;
                    while ((len = original.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, len);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                existingFiles.add(entry.getName());
                outputStream.closeEntry();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        original.close();
        return outputStream;

    }

    public static ZipOutputStream copyZipWithExcludedDirs(File source, File destination, String[] excludedFromCopy, ZipOutputStream stream) throws IOException {
        ArrayList<String> existingFiles = new ArrayList<>();
        ZipInputStream original = new ZipInputStream(new FileInputStream(source));
        ZipOutputStream outputStream;
        if(stream == null)
        outputStream = new ZipOutputStream(new FileOutputStream(destination));
        else
        outputStream = stream;
        byte[] buffer = new byte[1024];
        ZipEntry entry;
        try {
            while ((entry = original.getNextEntry()) != null) {
                if (entry.isDirectory()) continue;
                ZipEntry finalEntry = entry;
                if (Arrays.stream(excludedFromCopy).anyMatch(string -> finalEntry.getName().startsWith(string))) continue;
                if (existingFiles.contains(entry.getName())) continue;
                ZipEntry newEntry = new ZipEntry(entry.getName());
                System.out.println(entry.getName());
                outputStream.putNextEntry(newEntry);
                try {
                    int len = 0;
                    while ((len = original.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, len);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                existingFiles.add(entry.getName());
                outputStream.closeEntry();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        original.close();
        return outputStream;

    }

   public static void copyWithOut(ArrayList<File> source, File destination, String[] changedDirs) throws Exception {
        File newDestination = new File(destination.getParent(), destination.getName().replace(".jar", "_FIXED.jar"));
        if(newDestination.exists()){
            newDestination.delete();
        }

        newDestination.createNewFile();
        addFilesToZip(destination, newDestination, source, changedDirs);
   }

}
