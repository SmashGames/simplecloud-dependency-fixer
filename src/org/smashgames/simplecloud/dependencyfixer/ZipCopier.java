package org.smashgames.simplecloud.dependencyfixer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipCopier {

   public static void copyWithOut(ZipFile source, File destination, String... directoriesWithout) throws IOException {
           try (ZipOutputStream destFile = new ZipOutputStream(
                   Files.newOutputStream(Paths.get(destination.toURI())))) {
               Enumeration<? extends ZipEntry> entries = source.entries();
               while (entries.hasMoreElements()) {
                   ZipEntry src = entries.nextElement();
                   if(Arrays.stream(directoriesWithout).anyMatch(dirName -> dirName.equals(src.getName()))) continue;
                   ZipEntry dest = new ZipEntry(src.getName());
                   destFile.putNextEntry(dest);
                   try (InputStream content = source.getInputStream(src)) {
                       content.transferTo(destFile);
                   }
                   destFile.closeEntry();
               }
               destFile.finish();
           }
   }

   public static void copyWithOut(File source, File destination, String... directoriesWithout) throws IOException{
       copyWithOut(new ZipFile(source), destination, directoriesWithout);
   }

}
