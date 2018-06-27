package ruo.cmplus.cm.v18.function;

import com.google.common.io.Files;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import ruo.cmplus.cm.v17.Deb;
import ruo.minigame.api.WorldAPI;

import java.awt.*;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;

public class Function {
	private static final HashMap<String, Function> functionHash = new HashMap<>();
	private final HashMap<String, String> onereplace = new HashMap<>();
	public HashMap<String, String> replace = new HashMap<>();
	private File currentFile, eventFolder, commandFolder, guiFolder, entityFolder;
	public boolean isCanceled;//이벤트 캔슬했나
	public boolean stopRead;//폴문이 끝날 때까지 코드 실행을 멈춰야 하는가?
	private String name;

	/**
	 * @param type 펑션의 타입. 이벤트에서 호출되면 이벤트, 커맨드에서 실행되면 커맨드, 
	 * GUI 펑션을 호출하면 GUI
	 * @param name 펑션 파일 이름
	 * @param args 인자
	 * @return
	 */
	public static Function addFunction(String type, String name, String... args) {
		if (!functionHash.containsKey(name)) {
			functionHash.put(name, new Function(type, name, args));
		}
		for (int i = 0; i < args.length; i += 2) {// @인자이름, 인자값 때문에 +2함
			if (i + 1 >= args.length)
				break;
			functionHash.get(name).replace.put(args[i], args[i + 1]);
		}
		return functionHash.get(name);
	}
	public static Function getFunction(String name) {
		return functionHash.get(name);
	}

	public File getFile() {
		return currentFile;
	}

