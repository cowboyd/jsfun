package jsfun.examples;

import jsfun.utils.JSEnvironment;
import jsfun.utils.EnvDescription;
import jsfun.utils.Prompt;
import org.mozilla.javascript.*;

import java.util.HashMap;

@EnvDescription(
	"By default, the rhino projection of java objects into the javascript univers 'NativeJavaObject'\n" +
		"does not include the ability to add and remove properties dynamically. Sadly, they are as\n" +
		"static as the java objects themselves.\n\n" +
		"This environment enhances this implementation by using a special prototype that stores extra\n" +
		"properties for you:\n\n" + ExpandoJavaObjects.INITIAL_JS

)
@Prompt("expando>")
public class ExpandoJavaObjects implements Scriptable, JSEnvironment {


	private HashMap<Object, Object> properties = new HashMap<Object, Object>();
	private Scriptable scope;
	public static final String INITIAL_JS =
		"function taint(object) {\n" +
		"\tobject.tainted = true;\n" +
		"\treturn object;\n" +
		"}";

	@Override
	public String getClassName() {
		return "ExpandoJavaObjects";
	}

	@Override
	public Object get(String name, Scriptable start) {
		if (start.has(name, start)) {
			return start.get(name, start);
		} else if (this.properties.containsKey(name)) {
			return this.properties.get(name);
		} else {
			return Scriptable.NOT_FOUND;
		}
	}

	@Override
	public void put(String name, Scriptable start, Object value) {
		if (start.has(name, start)) {
			start.put(name, start, value);
		} else {
			this.properties.put(name, value);
		}
	}

	@Override
	public Object get(int index, Scriptable start) {
		return properties.get(index);
	}

	@Override
	public boolean has(String name, Scriptable start) {
		return (start != this && start.has(name, start)) || properties.containsKey(name);
	}

	@Override
	public boolean has(int index, Scriptable start) {
		return properties.containsKey(index);
	}

	@Override
	public void put(int index, Scriptable start, Object value) {
		this.properties.put(index, value);
	}

	@Override
	public void delete(String name) {
		this.properties.remove(name);
	}

	@Override
	public void delete(int index) {
		this.properties.remove(index);
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
		return this.scope;
	}

	@Override
	public void setParentScope(Scriptable parent) {
		this.scope = parent;
	}

	@Override
	public Object[] getIds() {
		return this.properties.keySet().toArray();
	}

	@Override
	public Object getDefaultValue(Class hint) {
		return hint != null ? hint.toString() : "NativeJavaObject";
	}

	@Override
	public boolean hasInstance(Scriptable instance) {
		return false;
	}

	@Override
	public String toString() {
		return String.format("[object %s]", this.getClass().getSimpleName());
	}

	@Override
	public Scriptable createScope(Context context) {
		
		context.setWrapFactory(new WrapFactory() {
			public Scriptable wrapAsJavaObject(Context cx, Scriptable scope, Object javaObject, Class staticType) {
				final Scriptable wrapped = super.wrapAsJavaObject(cx, scope, javaObject, staticType);
				if (wrapped instanceof NativeJavaObject) {
					//attach the mind-leech!
					wrapped.setPrototype(new ExpandoJavaObjects());
				}
				return wrapped;
			}
		});
		final ScriptableObject scope = context.initStandardObjects();
		context.evaluateString(scope, INITIAL_JS, "<init>", 1, null);
		return scope;
	}
}
