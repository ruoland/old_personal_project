package ruo.cmplus.cm.v17;

import ruo.cmplus.deb.CommandDeb;

public class Deb {

	public static void msgText(String msg) {
		if(CommandDeb.debText) {
			System.out.println("[디버그]텍스트-"+msg);
		}
	}
	
	public static void msgMove(String msg) {
		if(CommandDeb.debMove) {
			System.out.println("[디버그]이동-"+msg);
		}
	}
	
	public static void msgfunc(String funcName, String msg) {
		if(CommandDeb.debFunction) {
			if(CommandDeb.funcName == null || (CommandDeb.funcName != null && CommandDeb.funcName.equalsIgnoreCase(funcName)))
			System.out.println("[디버그]펑션"+funcName+" 이벤트: "+msg);
		}
	}

	public static void msgKey(String msg) {
		
		if(CommandDeb.debKey) {
			System.out.println("[디버그]키-"+msg);
		}
	}

	public static void msgVar(String string) {
		if(CommandDeb.debVar) {
			System.out.println("[디버그]변수-"+string);
		}
	}
}
