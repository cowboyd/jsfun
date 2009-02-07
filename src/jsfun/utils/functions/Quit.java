package jsfun.utils.functions;

import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class Quit extends BaseFunction {
	@Override
	public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
		System.exit(0);
		return null;
	}

	@Override
	public String toString() {
		return "[System.exit(0]";
	}
}
