package jsfun.utils;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public interface JSEnvironment {

	Scriptable createScope(Context cx);
}
