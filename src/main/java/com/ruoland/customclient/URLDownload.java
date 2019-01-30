package com.ruoland.customclient;

import net.minecraft.util.ResourceLocation;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * 소스 출처 http://forum.falinux.com/zbxe/index.php?document_srl=565194
 *
 */
public class URLDownload {
	/**
	 * 버퍼 사이즈
	 */
	final static int size = 1024;

	/**
	 * fileAddress에서 파일을 읽어, 다운로드 디렉토리에 다운로드
	 * 
	 * @param fileAddress
	 * @param localFileName
	 * @param downloadDir
	 */
	public static void fileUrlReadAndDownload(String fileAddress, String localFileName, String downloadDir) {
		OutputStream outStream = null;
		URLConnection uCon = null;
		InputStream is = null;
		try {

			//System.out.println("-------Download Start------");

			URL Url;
			byte[] buf;
			int byteRead;
			int byteWritten = 0;
			Url = new URL(fileAddress);
			outStream = new BufferedOutputStream(new FileOutputStream(downloadDir + "\\" + localFileName));

			uCon = Url.openConnection();
			is = uCon.getInputStream();
			buf = new byte[size];
			while ((byteRead = is.read(buf)) != -1) {
				outStream.write(buf, 0, byteRead);
				byteWritten += byteRead;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
				outStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @param fileAddress
	 * @param downloadDir
	 */
	private static ResourceLocation fileUrlDownload(String modid, String fileAddress, String downloadDir) {

		int slashIndex = fileAddress.lastIndexOf('/');
		int periodIndex = fileAddress.lastIndexOf('.');

		// 파일 어드레스에서 마지막에 있는 파일이름을 취득
		String fileName = fileAddress.substring(slashIndex + 1);
		File f = new File(downloadDir+fileName);

		if(f.isFile()){
			return RenderAPI.getDynamicTexture(fileName, f);
		}
		File mcmeta = new File("resourcepacks/"+modid+"/pack.mcmeta");
		try{
		mcmeta.createNewFile();
		BufferedWriter writer = new BufferedWriter(new FileWriter(mcmeta));
		writer.write("{");
		writer.newLine();
		writer.write("  \"pack\": {");
		writer.newLine();
		writer.write("    \"pack_format\": 3,");
		writer.newLine();
		writer.write("    \"description\": \"CustomClient\"");
		writer.newLine();
		writer.write("  }");
		writer.newLine();
		writer.write("}");
		writer.flush();
		writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}

		if (periodIndex >= 1 && slashIndex >= 0 && slashIndex < fileAddress.length() - 1) {
			fileUrlReadAndDownload(fileAddress, fileName, downloadDir);
		} else {
			System.err.println("path or file name NG.");
		}
		System.out.println(fileName+f);
		return RenderAPI.getDynamicTexture(fileName, f);
	}

	/**
	 * main
	 * 
	 */
	public static ResourceLocation download(String modid, String url, String downDir) {
		return fileUrlDownload(modid, url, downDir);

	}
}
