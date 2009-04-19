package jsfun.utils;

import org.mozilla.javascript.ScriptableObject;


public class CleanScope {

	public static void clean(ScriptableObject scope) {
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
	}
}
