package ruo.cmplus.cm.v18.customgui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import ruo.minigame.api.WorldAPI;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * �ҽ� ��ó http://forum.falinux.com/zbxe/index.php?document_srl=565194
 *
 */
public class CMResourcePack {
	/**
	 * 버퍼 사이즈
	 */
	final static int size = 1024;
	/**
	 * fileAddress에서 파일을 읽어, 다운로드 디렉토리에 다운로드
	 * 
	 * @param fileAddress
	 * @param localFileName
	 */
	public static void fileUrlReadAndDownload(String fileAddress, String localFileName) {
		OutputStream outStream = null;
		URLConnection uCon = null;
		InputStream is = null;
		try {
			URL url = new URL(fileAddress);
			byte[] buf = new byte[size];
			int byteRead;
			int byteWritten = 0;
			outStream = new BufferedOutputStream(new FileOutputStream(getDirectory() + "\\" + localFileName));
			uCon = url.openConnection();
			is = uCon.getInputStream();
			while ((byteRead = is.read(buf)) != -1) {
				outStream.write(buf, 0, byteRead);
				byteWritten += byteRead;
			}
			is.close();
			outStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static ResourceLocation getLocation(File file2){
		
		BufferedImage img;
		try {
			img = ImageIO.read(file2);
			ResourceLocation lo = Minecraft.getMinecraft().getTextureManager()
					.getDynamicTextureLocation(getDirectory()+"/"+file2.getName(), new DynamicTexture(img));
			return lo;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}
	public static ResourceLocation copyFile(File texture){
		File output = new File(getDirectory()+"/"+texture.getName());
		if(!getDirectory().isDirectory()){
			createResourcePack();
		}

		try{
			FileInputStream fis = new FileInputStream(texture);
			FileOutputStream fos = new FileOutputStream(output);
			int data = 0;
			while((data=fis.read()) != -1){
				fos.write(data);
			}
			fis.close();
			fos.close();
			return getLocation(output);

		}catch (Exception e) {
			
			e.printStackTrace();
		}
		return null;
		
	}

	public static void createResourcePack() {
		File mcmeta = getMcmeta();
		if (mcmeta.isFile())
			return;
		try {
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
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static String imageDownload(String url) {
		int slashIndex = url.lastIndexOf('/');
		String fileName = url.substring(slashIndex + 1);
		createResourcePack();
		fileUrlReadAndDownload(url, fileName);
		return fileName;
	}
	public static File getMcmeta(){
		if(!getDirectory().isDirectory())
			getDirectory().mkdirs();
		return new File("resourcepacks/"+WorldAPI.getCurrentWorldName()+"/pack.mcmeta");
	}
	public static File getDirectory(){
		return new File("resourcepacks/"+WorldAPI.getCurrentWorldName()+"/assets/"+WorldAPI.getCurrentWorldName()+"/textures/");
	}
	public static String getDirectory(String file){
		return WorldAPI.getCurrentWorldName()+":"+"textures/"+file;
	}
}
