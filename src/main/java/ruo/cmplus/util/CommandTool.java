package ruo.cmplus.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import ruo.minigame.api.WorldAPI;

public class CommandTool {

	private final String command;
	
	public GameSettings s = Minecraft.getMinecraft().gameSettings;
	public CommandTool(String command) {
		this.command = command;
	}
	/**
	 * 배열에서 마지막에 있는 값을 가져옴
	 */
	public String getLastArg(String[] args) {
		return args[args.length - 1];
	}
	/**
	 * args안에 들어있는 문자와 arg와 일치하는 경우 true를 반환함
	 */
	public boolean argCheck(String arg, String... args){
		for(String ar : args){
			if(ar.equalsIgnoreCase(arg)){
				return true;
			}
		}
		return false;
	}
	public boolean length(String[] p_71515_2_){
		if(p_71515_2_.length == 0)
		{
			addLoMessage("help");
			return true;
		}else return false;
	}

	/**
	 * boolean 으로 변환함
	 */
	public boolean parseBoolean(String arg){
		if(arg.equalsIgnoreCase("true")  || arg.equalsIgnoreCase("on")){
			return true;
		}
		if(arg.equalsIgnoreCase("false") || arg.equalsIgnoreCase("off")){
			return false;
		}
		return Boolean.valueOf(arg);
	}

	/**
	 * boolean 으로 변환 가능한가
	 */
	public boolean parseBooleanCheck(String arg){
		if(arg.equalsIgnoreCase("true")  || arg.equalsIgnoreCase("on") || arg.equalsIgnoreCase("false") || arg.equalsIgnoreCase("off")){
			return true;
		}
		return false;
	}
	/**
	 * 첫번째 인자는 타입
	 * 두번째 인자는 기본값
	 * 세번째 인자는 인자값
	 * 타입이 set 인 경우 argValue를 반환하고
	 * add 인 경우 defaultValue에 argValue를 더한 값을 반환함
	 * 
	 * 이 메서드는 값을 설정할 때 쓰임
	 */
	public double math(String key, double defaultValue, String argValueStr){
	    double argValue = argValueStr.equals("~") ? defaultValue : Double.valueOf(argValueStr);
		if(key.equals("add"))
		{
			return defaultValue+ argValue;
		}
		if(key.equals("sub"))
		{
			return defaultValue- argValue;
		}
		if(key.equals("~"))
		{
			return defaultValue;
		}
		if(key.equals("set") || key == null)
		{
			return defaultValue = argValue;
		}
		return defaultValue;
	}
    public float math(String p_71515_2_, double defaultValue, double argValue){
        return (float) math(p_71515_2_, (double) defaultValue, ""+argValue);
    }

    public float math(String p_71515_2_, float defaultValue, float argValue){
		return (float) math(p_71515_2_, (double) defaultValue, ""+argValue);
	}

	public int math(String p_71515_2_, int defaultValue, int argValue){
		return (int) math(p_71515_2_, (double) defaultValue, ""+argValue);
	}

	/*
	 * 	public double math(String p_71515_2_, double defaultValue, double argValue){
		if(p_71515_2_.equals("add"))
		{
			return defaultValue+ argValue;
		}
		if(p_71515_2_.equals("sub"))
		{
			return defaultValue- argValue;
		}
		if(p_71515_2_.equals("set"))
		{
			return defaultValue= argValue;
		}else return defaultValue;
	}}
	 */

