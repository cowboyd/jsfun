package jsfun.examples;

import jsfun.utils.EnvDescription;
import jsfun.utils.JSEnvironment;
import jsfun.utils.Prompt;
import jsfun.utils.QuitOnInterrupt;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;


@EnvDescription(
		"The absolute barest possible setup you could possibly run with(almost!). The only thing that we have implemented\n" +
		"here is the toString() function. Otherwise, we would not even be equipped with our flashlight to explore this\n" +
		"dank environment. There is no Object, no Date, no Math. We believe in Nothing Lebowski, nothing!")
@Prompt("nossing>")
@QuitOnInterrupt
public class NothingLebowskiNothing extends ScriptableObject implements JSEnvironment {
	public Scriptable createScope(Context cx) {
		return this;
	}


	@Override
	public String getClassName() {
		return "Nothing";
	}

	@Override
	public String toString() {
		return "[We Believe In Nossing]";
	}
}
