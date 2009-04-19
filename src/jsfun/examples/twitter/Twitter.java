package jsfun.examples.twitter;

import jsfun.utils.JSEnvironment;
import jsfun.utils.CleanScope;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.ScriptableObject;

import java.io.InputStreamReader;


public class Twitter implements JSEnvironment {
	@Override
	public Scriptable createScope(Context cx) throws Exception {
		Script script = cx.compileReader(new InputStreamReader(Twitter.class.getResourceAsStream("twitter.js")), "twitter.js", 1, null);
		ScriptableObject scope = cx.initStandardObjects();
		script.exec(cx, scope);
		CleanScope.clean(scope);
		return scope;
	}
}