	private Function(String type, String name, String... args) {
		if (WorldAPI.getPlayer() == null)
			return;
		if (type.equals("이벤트"))
			currentFile = new File(getEventFolder() + "/" + name + ".txt");
		else if (type.equals("커맨드"))
			currentFile = new File(getCommandFolder() + "/" + name + ".txt");
		else if (type.equals("GUI"))
			currentFile = new File(getGuiFolder() + "/" + name + ".txt");
		else if (type.equals("Entity"))
			currentFile = new File(getEntityFolder() + "/" + name + ".txt");

		try {
			if (!currentFile.isFile()) {
				currentFile.createNewFile();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = 0; i < args.length; i += 2) {// @인자이름, 인자값 때문에 +2함
			if (i + 1 >= args.length)
				break;
			replace.put(args[i], args[i + 1]);
		}
		functionHash.put(name, this);
		this.name = name;
	}

	/**
	 * 일회용 인자를 추가함 펑션을 실행하면 여기에서 추가된 인자는 전부 삭제됨
	 */
	public void addOne(String... args) {
		for (int i = 0; i < args.length; i += 2) {// @인자이름, 인자값 때문에 +2함
			if (i + 1 >= args.length)
				break;
			this.onereplace.put(args[i], args[i + 1]);
		}
	}

	public String getName() {
		return name;
	}

	public void runScript() {
		if (WorldAPI.getPlayer() == null)
			return;
		if(!WorldAPI.getWorld().isRemote) {
			try {
				readLine(null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void runScript(String... args) {
		if (WorldAPI.getPlayer() == null)
			return;
		if(!WorldAPI.getWorld().isRemote) {
			try {
				readLine(args);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private ArrayList<String> writeList = new ArrayList();
	private BufferedWriter bufferedWriter;

	public void write(String command) {
		try {
			if (bufferedWriter == null)
				bufferedWriter = Files.newWriter(currentFile, Charset.forName("UTF-8"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		writeList.add(command);
	}

	public void writeEnd() {
		try {
			for (String readLine : Files.readLines(currentFile, Charset.forName("UTF-8"))) {
				bufferedWriter.write(readLine);
				bufferedWriter.newLine();
			}
			for (String writeLine : writeList) {
				bufferedWriter.write(writeLine);
				bufferedWriter.newLine();
			}
			bufferedWriter.close();
			bufferedWriter = null;
			writeList.clear();;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void readLine(String[] args) throws Exception {
		if (currentFile == null || currentFile.length() == 0) {
			Deb.msgfunc(name," 이벤트: 파일이 없거나 파일이 비어 있어 펑션이 취소됐습니다. - "+WorldAPI.getCurrentWorldName());
			return;
		}
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
				BufferedReader reader = new BufferedReader(new FileReader(currentFile));
				String command = reader.readLine();
				while (command != null) {
					if(stopRead) {
						Deb.msgfunc(name," 일시 정지 되어 있어 읽는 걸 정지함");
						continue;
					}
					if (command.startsWith("/-")) {
						command = reader.readLine();
						continue;
					}
					for (int i = 1; args != null && i < args.length; i++) {
						if (("@인자" + i).equals(args[i]) || command.indexOf("@인자" + i) != -1) {
							command = command.replace("@인자" + i, args[i]).trim();
							Deb.msgfunc(name," 리플레이스에서 인자를 바꿨음 " + command);
						}
					}
					for (String parameter : command.split(" ")) {
						if (replace.containsKey(parameter)) {
							command = command.replace(parameter, replace.get(parameter));
							Deb.msgfunc(name," 2222 리플레이스에서 인자를 바꿨음 " + command);
						}
						if (onereplace.containsKey(parameter)) {
							command = command.replace(parameter, onereplace.get(parameter));
							Deb.msgfunc(name," 2222 1회용 리플레이스에서 인자를 바꿨음 " + command);
							onereplace.remove(parameter);
						}
					}
					if(!replacePlayer(command).equalsIgnoreCase(command)) {
						command = replacePlayer(command).trim();
						Deb.msgfunc(name, " 3333 리플레이스에서 인자를 바꿨음 " + command);
					}
					if (command.equals(" ") || command.equals("")) {
						Deb.msgfunc(name," 커맨드가 공백이라 스킵함" + command);
						command = reader.readLine();
						continue;
					}
					if (command.equals("cancel")) {
						isCanceled = true;
						Deb.msgfunc(name," 이벤트가 캔슬됨");
						reader.close();
						return;
					}
					Deb.msgfunc(name,"최종 명령어" + (command.startsWith("/") ? command : "/" + command));
					WorldAPI.command(command.startsWith("/") ? command : "/" + command);
					command = reader.readLine();
				}
				reader.close();
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}

	private String replacePlayer(String com) {
		EntityPlayer e = WorldAPI.getPlayer();
		ItemStack is = e.getHeldItemMainhand();

		return com.replace("@플레이어", e.getDisplayNameString()).replace("@X", "" + e.posX).replace("@Y", "" + e.posY)
				.replace("@Z", "" + e.posZ).replace("@체력", "" + e.getHealth())
				.replace("@이름", "" + e.getDisplayNameString())
				.replace("@아이템", (is == null ? "없음" : is.getDisplayName())).replace("@차원", "" + e.dimension)
				.replace("@피치", "" + e.rotationPitch).replace("@요", "" + e.rotationPitch)
				.replace("@밝기", "" + e.getBrightness(0)).replace("@이름", e.getCustomNameTag());
	}

	private File getEventFolder() {
		if (eventFolder == null) {
			eventFolder = new File(WorldAPI.getCurrentWorldFile() + "/commandplus/" + "script/event/");
		}
		if (!eventFolder.isDirectory())
			eventFolder.mkdirs();
		return eventFolder;
	}

	private File getEntityFolder() {
		if (entityFolder == null) {
			entityFolder = new File(WorldAPI.getCurrentWorldFile() + "/commandplus/" + "script/entity/");
		}
		if (!entityFolder.isDirectory())
			entityFolder.mkdirs();
		return entityFolder;
	}

	private File getCommandFolder() {
		if (commandFolder == null) {
			commandFolder = new File(WorldAPI.getCurrentWorldFile() + "/commandplus/" + "script/");
		}
		if (!commandFolder.isDirectory())
			commandFolder.mkdirs();
		return commandFolder;
	}

	private File getGuiFolder() {
		if (guiFolder == null) {
			guiFolder = new File(WorldAPI.getCurrentWorldFile() + "/commandplus/" + "script/gui/");
		}
		if (!guiFolder.isDirectory())
			guiFolder.mkdirs();
		return guiFolder;
	}

	public void openFile() {
		try {
			Desktop.getDesktop().edit(currentFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public void openFolder() {
		try {
			Desktop.getDesktop().open(new File(WorldAPI.getCurrentWorldFile() + "/commandplus/" + "script/"));
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
}
