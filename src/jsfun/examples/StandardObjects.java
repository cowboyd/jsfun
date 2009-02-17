package jsfun.examples;

import jsfun.utils.EnvDescription;
import jsfun.utils.JSEnvironment;
import jsfun.utils.functions.Quit;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;

@EnvDescription(
		"This is the standard scripting environment that comes 'out of the box' with Rhino.\n" +
				"This what you get when you start with a top-level scope obtained by calling\n" +
			"cx.initStandardObjects(). There are standard ECMA Objects, as well as access to\n" +
			"the classes in java.* and javax.*; To instantiate a java object, reference it by\n" +
			"its fully qualified name. e.g.\n" +
			"   var s = new java.lang.String(\"Hello World\")")
public class StandardObjects implements JSEnvironment{
	@Override
	public Scriptable createScope(Context cx) {
		ScriptableObject scope = cx.initStandardObjects();
		ScriptableObject.putProperty(scope, "quit", new Quit());
		return scope;
	}
}
