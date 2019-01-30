package com.ruoland.customclient;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ResourceHelper {
    private static final File resourceFolder = new File("resourcepacks");
    public static void init(){
        File mcmeta = new File(resourceFolder+"/customclient/pack.mcmeta");
        File resourceDirectory = new File(resourceFolder+"/CustomClient");
        File zipFile = new File(resourceFolder+"/CustomClient.zip");

        if(zipFile.isFile()){
            FileInputStream fis;
            ZipInputStream zis;
            ZipEntry zipEntry;
            try{
                fis = new FileInputStream(zipFile);
                zis = new ZipInputStream(fis);
                while((zipEntry = zis.getNextEntry()) != null){
                    String filename = zipEntry.getName();
                    File file = new File(resourceDirectory, filename);
                    if (zipEntry.isDirectory()) {
                        file.mkdirs();
                    } else {
                        createFile(file, zis);
                    }
                }
            }catch (Throwable e){
                e.printStackTrace();
            }
        }
        createMeta(mcmeta);

    }
    private static void createFile(File file, ZipInputStream zis) throws Throwable {
        File parentDir = new File(file.getParent());
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
        try (FileOutputStream fos = new FileOutputStream(file)) {
            byte[] buffer = new byte[256];
            int size;
            while ((size = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, size);
            }
        } catch (Throwable e) {
            throw e;
        }
    }
    private static void createMeta(File mcmeta){
        if (!mcmeta.isFile()) {
            try {
                mcmeta.createNewFile();
                BufferedWriter writer = new BufferedWriter(new FileWriter(mcmeta));
                writer.write("{");
                writer.newLine();
                writer.write("  \"pack\": {");
                writer.newLine();
                writer.write("    \"pack_format\": 2,");
                writer.newLine();
                writer.write("    \"description\": \"CustomClient\"");
                writer.newLine();
                writer.write("  }");
                writer.newLine();
                writer.write("}");
                writer.flush();
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}


