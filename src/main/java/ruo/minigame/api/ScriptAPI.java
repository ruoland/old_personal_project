package ruo.minigame.api;

import net.minecraft.client.Minecraft;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.*;
import java.util.HashMap;

public class ScriptAPI {
	private static Script start(String name, boolean isScript, boolean run) {
		Script thread = new Script();
		thread.init(WorldAPI.getCurrentWorldFile()+"/commandplus/"+name, isScript, run);
		return thread;
	}

	public static Script createScript(String filename, boolean runThread) {
		return start(filename, false, runThread);
	}

	/**
	 * 스크립트를 만들고 파일이름을 가진 메서드를 추가합니다
	 */
	public static Script createScriptMethod(String filename, boolean runThread) {
		Script s = start(filename, false, runThread);
		s.addFunction(filename, "", "");
		return s;
	}

	/**
	 * 메서드를 만들고 실행합니단
	 */
	public static Script createScriptMethod(String filename, boolean runThread, Object... para) {
		Script s = start(filename, false, runThread);
		s.addFunction(filename, "", "");
		s.runFunction(filename, para);
		return s;
	}

	public static Script getNewScript(String script, boolean runThread) {
		return start(script, true, runThread);
	}

	public static ScriptAPI instance() {
		return new ScriptAPI();
	}

	public static class Script {
		public HashMap<String, String> replace = new HashMap<String, String>();
		public HashMap<String, String> replaceRe = new HashMap<String, String>();

		private boolean runThread;

		public void addScript(String custom, String code) {
			replace.put(custom, code);
			replaceRe.put(code, custom);
		}

