package jsfun.examples;

import jsfun.utils.JSEnvironment;
import jsfun.utils.EnvDescription;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.BaseFunction;

import java.util.HashMap;

@EnvDescription(
	"This environment is to demonstrate the hash-like operations that form the core\n" +
		"of the js runtime. The function\n\n\n" +
		"\tLoudGetAndSet()\n\n" +
		"will construct a new object which prints out a message with every call\n" +
		"to get/set and delete. The following code has been executed:\n\n" + LoudGetAndSet.INITIAL_JS
)
public class LoudGetAndSet implements Scriptable, JSEnvironment {

	HashMap<Object, Object> properties = new HashMap<Object, Object>();
	public static final String INITIAL_JS = "var loud = new LoudGetAndSet()";

	@Override
	public String getClassName() {
		return "LoudGetAndSet";
	}

	@Override
	public Object get(String name, Scriptable start) {
		System.out.println("get(" + name + ")");
		return this.properties.get(name);
	}

	@Override
	public Object get(int index, Scriptable start) {
		System.out.println("get(" + index + ")");
		return this.properties.get(index);
	}

	@Override
	public void put(String name, Scriptable start, Object value) {
		System.out.println("put(" + name + ", " + value + ")");
		this.properties.put(name, value);
	}

	@Override
	public void put(int index, Scriptable start, Object value) {
		System.out.println("put(" + index + ", " + value + ")");
		this.properties.put(index, value);
	}

	@Override
	public void delete(String name) {
		System.out.println("delete(" + name + ")");
		this.properties.remove(name);
	}

	@Override
	public void delete(int index) {
		System.out.println("delete(" + index + ")");
		this.properties.remove(index);
	}

	@Override
	public boolean has(String name, Scriptable start) {
		//System.out.println("has(" + name + ")");
		return this.properties.containsKey(name);
	}

	@Override
	public boolean has(int index, Scriptable start) {
		//System.out.println("has(" + index + ")");
		return this.properties.containsKey(index);
	}

	@Override
	public Scriptable getPrototype() {
		return null;
	}

	@Override
	public void setPrototype(Scriptable prototype) {
	}

	@Override
	public Scriptable getParentScope() {
		return null;
	}

	@Override
	public void setParentScope(Scriptable parent) {
	}

	@Override
	public Object[] getIds() {
		return this.properties.keySet().toArray();
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
		final ScriptableObject scope = cx.initStandardObjects();
		scope.put("LoudGetAndSet", scope, new BaseFunction() {
			@Override
			public Scriptable construct(Context cx, Scriptable scope, Object[] args) {
				return new LoudGetAndSet();
			}
		});
		cx.evaluateString(scope, INITIAL_JS, "<init>", 1, null);
		return scope;
	}
}
