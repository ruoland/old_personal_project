package ruo.cmplus.cm.v18.function;

import ruo.cmplus.cm.v17.Deb;

import java.util.ArrayList;

public class FunctionIF {
	public static ArrayList<FunctionIF> fifList = new ArrayList<FunctionIF>();

	public String args1, type, args2;

	private FunctionIF(String args1, String type, String args2) {
		this.args1 = args1;
		this.args2 = args2;
		this.type = type;
	}

	public boolean check() {
		if (VAR.hasInteger(args1)) {
			Deb.msgfunc("조건", "Integer 변수로 조건을 실행합니다.");
			Deb.msgfunc("조건", args1 + " " + args2);
			return VAR.ifcheck(args1, type, args2);
		}
		if (VAR.hasBoolean(args1) ) {
			Deb.msgfunc("조건", "불린 변수에서 실행합니다." + VAR.getBoolean(args1));
			return VAR.getBoolean(args1);
		}
		Deb.msgfunc("조건", "String 변수에서 실행합니다.");
		Deb.msgfunc("조건", VAR.getStr(args1) + type + VAR.getStr(args2));

		if (type.equalsIgnoreCase("!=")) {
			return !VAR.getStr(args1).equalsIgnoreCase(VAR.getStr(args2));
		} else
			return VAR.getStr(args1).equalsIgnoreCase(VAR.getStr(args2));
	}

	/**
	 * 조건문 객체를 만들지만 리스트에 조건 객체를 추가하지는 않음 While 클래스에서 사용됨
	 */
	public static FunctionIF create(String args1, String type, String args2) {
		return new FunctionIF(args1, type, args2);
	}

	public static FunctionIF addIF(String args1, String type, String args2) {
		fifList.add(new FunctionIF(args1, type, args2));
		return currentIF();
	}

	/**
	 * 이는 불린 변수용
	 */
	public static FunctionIF addIF(String args1) {
		fifList.add(new FunctionIF(args1, "", ""));
		return currentIF();
	}

	/**
	 * 조건이 있는지 반환하는 메서드
	 */
	public static boolean isIF() {
		return fifList.size() > 0;
	}

	/**
	 * 현재 IF를 가져오는 메서드
	 */
	public static FunctionIF currentIF() {
		return fifList.get(fifList.size() - 1);
	}

	/**
	 * IF를 제거하는 메서드
	 */
	public static void removeIF(FunctionIF fif) {
		fifList.remove(fif);
	}

	/**
	 * 현재 IF를 제거하는 메서드
	 */
	public static void removeIF() {
		fifList.remove(currentIF());
	}
}
