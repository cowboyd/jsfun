package jsfun.examples;

import jsfun.utils.EnvDescription;
import jsfun.utils.JSEnvironment;
import jsfun.utils.functions.Quit;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;

@EnvDescription(
		"This is the standard scripting environment that comes 'out of the box' with Rhino." +
				"The standard objects are all there, but nothing else.")
public class StandardObjects implements JSEnvironment{
	@Override
	public Scriptable createScope(Context cx) {
		ScriptableObject scope = cx.initStandardObjects();
		ScriptableObject.putProperty(scope, "quit", new Quit());
		return scope;
	}
}
