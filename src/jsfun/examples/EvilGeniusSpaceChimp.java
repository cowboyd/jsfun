package jsfun.examples;

import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;
import jsfun.utils.JSEnvironment;
import jsfun.utils.EnvDescription;
import jsfun.utils.Prompt;

import java.lang.reflect.InvocationTargetException;

@EnvDescription(
		"Prototypes control all aspects of property access, and evil space chimps don't\n" +
			"like to have their secrets revealed.\n\n" +
			"This environment has EvilGeniusSpaceChimp variable called 'chimp'. Try setting the chimp\n" +
			"to be the prototype of an object and see what happens. e.g.\n\n" +
			"\tvar o = new Object()\n" +
			"\t$proto(o, chimp)\n"
)
@Prompt("hoo-hoo-haaaa>")
public class EvilGeniusSpaceChimp implements Scriptable, JSEnvironment {

	private Scriptable prototype;
	private Scriptable parentScope;

	@Override
	public String getClassName() {
		return "EvilGeniusSpaceChimp";
	}

	@Override
	public Object get(String name, Scriptable start) {
		return "I don't think we'll be telling them about '" + name + "'";
	}

	@Override
	public Object get(int index, Scriptable start) {
		return "I don't think we'll be telling them about '" + index + "'";
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
			ScriptableObject.defineClass(scope, EvilGeniusSpaceChimp.class);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		scope.put("chimp", scope, new EvilGeniusSpaceChimp());
		return scope;
	}
}
