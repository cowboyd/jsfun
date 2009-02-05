package jsfun.examples;

import jsfun.utils.JSEnvironment;
import jsfun.utils.EnvDescription;
import jsfun.utils.Prompt;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;


@EnvDescription(
		"The absolute barest possible setup you could possibly run with(almost!). The only thing that we have implemented\n" +
		"here is the toString() function. Otherwise, we would not even be equipped with our flashlight to explore this\n" +
		"dank environment. There is no Object, no Date, no Math. We believe in Nothing Lebowski, nothing!")
@Prompt("nossing>")
public class NothingLebowskiNothing implements JSEnvironment {
	public Scriptable createScope(Context cx) {
		ScriptableObject scope = new ScriptableObject() {
			@Override
			public String getClassName() {
				return "QuiteBare";
			}

			@Override
			public String toString() {
				return "[We Believe in Nossing]";
			}
		};
		return scope;
	}
}
