package jsfun.utils;

import org.mozilla.javascript.*;
import sun.misc.Signal;
import sun.misc.SignalHandler;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.File;

import jline.ConsoleReader;
import jline.ConsoleReaderInputStream;
import jline.History;

public class Shell implements SignalHandler {
	private JSEnvironment env;
	private Context cx;
	private byte[] buffer;
	private Scriptable scope;
	private StringBuilder input;
	private int line;
	private String prompt;
	private Object result;
	private ConsoleReader reader;

	public Shell(final JSEnvironment env) {

		try {
			this.reader = new ConsoleReader();
			this.reader.setHistory(new History(new File
                (System.getProperty("user.home"),
                    ".jsfun.history")));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
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


	@Override
	public void handle(Signal signal) {
		if (quitOnInterrupt()) {
			System.exit(0);
		} else {
			try {
				this.reader.setCursorPosition(0);
				this.reader.killLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		if (args.length > 0) {
			JSEnvironment env = loadEnv(args[0]);
			Shell shell = new Shell(env);
			Runtime.getRuntime().addShutdownHook(new Thread(new NewlineShutdownHook()));
			Signal.handle(new Signal("INT"), shell);
			ConsoleReaderInputStream.setIn(shell.reader);
			shell.repl();
		}
		throw new RuntimeException("Must Specify a javascript environment");
	}

	private int repl() {
		return (Integer) new ContextFactory().call(new ContextAction() {
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
	}

	private boolean read() {
		try {
			String line = this.reader.readLine(this.prompt + " ");
			this.input.append(line);
			if (cx.stringIsCompilableUnit(this.input.toString())) {
				return true;
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return false;
	}

	private void execute() {
		result = Undefined.instance;
		try {
			result = cx.evaluateString(this.scope, this.input.toString(), "shell", this.line++, null);
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
		if (result == null) {
			System.out.println("null");
		} else if (result instanceof Undefined) {
			//print nothing
		} else if (result instanceof Scriptable) {
			Scriptable scriptable = (Scriptable) result;
			Object toString;
			try {
				toString = ScriptableObject.getProperty(scriptable, "toString");
			} catch (Exception e) {
				System.out.println(scriptable.toString());
				return;
			}
			if (toString instanceof Function) {
				Function callableToString = (Function) toString;
				System.out.println(callableToString.call(cx, scope, scriptable, new Object[0]));
			} else {
				System.out.println(result.toString());
			}
		} else {
			System.out.println(result.toString());
		}
	}

	private boolean quitOnInterrupt() {
		return env == null || env.getClass().isAnnotationPresent(QuitOnInterrupt.class);
	}

	@SuppressWarnings({"unchecked"})
	private static JSEnvironment loadEnv(String name) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
		Class<? extends JSEnvironment> envs = (Class<? extends JSEnvironment>) Class.forName(name);
		JSEnvironment env = envs.newInstance();
		System.out.println("Cogent Dude on Javascript 0.1\n");
		System.out.println("Environment:\t" + envs.getSimpleName());
		if (envs.isAnnotationPresent(EnvDescription.class)) {
			System.out.println("\n" + envs.getAnnotation(EnvDescription.class).value());
		}
		System.out.println();
		return env;
	}

	private static class NewlineShutdownHook implements Runnable {
		@Override
		public void run() {
			System.out.println();
		}
	}
}