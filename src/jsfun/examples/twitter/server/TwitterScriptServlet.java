package jsfun.examples.twitter.server;


import jsfun.examples.twitter.Twitter;
import org.mozilla.javascript.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;

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
		Object result = new ServletContextFactory().call(new RunScript(request.getInputStream(), this.scope));
		response.setStatus(HttpServletResponse.SC_OK);
		new JSONPP(response.getWriter(), new ScriptableCodec(), new Layout()).pp(result);

	}

	@Override
	public Object run(Context cx) {
		try {
			ScriptableObject scope = (ScriptableObject) new Twitter().createScope(cx);
			scope.sealObject();
			for (int i = 0; i < scope.getIds().length; i++) {
				Object key = scope.getIds()[i];
				if (key instanceof String) {
					String name = (String) key;
					ScriptableObject member = (ScriptableObject) scope.get(name, scope);
					member.sealObject();
				}
			}
			return scope;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static class ServletContextFactory extends ContextFactory {

		@Override
		protected void onContextCreated(Context cx) {
			cx.setInstructionObserverThreshold(100);
			Twitter.setupContext(cx);
			super.onContextCreated(cx);	//To change body of overridden methods use File | Settings | File Templates.
		}

		@Override
		protected void observeInstructionCount(Context cx, int instructionCount) {

			throw new Error("Script Took Too Long");
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
				throw new RuntimeException(e);
			}

		}
	}
}
