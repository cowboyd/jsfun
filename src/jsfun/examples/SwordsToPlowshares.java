package jsfun.examples;

import jsfun.utils.EnvDescription;
import jsfun.utils.Prompt;
import jsfun.utils.JSEnvironment;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.BaseFunction;

import java.util.HashMap;
import static java.lang.String.format;

@EnvDescription(
	"Use the builtin (by me) $proto function to switch the prototypes of the object at runtime. Between \n" +
		"Swords and Plowshares objects. Swords will not allow it's modus property to be set to Plowshares\n" +
		"and vice-versa.\n\n" +
		"  They both have a reap() method.\n\n" +
		"The environment starts out like this:\n\n" + SwordsToPlowshares.INITIAL_JS
)
@Prompt("choose>")
public class SwordsToPlowshares implements JSEnvironment {
	public static final String INITIAL_JS = "" +
		"sword = new Sword();\n" +
		"sword.reap = function() { return 'blood'};\n" +
		"ploughshare = new PloughShare();\n" +
		"ploughshare.reap = function() {return 'sustenance'};\n\n" +
		"citizen = {\n" +
		"   name: 'Charles',\n" +
		"   country: 'USA'\n" +
		"}\n";


	private HashMap<Scriptable, HashMap<Object, Object>> properties = new HashMap<Scriptable, HashMap<Object, Object>>();


	public Object retrieve(Scriptable object, Object key) {
		return props(object).get(key);
	}

	public void store(Scriptable object, Object key, Object value) {
		props(object).put(key, value);
	}

	public void delete(Scriptable object, Object key) {
		props(object).remove(key);
	}

	private HashMap<Object, Object> props(Scriptable object) {
		HashMap<Object, Object> map = this.properties.get(object);
		if (map == null) {
			map = new HashMap<Object, Object>();
			this.properties.put(object, map);
		}
		return map;
	}

	@Override
	public Scriptable createScope(Context cx) {
		final ScriptableObject scope = cx.initStandardObjects();
		final SwordsToPlowshares storage = this;
		scope.put("Sword", scope, new BaseFunction() {
			@Override
			public Scriptable construct(Context cx, Scriptable scope, Object[] args) {
				return new Sword(scope, null, storage);
			}
		});
		scope.put("PloughShare", scope, new BaseFunction() {
			@Override
			public Scriptable construct(Context cx, Scriptable scope, Object[] args) {
				return new PloughShare(scope, null, storage);
			}
		});
		cx.evaluateString(scope, INITIAL_JS, "<init>", 1, null);
		return scope;

	}

	private abstract class Base extends ScriptableObject {
		protected SwordsToPlowshares storage;

		protected Base(Scriptable scope, Scriptable prototype, SwordsToPlowshares storage) {
			super(scope, prototype);
			this.storage = storage;
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
		public Object get(String name, Scriptable start) {
			return doGet(name, start);
		}

		private Object doGet(Object key, Scriptable start) {
			if (key == "modus") {
				return this;
			}
			Object o = this.storage.retrieve(start, key);
			if (o == null && start != this) {
				o = doGet(key, this);
			}
			return o;
		}

		@Override
		public Object get(int index, Scriptable start) {
			return doGet(index, start);
		}

		@Override
		public void put(String name, Scriptable start, Object value) {
			if (name == "modus") {
				return;
			}
			this.storage.store(start, name, value);
		}

		@Override
		public void put(int index, Scriptable start, Object value) {
			this.storage.store(start, index, value);
		}

		@Override
		public String toString() {
			return format("[object %s]", this.getClass().getSimpleName());
		}
	}

	private class Sword extends Base {
		protected Sword(Scriptable scope, Scriptable prototype, SwordsToPlowshares storage) {
			super(scope, prototype, storage);
		}

		@Override
		public String getClassName() {
			return "Sword";
		}
	}

	private class PloughShare extends Base {
		protected PloughShare(Scriptable scope, Scriptable prototype, SwordsToPlowshares storage) {
			super(scope, prototype, storage);
		}

		@Override
		public String getClassName() {
			return "PloughShare";
		}
	}
}