	public void addSettingMessage(Object...objects){
		if(!WorldAPI.getPlayer().worldObj.getGameRules().getBoolean("sendCommandFeedback"))
			return;
		if(objects.length >= 1){
			WorldAPI.getPlayer().addChatComponentMessage(new TextComponentString(I18n.format("commandPlus."+command+".setting", objects)));
			return;
		}
		WorldAPI.getPlayer().addChatComponentMessage(new TextComponentString(I18n.format("commandPlus."+command+".setting")));
	}
	public void addErrorMessage(Object...objects){
		if(objects.length >= 1){
			WorldAPI.getPlayer().addChatComponentMessage(new TextComponentString(I18n.format("commandPlus."+command+".error", objects)).setStyle(new Style().setColor(TextFormatting.RED)));
			return;
		}
		WorldAPI.getPlayer().addChatComponentMessage(new TextComponentString(I18n.format("commandPlus."+command+".error")).setStyle(new Style().setColor(TextFormatting.RED)));
	}
	public void addLoMessage(String m){
		if(!WorldAPI.getPlayer().worldObj.getGameRules().getBoolean("sendCommandFeedback"))
			return;
		WorldAPI.getPlayer().addChatComponentMessage(new TextComponentString(I18n.format("commandPlus."+command+"."+m)));
	}
	public void addLoMessage(String m, Object...objects){
		if(!WorldAPI.getPlayer().worldObj.getGameRules().getBoolean("sendCommandFeedback"))
			return;
		WorldAPI.getPlayer().addChatComponentMessage(new TextComponentString(I18n.format("commandPlus."+command+"."+m, objects)));
	}
	/**
	 * 인자 최대값을 value가 넘어가는 경우
	 * 배열에서 value에 해당되는 값을 찾음, 찾지 못하면 defaultvalue를 반환함
	 */
	public String findString(String[] args, int value, String defaultvalue){
		if(args.length > value && !args[value].equals("~")){
			return args[value];
		}else
			return defaultvalue;
	}
	public double[] findPosition(String[] args, int value, double x, double y, double z) {
		double[] pos = new double[3];
		if(args.length > value+2) {
			if(args[value].equals("~"))
				pos[0] = x;
			else
				pos[0] = Double.valueOf(args[value]);

			if(args[value+1].equals("~"))
				pos[1] = y;	
			else
				pos[1] = Double.valueOf(args[value+1]);
			if(args[value+2].equals("~"))
				pos[2] = z;
			else
				pos[2] = Double.valueOf(args[value+2]);
			return pos;
		}
		pos[0] = x;
		pos[1] = y;
		pos[2] = z;
		return pos;
	}
	public double[] findPosition(String[] args, int value, BlockPos pos) {
		return findPosition(args, value, pos.getX(), pos.getY(), pos.getZ());
	}
	/**
	 * 인자 최대값을 value가 넘어가는 경우
	 * 배열에서 value에 해당되는 값을 찾음, 찾지 못하면 defaultvalue를 반환함
	 */
	public boolean findBoolean(String[] args, int value, boolean defaultvalue){
		try {
		if(args.length > value && !args[value].equals("~")){
			return Boolean.valueOf(args[value]);
		}else
			return defaultvalue;
		}catch(Exception e) {
			return defaultvalue;
		}
	}
	/**
	 * 인자 최대값을 value가 넘어가는 경우
	 * 배열에서 value에 해당되는 값을 찾음, 찾지 못하면 defaultvalue를 반환함
	 */
	public int findInteger(String[] args, int value, int defaultvalue){
		if(args.length > value && !args[value].equals("~")){
			return Integer.valueOf(args[value]);
		}else
			return defaultvalue;
	}
	/**
	 * 인자 최대값을 value가 넘어가는 경우
	 * 배열에서 value에 해당되는 값을 찾음, 찾지 못하면 defaultvalue를 반환함
	 */
	public float findFloat(String[] args, int value, float defaultvalue){
		if(args.length > value && !args[value].equals("~")){
			return Float.valueOf(args[value]);
		}else
			return defaultvalue;
	}
	public String getCommand(String[] args, int length){
		final StringBuffer command = new StringBuffer();
		for(int i = 0;i<args.length-length; i++){
			command.append(args[i]+" ");
		}
		return command.toString();
	}
	/**
	 * args에서 firstLength부터 maxLength까지 가져옵니다
	 */
	public String getCommand(String[] args, int firstLength, int maxLength){
		final StringBuffer command = new StringBuffer("");
		for(int i = firstLength;i<maxLength; i++){
			command.append(args[i]+" ");
		}
		return command.toString();
	}
}
