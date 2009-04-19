package jsfun.examples;

import jsfun.utils.JSEnvironment;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;

/**
 * Created by IntelliJ IDEA.
 * User: cowboyd
 * Date: Apr 17, 2009
 * Time: 5:32:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class StandardObjectNoJava implements JSEnvironment {

	@Override
	public Scriptable createScope(Context cx) {
		ScriptableObject scope = cx.initStandardObjects();
		scope.delete("Packages");
		scope.delete("java");
		scope.delete("javax");
		scope.delete("org");
		scope.delete("com");
		scope.delete("edu");
		scope.delete("getClass");
		scope.delete("JavaAdapter");
		scope.delete("JavaImporter");
		scope.delete("Continuation");
		scope.delete("JavaImporter");
		scope.delete("XML");
		scope.delete("XMLList");
		scope.delete("Namespace");
		scope.delete("QName");
		return scope;
	}
}
