package cmplus.cm.v18.function;

import cmplus.CMPlus;
import net.minecraftforge.common.config.ConfigCategory;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class VAR {

	private static ConfigCategory getDouble(){
		return CMPlus.cmPlusConfig.getCategory("doubleVar");
	}
	private static ConfigCategory getString(){
		return CMPlus.cmPlusConfig.getCategory("stringVar");
	}
	private static ConfigCategory getInteger(){
		return CMPlus.cmPlusConfig.getCategory("integerVar");
	}
	private static ConfigCategory getBoolean(){
		return CMPlus.cmPlusConfig.getCategory("booleanVar");
	}
	public static void putDouble(String key, double value) {
		CMPlus.cmPlusConfig.get("doubleVar", key, value).set(value);
	}

	public static void putInteger(String key, int value) {
		CMPlus.cmPlusConfig.get("integerVar", key, value).set(value);
	}

	public static void putString(String key, String value) {
		CMPlus.cmPlusConfig.get("stringVar", key, value).set(value);
	}

	public static void putBoolean(String key, boolean value) {
		CMPlus.cmPlusConfig.get("booleanVar", key, value).set(value);
	}

	public static boolean hasDouble(String key) {
		return getDouble().containsKey(key);
	}

	public static boolean hasInteger(String key) {
		return getInteger().containsKey(key);
	}

	public static boolean hasString(String key) {
		return getString().containsKey(key);
	}

	public static boolean hasBoolean(String key) {
		key = key.replace("!", "");
		return getBoolean().containsKey(key);
	}

	public static boolean getBoolean(String key) {
		if (hasBoolean(key)) {
			boolean reverse = key.startsWith("!");
			key = key.replace("!", "");
			return reverse ? !getBoolean().get(key).getBoolean() : getBoolean().get(key).getBoolean();
		} else {
			return Boolean.valueOf(key);
		}

	}

	public static String getStr(String key) {
		if (hasString(key))
			return getString().get(key).getString();
		else {
			return String.valueOf(key);
		}
	}

	private static double getDoublePrivate(String key) {
		if (hasDouble(key))
			return getDouble().get(key).getDouble();
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

		if (hasInteger(key))
			return getInteger().get(key).getInt();
		else {
			try {
				return Integer.valueOf(key);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return 0;
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
		for (String var : getDouble().keySet()) {
			if (var.equals(key))
				return var;
			if (key.contains(var))
				return var;
		}
		return null;
	}

	public static boolean ifMath(String key) {
		return (key.contains("+") || key.contains("-") || key.contains("*")
				|| key.contains("/")) && checkNumber(key) && key.contains("@");
	}

	public static boolean checkNumber(String key) {
		try {
			String sub = key.substring(key.indexOf("/"), key.indexOf("/") + 1);
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

}
