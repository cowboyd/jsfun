package jsfun.utils;

import org.mozilla.javascript.*;

import java.io.IOException;

@SuppressWarnings({"unchecked"})
public class Shell {
	private JSEnvironment env;
	private Context cx;
	private byte[] buffer;
	private Scriptable scope;
	private StringBuilder input;
	private int line;
	private String prompt;

	public Shell(final JSEnvironment env) {
		this.env = env;
		this.line = 1;
		this.buffer = new byte[2048];
		this.input = new StringBuilder();
		if (env.getClass().isAnnotationPresent(Prompt.class)) {
			this.prompt = env.getClass().getAnnotation(Prompt.class).value();
		} else {
			this.prompt = env.getClass().getSimpleName() + ">";
		}
	}

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		if (args.length > 0) {
			JSEnvironment env = loadEnv(args[0]);
			Shell shell = new Shell(env);
			int exitValue = shell.repl();
			System.exit(exitValue);
		}
		throw new RuntimeException("Must Specify a javascript environment");
	}

	private int repl() {
		new ContextFactory().call(new ContextAction() {
			public Object run(Context cx) {
				Shell.this.cx = cx;
				Shell.this.scope = env.createScope(cx);
				for (;;) {
					if (read()) {
						execute();
						print();
					}
				}
			}
		});
		return 0;
	}

	private boolean read() {
		try {
			System.out.print(this.prompt + " ");
			int read = System.in.read(buffer);
			this.input.append(new String(buffer, 0, read));
			if (cx.stringIsCompilableUnit(this.input.toString())) {
				return true;
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return false;
	}

	private void execute() {
		try {
			Object result = cx.evaluateString(this.scope, this.input.toString(), "shell", this.line++, null);
			if (result instanceof Undefined) {
			} else {
				System.out.println(result);
			}
		} catch (EcmaError e) {
			System.err.println(e.details());
		} catch (EvaluatorException e) {
			System.err.println(e.details());
		} catch (Exception e) {
			e.printStackTrace(System.err);
		} finally {
			this.input.setLength(0);
		}
	}

	private void print() {

	}

	private static JSEnvironment loadEnv(String name) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
		Class<? extends JSEnvironment> envs = (Class<? extends JSEnvironment>) Class.forName(name);
		JSEnvironment env = envs.newInstance();
		System.out.println("The Cogent Dude on Javascript 0.1\n");
		System.out.println("Environment:\t" + envs.getSimpleName());
		if (envs.isAnnotationPresent(EnvDescription.class)) {
			System.out.println("\n" + envs.getAnnotation(EnvDescription.class).value());
		}
		System.out.println();
		return env;
	}
}