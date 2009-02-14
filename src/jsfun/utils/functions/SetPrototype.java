package jsfun.utils.functions;

import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class SetPrototype extends BaseFunction {

	@Override
	public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
		Scriptable object = (Scriptable) args[0];
		Scriptable prototype = args.length > 1 ? (Scriptable) args[1] : null;
		Scriptable old = object.getPrototype();
		object.setPrototype(prototype);
		return old;
	}

	@Override
	public String toString() {
		return "/* Dynamically change the prototype of an object at runtime */\n" +
				"function(object, prototype) {\n" +
				"\t[Native Code; arity=2]\n" +
				"}";
	}
}