		public void init(String init, boolean isScriptFile, boolean runT) {
			this.runThread = runT;
			this.isScriptFile = isScriptFile;
			if (!isScriptFile)
				initJS(init + ".js");
			else
				initJS(init);
			addObject("system", System.out);
			addObject("nbt", new NBTAPI("./scriptapi.dat"));
		}
		public String replaceFunction(String line) {
			try {
				int index = line.indexOf("function");
				if (index != -1) {
					String ss = line.substring(index);
					ss = ss.replace("function", "function");
					ss = ss.substring(ss.indexOf("function"), ss.indexOf("("));
					ss = ss.replace("function ", "");
					if (replace.containsKey(ss)) {
						ss = line.replace(replaceRe.get(replace.get(ss)), replace.get(ss)).replace("function ", "");
						return "function "+ss;
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return line;

		}
		public String replace(String line) {
			try {
				int index = line.indexOf("@");
				if (index != -1) {
					String ss = line.substring(index);
					ss = ss.substring(ss.indexOf("@"), ss.indexOf("("));

					if (replace.containsKey(ss)) {
						ss = line.replace(replaceRe.get(replace.get(ss)), replace.get(ss));
						if (ss.trim().indexOf("@") != -1) {
							String ss2 = ss.substring(ss.indexOf("@"));
							ss2 = ss2.substring(ss2.indexOf("@"), ss2.indexOf("("));
							ss = ss.replace(replaceRe.get(replace.get(ss2)), replace.get(ss2));
						} else
							return ss;
						return ss;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return line;
		}
		public String replace2(String line) {
			try {
				int index = line.indexOf("$");
				if (index != -1 && line.indexOf(",") != -1) {
					String ss = line.substring(index);
					ss = ss.substring(ss.indexOf("$"), ss.indexOf(","));

					if (replace.containsKey(ss)) {
						ss = line.replace(replaceRe.get(replace.get(ss)), replace.get(ss));
						if (ss.trim().indexOf("$") != -1) {
							String ss2 = ss.substring(ss.indexOf("$"));
							ss2 = ss2.substring(ss2.indexOf("$"), ss2.indexOf(","));
							ss = ss.replace(replaceRe.get(replace.get(ss2)), replace.get(ss2));
						} else
							return ss;
						return ss;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return line;
		}
		private final ScriptEngineManager em = new ScriptEngineManager(null);
		private ScriptEngine engine;
		private StringBuffer script = new StringBuffer();
		private long lastEdit = 0;
		private File f;
		public boolean isScriptFile;//파일에 저장되는 경우 true

		public void initJS(String dir) {
			engine = em.getEngineByName("Nashorn");
			if (engine == null)
				engine = em.getEngineByName("JavaScript");
			try {
				if (!isScriptFile) {
					f = new File(dir);
					if (!f.isFile()) {
						f.createNewFile();
					}
				}

				else {
					script = new StringBuffer(dir);
				}
				reload();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void reload() {
			try {
				if (script.indexOf("@") != -1 || script.indexOf("function") != -1 || !isScriptFile && isChange()) {
					BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
					script = new StringBuffer();
					for (String s = reader.readLine(); s != null; s = reader.readLine()) {
						System.out.println("Replace2---"+replace2(s));
						System.out.println("Replace---"+replace(replace2(s)));
						System.out.println("ReplaceFunction---"+replaceFunction(replace(replace2(s))));

						script.append(replaceFunction(replace(replace2(s))));
						
					}
					reader.close();
					lastEdit = f.lastModified();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void addObject(String key, Object value) {
			engine.put(key.toLowerCase(), value);
		}

		public String getScript() {
			return script.toString();
		}
		public boolean addFunction(String methodName, String Parameter, String retu) {
			try {
				if (getScript().indexOf("function " + methodName + "(" + Parameter + ")") != -1)
					return false;
				BufferedWriter b = new BufferedWriter(new FileWriter(f, true));
				b.newLine();
				b.append("function " + methodName + "(" + Parameter + ")");
				b.newLine();

				b.write("{");
				b.newLine();
				if (!retu.equals("")) {
					b.write("		return " + retu + ";");
					b.newLine();
				}
				b.write("}");
				b.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		}

		public boolean addFunction(String help, String methodName, String Parameter, String retu) {
			try {
				if (getScript().indexOf("function " + methodName + "(" + Parameter + ")") != -1)
					return false;
				BufferedWriter b = new BufferedWriter(new FileWriter(f, true));
				b.newLine();
				b.append("//"+help);
				b.newLine();
				b.append("function " + methodName + "(" + Parameter + ")");
				b.newLine();

				b.write("{");
				b.newLine();
				if (!retu.equals("")) {
					b.write("		return " + retu + ";");
					b.newLine();
				}
				b.write("}");
				b.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		}

		public boolean isChange() {
			return f.lastModified() != lastEdit;
		}
		public boolean isFile() {
			return f.exists();
		}
		public Object runNoThreadFunction(String method, Object... obj) {
			reload();
			method = replace.get(method);

			try {
				engine.eval(getScript());
				Invocable c = (Invocable) engine;
				return c.invokeFunction(method, obj);
			} catch (Exception e) {
				// if(Minecraft.getMinecraft().thePlayer != null)
				// Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new
				// ChatComponentText(e.getMessage()));
				e.printStackTrace();
			}
			return null;
		}

		public Object runFunction(String method, Object... obj) {
			reload();
			method = replace.get(method);
			ScriptAPIFunction thread = new ScriptAPIFunction(engine, getScript(), method, obj);

			if (runThread) {
				thread.start();
				return thread.function();
			} else {
				try {
					engine.eval(getScript());
					Invocable c = (Invocable) engine;
					if(obj.length == 0)
						return c.invokeFunction(method);
					else
						return c.invokeFunction(method, obj);
				} catch (Exception e) {
					if (Minecraft.getMinecraft().thePlayer != null)
						// Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new
						// ChatComponentText(e.getMessage()));
						e.printStackTrace();
				}
			}
			return null;
		}

		public ScriptAPIFunction runDebugFunction(String method, Object... obj) {
			reload();
			method = replace.get(method);

			ScriptAPIFunction thread = new ScriptAPIFunction(engine, getScript(), method, obj);

			if (runThread) {
				thread.start();
				return thread;
			}

			return null;
		}
	}

}
