package jsfun.examples.twitter.server;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.ResourceHandler;
import org.mortbay.jetty.handler.HandlerCollection;
import org.mortbay.jetty.handler.ContextHandlerCollection;
import org.mortbay.jetty.handler.ContextHandler;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.jetty.servlet.Context;
import org.apache.jasper.servlet.JspServlet;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class Main {
	public static void main(String[] args) throws Exception {
		Server server = new Server(8080);
		ContextHandlerCollection handlers = new ContextHandlerCollection();
		server.setHandler(handlers);

		Context root = new Context(handlers, "/", Context.SESSIONS);
		root.setResourceBase("./web");
		root.addServlet(new ServletHolder(new JspServlet()), "*.jsp");
		root.addServlet(new ServletHolder(new TwitterScriptServlet()), "/api");
		handlers.addContext("/pub", "./web").addHandler(new ResourceHandler());

		server.start();
	}
}
