package jsfun.examples.twitter.server;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.jetty.servlet.Context;
import org.apache.jasper.servlet.JspServlet;


public class Main {
		public static void main(String[] args) throws Exception {
		Server server = new Server(8080);
		Context root = new Context(server, "/", Context.SESSIONS);
		root.setResourceBase("./web");
		root.addServlet(new ServletHolder(new JspServlet()), "*.jsp");
		root.addServlet(new ServletHolder(new TwitterScriptServlet()), "/api");
		server.start();
	}
}
