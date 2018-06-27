package ruo.minigame.api;

import javax.script.Invocable;
import javax.script.ScriptEngine;

public class ScriptAPIFunction extends Thread {

	private ScriptEngine engine;
	private String method;
	private String script;
	private Object[] obj;
	private Object function;
	public ScriptAPIFunction(ScriptEngine api, String script, String method, Object... obj) {
		this.engine = api;
		this.obj = obj;
		this.script = script;
		this.method = method;
	}
	public void run() {
		try {
			engine.eval(script);
			Invocable c = (Invocable) engine;
			if(obj.length == 0)
				function = c.invokeFunction(method);
			else
				function = c.invokeFunction(method, obj);		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public Object function(){
		return function;
	}
}
