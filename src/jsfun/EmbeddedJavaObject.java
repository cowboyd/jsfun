package jsfun;

import jsfun.utils.BeanUtilsUtils;
import org.mozilla.javascript.Scriptable;


public class EmbeddedJavaObject implements Scriptable {
	private Object javaInstance;


	public EmbeddedJavaObject(Object javaInstance) {
		this.javaInstance = javaInstance;
	}

	public String getClassName() {
		return "EmbeddedJavaInstance";
	}

	public Object get(String name, Scriptable start) {
		return BeanUtilsUtils.getProperty(javaInstance, name);
	}

	public Object get(int index, Scriptable start) {
		return null;
	}

	public boolean has(String name, Scriptable start) {
		return false;
	}

	public boolean has(int index, Scriptable start) {
		return false;
	}

	public void put(String name, Scriptable start, Object value) {
		BeanUtilsUtils.setProperty(javaInstance, name, value);
	}

	public void put(int index, Scriptable start, Object value) {
	}

	public void delete(String name) {
	}

	public void delete(int index) {
	}

	public Scriptable getPrototype() {
		return null;
	}

	public void setPrototype(Scriptable prototype) {
	}

	public Scriptable getParentScope() {
		return null;
	}

	public void setParentScope(Scriptable parent) {
	}

	public Object[] getIds() {
		return new Object[0];
	}

	public Object getDefaultValue(Class hint) {
		return null;
	}

	public boolean hasInstance(Scriptable instance) {
		return false;
	}
}
