package ruo.cmplus.deb;

import net.minecraft.launchwrapper.Launch;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

public class RClassLoader {
	public static ArrayList<String> folderName = new ArrayList<String>();
	public static ArrayList<String> fileName = new ArrayList<String>();
	public static ArrayList<String> findFileList = new ArrayList<String>();

	private static String  defaultPath = "C:/Users/oprond/Desktop/intelliJ/out/production/intelliJ_main/ruo";
	public RClassLoader(){
		if(fileName.size() == 0)
		findAllFile(null);
	}
	public void findAllFolder(File folder) {
		if(folder == null)
			folder = new File(defaultPath);
		for (File file : folder.listFiles()) {
			String path = file.getPath().replace("/", ".")
					.replace("C:.Users.oprond.Desktop.intelliJ.out.production.intelliJ_main.", "");
			if (file.isDirectory() && !folderName.contains(path)) {
				folderName.add(path);
			}
			if (file.isFile()) {
				findAllFolder(file);
			}
		}
	}
	public static void findAllFile(File folder) {
		if(folder == null)
			folder = new File(defaultPath);
		for (File file : folder.listFiles()) {
			String path = file.getPath().replace("\\", "/").replace("/", ".")
					.replace("C:.Users.oprond.Desktop.intelliJ.out.production.intelliJ_main.", "");
			if (file.isFile() && file.getName().endsWith(".class") && !fileName.contains(path)) {
				fileName.add(path);
			}
			if (file.isDirectory()) {
				findAllFile(file);
			}
		}
	}

	public String findFile(String name) {
		findFileList.clear();
		for (String file : fileName) {
			file = file.replace("\\", "/").replace("/", ".")
					.replace("D:.asdf.asdf.1.10 C2.bin.", "");
			System.out.println("aaaaa "+file);
			if(file.indexOf(name) != -1)
				findFileList.add(file);
		
		}
		
		if (findFileList.size() == 0) {
			return null;
		}
		
		if (findFileList.size() == 1) {
			return findFileList.get(0);
		}
		
		if (findFileList.size() > 1) {
			System.out.println("여러 파일을 발견했습니다");
			for(String str : findFileList) {
				System.out.println(str);
			}
			return findFileList.get(0);
		}
		return null;
	}
	public String findFolder(String name) {
		for (String file : folderName) {

			if (file.equalsIgnoreCase(name)) {
				return file;
			}
		}
		return null;
	}
	private static Object obj;

	public Object preload(String classname, String method) {
		if(findFile(classname + (classname.endsWith(".class") ? ""  : ".class")) == null) {
			File notebook = new File("C:/Users/oprond/Desktop/forge-1.10.2-12.18.3.2185-mdk/src/main/java/ruo");
			File computer = new File("D:/asdf/asdf/1.10 C2/bin/ruo");

			if(notebook.isDirectory())
				findAllFile(notebook);
			else
				findAllFile(computer);
		}
		try {
			System.out.println(findFile(classname + (classname.endsWith(".class") ? ""  : ".class"))+" aaaa "+classname+(classname.endsWith(".class") ? ""  : ".class"));
			loadClass(findFile(classname + (classname.endsWith(".class") ? ""  : ".class")).replace(".class", ""), method);
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return method;
	}

	private static void loadClass(String classname, String method) throws Exception {
		classname = classname.replace(".", "/");
			System.out.println(classname);
			System.out.println(new File(classname));
			URL[] url = new URL[] { new File(classname).toURI().toURL() };
			Launch.classLoader.addURL(url[0]);
			Class c = Launch.classLoader.loadClass(classname);
			obj = c.getConstructor().newInstance();
			if(method != null)
				c.getMethod(method).invoke(obj);
	}
}