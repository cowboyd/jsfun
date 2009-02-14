package jsfun.examples;

import jsfun.utils.JSEnvironment;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;

public class APrioriObject implements JSEnvironment {

	public Scriptable createScope(Context cx) {
		return new ScriptableObject() {
			@Override
			public String getClassName() {
				return "ReallyBare";
			}
		};
	}
}
