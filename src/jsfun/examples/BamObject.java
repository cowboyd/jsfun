package jsfun.examples;

import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;
import jsfun.utils.JSEnvironment;
import jsfun.utils.EnvDescription;

import java.lang.reflect.InvocationTargetException;

@EnvDescription(
		"The Bam Object demonstrates how prototypes have complete control over property lookup.\n" +
				"All the do is return the string \"Bam!\" for every property. It is an object in \n" +
				"its own right, but also is useable as a prototype. The code below has already been executed. \n\n" + BamObject.INITIAL_JS
)
public class BamObject implements Scriptable, JSEnvironment {

	public static final String INITIAL_JS = "\tvar bam = new BamObject();\n" +
			"\tfunction OtherObject() {\n" +
			"\t};\n" +
			"\tOtherObject.prototype = bam;\n";
	private Scriptable prototype;
	private Scriptable parentScope;

	@Override
	public String getClassName() {
		return "BamObject";
	}

	@Override
	public Object get(String name, Scriptable start) {
		return "BAM!";
	}

	@Override
	public Object get(int index, Scriptable start) {
		return "BAM!";
	}

	@Override
	public boolean has(String name, Scriptable start) {
		return true;
	}

	@Override
	public boolean has(int index, Scriptable start) {
		return true;
	}

	@Override
	public void put(String name, Scriptable start, Object value) {
	}

	@Override
	public void put(int index, Scriptable start, Object value) {
	}

	@Override
	public void delete(String name) {
	}

	@Override
	public void delete(int index) {
	}

	@Override
	public Scriptable getPrototype() {
		return this.prototype;
	}

	@Override
	public void setPrototype(Scriptable prototype) {
		this.prototype = prototype;
	}

	@Override
	public Scriptable getParentScope() {
		return this.parentScope;
	}

	@Override
	public void setParentScope(Scriptable parent) {
		this.parentScope = parent;
	}

	@Override
	public Object[] getIds() {
		return new Object[0];
	}

	@Override
	public Object getDefaultValue(Class hint) {
		return null;
	}

	@Override
	public boolean hasInstance(Scriptable instance) {
		return false;
	}

	@Override
	public Scriptable createScope(Context cx) {
		ScriptableObject scope = cx.initStandardObjects();
		try {
			ScriptableObject.defineClass(scope, BamObject.class);
			cx.evaluateString(scope, INITIAL_JS, "init", 1, null);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return scope;
	}
}
