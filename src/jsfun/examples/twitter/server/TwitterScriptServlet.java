package jsfun.examples.twitter.server;


import jsfun.examples.twitter.Twitter;
import org.mozilla.javascript.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

import jsonpp.JSONPP;
import jsonpp.layout.Layout;


public class TwitterScriptServlet extends HttpServlet implements ContextAction {
	private Scriptable scope;

	public TwitterScriptServlet() {
		this.scope = (Scriptable) new ContextFactory().call(this);
	}

	@Override
	protected void doPost(final HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain");
		JSONPP jsonpp = new JSONPP(response.getWriter(), new ScriptableCodec(), new Layout());
		Object result;
		try {
			try {
				result = new ServletContextFactory().call(new RunScript(request.getInputStream(), this.scope));
				response.setStatus(HttpServletResponse.SC_OK);
				if (result instanceof Undefined) {
					result = "undefined";
				}
				jsonpp.pp(result);
			} catch (ExceptionWrapper e) {
				throw e.getRootCause();
			}

		} catch (RhinoException e) {
			Map m = error(e.getMessage(), response);
			m.put("stack", getScriptTrace(e));
			jsonpp.pp(m);
		} catch (Exception e) {
			jsonpp.pp(error(e.getMessage(), response));
		} catch (LongRunningScriptError e) {
			jsonpp.pp(error("Script exceeded the maximum allotment of instructions", response));
		} catch (Throwable t) {
			if (t instanceof Error) {
				throw (Error)t;
			} else {
				throw new Error(t);
			}
		}

	}

	@Override
	public Object run(Context cx) {
		ScriptableObject scope = null;
		try {
			scope = (ScriptableObject) new Twitter().createScope(cx);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		deepSeal(scope);
		return scope;
	}

	private void deepSeal(ScriptableObject o) {
		deepSeal(o, new HashSet<ScriptableObject>());
	}

	private void deepSeal(ScriptableObject o, Set<ScriptableObject> seen) {
		if (seen.contains(o)) return;
		seen.add(o);
		o.sealObject();
		for (int i = 0; i < o.getAllIds().length; i++) {
			Object id = o.getAllIds()[i];
			Object value;
			if (id instanceof String) {
				String name = (String) id;
				value = o.get(name, o);
			} else {
				Integer idx = (Integer) id;
				value = o.get(idx, o);
			}
			if (value instanceof ScriptableObject) {
				deepSeal((ScriptableObject) value, seen);
			}
		}
	}

	public static class ServletContextFactory extends ContextFactory {

		@Override
		protected void onContextCreated(Context cx) {
			cx.setInstructionObserverThreshold(100000);
			Twitter.setupContext(cx);
			super.onContextCreated(cx);	//To change body of overridden methods use File | Settings | File Templates.
		}

		@Override
		protected void observeInstructionCount(Context cx, int instructionCount) {

			throw new LongRunningScriptError();
		}

	}


	public static class RunScript implements ContextAction {

		private Scriptable scope;

		private InputStream input;

		public RunScript(InputStream input, Scriptable scope) {
			this.input = input;
			this.scope = scope;
		}

		@Override
		public Object run(Context cx) {
			try {
				NativeObject o = new NativeObject();
				o.setParentScope(scope);
				return cx.evaluateReader(o, new InputStreamReader(input), "script", 1, null);
			} catch (Exception e) {
				throw new ExceptionWrapper(e);
			}

		}

	}

	private static class LongRunningScriptError extends Error {

	}

	private static class ExceptionWrapper extends RuntimeException {
		public ExceptionWrapper(Exception e) {
			super(e);
		}

		public Throwable getRootCause() {
			for (Throwable t = this; ; t = t.getCause()) {
				if (t.getCause() == null) {
					return t;
				}
			}
		}
	}

	private Map error(String message, HttpServletResponse response) {
		response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("error", message);
		return map;
	}

	private ArrayList getScriptTrace(RhinoException e) {
		String raw = e.getScriptStackTrace(new FilenameFilter() {
			@Override
			public boolean accept(File file, String s) {
				return !s.endsWith(".java");
			}
		});
		String[] elements = raw.split("\n");
		ArrayList list = new ArrayList(elements.length);
		for (int i = 0; i < elements.length; i++) {
			String element = elements[i];
			if (element.length() > 0) {
				list.add(element.replaceAll("^\\s+at ", ""));
			}
		}
		return list;
	}	
}
