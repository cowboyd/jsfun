package jsfun.examples.twitter;

import jsfun.utils.JSEnvironment;
import jsfun.utils.CleanScope;
import org.mozilla.javascript.*;

import java.io.InputStreamReader;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.InvocationTargetException;


public class Twitter implements JSEnvironment, ClassShutter {
	@Override
	public Scriptable createScope(Context cx) throws Exception {
		Script script = cx.compileReader(new InputStreamReader(Twitter.class.getResourceAsStream("twitter.js")), "twitter.js", 1, null);
		ScriptableObject scope = cx.initStandardObjects();
		Scriptable api = (Scriptable) script.exec(cx, scope);
		for (Object id : api.getIds()) {
			String name = (String)id;
			Scriptable function = (Scriptable) api.get(name, api);
			scope.put(name, scope, function);
		}
		setupContext(cx);
		//cx.setClassShutter(this);
		CleanScope.clean(scope);
		return scope;
	}

	public static void setupContext(Context cx) {
		cx.putThreadLocal("TWITTER_API", newApiImpl());
	}

	@Override
	public boolean visibleToScripts(String fullClassName) {
		return fullClassName.equals("$Proxy0");
	}

	public static class ApiInvoker implements InvocationHandler {

		private ApiImpl impl = new ApiImpl();

		@Override
		public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
			try {
				return method.invoke(impl, objects);
			} catch (InvocationTargetException e) {
				throw e.getCause();
			}
		}

	}

	private static Object newApiImpl() {
		return Proxy.newProxyInstance(Twitter.class.getClassLoader(), new Class[] {Api.class}, new ApiInvoker());
	}
}
