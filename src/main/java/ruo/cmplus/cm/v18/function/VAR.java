package ruo.cmplus.cm.v18.function;

import ruo.cmplus.deb.DebAPI;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.HashMap;

public class VAR {
	private static HashMap<String, Double> doubleVar = new HashMap<>();
	private static HashMap<String, String> stringVar = new HashMap<>();
	private static HashMap<String, Boolean> booleanVar = new HashMap<>();

	public static void putDouble(String key, double value) {
		doubleVar.put(key, value);
	}

	public static void putInteger(String key, int value) {
		doubleVar.put(key, (double) value);
	}

	public static void putString(String key, String value) {
		stringVar.put(key, value);
	}

	public static void putBoolean(String key, boolean value) {
		booleanVar.put(key, value);
	}

	public static boolean hasDouble(String key) {
		return doubleVar.containsKey(key);
	}

	public static boolean hasInteger(String key) {
		return doubleVar.containsKey(key);
	}

	public static boolean hasString(String key) {
		return stringVar.containsKey(key);
	}

	public static boolean hasBoolean(String key) {
		key = key.replace("!", "");
		return booleanVar.containsKey(key);
	}

	public static boolean getBoolean(String key) {
		if (hasBoolean(key)) {
			boolean reverse = key.startsWith("!");
			key = key.replace("!", "");
			return reverse ? !booleanVar.get(key) : booleanVar.get(key);
		} else {
			return Boolean.valueOf(key);
		}

	}

	public static String getStr(String key) {
		if (hasString(key))
			return stringVar.get(key);
		else {
			return String.valueOf(key);
		}
	}

	private static double getDoublePrivate(String key) {
		if (hasDouble(key))
			return doubleVar.get(key);
		else {
			try {
				return Double.valueOf(key);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	public static int getInt(String key) {
		return (int) getDouble(key);
	}

	public static double getDouble(String key) {
		ScriptEngineManager mgr = new ScriptEngineManager(null);
		ScriptEngine engine = mgr.getEngineByName("Nashorn");
		if (engine == null)
			engine = mgr.getEngineByName("JavaScript");
		if (hasDouble(key)) {
			return getDoublePrivate(key);
		}
		try {
			try {
				return (double) engine.eval(key);
			} catch (Exception e) {
				return (int) engine.eval(key);
			}
		} catch (Exception e) {
			return getDoublePrivate(key);
		}
	}

	/**
	 * 명령어에 @asdf/2 이런 인자가 넘어오는 경우에는 커맨드플러스에서 @asdf라는 변수가 있는지 알 수 없으므로 만든 메서드 asdf라는
	 * 이름을 가진 변수가 있는 경우 그 변수의 이름을 반환함
	 */
	public static String findDoubleKey(String key) {
		for (String var : doubleVar.keySet()) {
			if (var.equals(key))
				return var;
			if (key.indexOf(var) != -1)
				return var;
		}
		return null;
	}

	public static boolean ifMath(String key) {
		return key.indexOf("+") != -1 || key.indexOf("-") != -1 || key.indexOf("*") != -1
				|| (key.indexOf("/") != -1 && checkNumber(key));
	}

	public static boolean checkNumber(String key) {
		String sub = key.substring(key.indexOf("/"), key.indexOf("/") + 1);
		try {
			Integer.valueOf(sub);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean ifcheck(String firstVar, String type, String thirdVar) {
		if (hasBoolean(firstVar)) {
			return getBoolean(firstVar);
		}
		if (hasString(firstVar)) {
			if (type.equalsIgnoreCase("!="))
				return !getStr(firstVar).equalsIgnoreCase(VAR.getStr(thirdVar));
			else
				return getStr(firstVar).equalsIgnoreCase(VAR.getStr(thirdVar));
		}
		double first = getDouble(firstVar);
		double third = getDouble(thirdVar);
		if (type.equals("==")) {
			return first == third;
		}
		if (type.equals(">")) {
			return first > third;
		}
		if (type.equals("<")) {
			return first < third;
		}
		if (type.equals("<=")) {
			return first <= third;
		}
		if (type.equals(">=")) {
			return first >= third;
		}
		if (type.equals("!=")) {
			return first != third;
		} else
			return false;
	}

	public static boolean hasKey(String key) {
		return hasBoolean(key) || hasDouble(key) || hasInteger(key) || hasString(key);
	}

	public static void save() {
		DebAPI.saveObject("cmplusvarDou", doubleVar);
		DebAPI.saveObject("cmplusvarStr", stringVar);
		DebAPI.saveObject("cmplusvarBoo", booleanVar);
	}

	public static void read() {
		doubleVar = (HashMap<String, Double>) DebAPI.readObject("cmplusvarDou", new HashMap<String, Double>());
		stringVar = (HashMap<String, String>) DebAPI.readObject("cmplusvarStr", new HashMap<String, String>());
		booleanVar = (HashMap<String, Boolean>) DebAPI.readObject("cmplusvarBoo", new HashMap<String, Boolean>());
		if (doubleVar == null)
			doubleVar = new HashMap<>();
		if (stringVar == null)
			stringVar = new HashMap<>();
		if (booleanVar == null)
			booleanVar = new HashMap<>();
	}
}
