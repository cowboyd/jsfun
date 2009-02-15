package jsfun.examples;

import jsfun.utils.JSEnvironment;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.WrapFactory;

import java.util.HashMap;

public class ExpandoJavaObjects implements Scriptable, JSEnvironment {


	private HashMap<Object, Object> properties = new HashMap<Object, Object>();
	private Scriptable scope;

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
	public Scriptable createScope(Context cx) {
		cx.setWrapFactory(new WrapFactory() {
			@Override
			public Scriptable wrapAsJavaObject(Context cx, Scriptable scope, Object javaObject, Class staticType) {
				final Scriptable wrapped = super.wrapAsJavaObject(cx, scope, javaObject, staticType);
				if (wrapped instanceof NativeJavaObject) {

					//attach the mind-leech!
					wrapped.setPrototype(new ExpandoJavaObjects());
				}
				return wrapped;
			}
		});
		return cx.initStandardObjects();
	}

	private NativeJavaObject njo(Scriptable start) {
		if (start instanceof NativeJavaObject) {
			return (NativeJavaObject) start;
		} else {
			throw new IllegalArgumentException("Hey you, expando java objects weren't meant to operate on non java objects");
		}
	}


}
