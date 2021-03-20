package com.github.enjektor.web.tut;

import com.github.enjektor.context.ApplicationContext;
import com.github.enjektor.context.PrimitiveApplicationContext;
import com.github.enjektor.context.dependency.DefaultDependencyInitializer;
import com.github.enjektor.context.dependency.DependencyInitializer;
import com.github.enjektor.web.WebDependencyInitializer;
import com.github.enjektor.web.servlet.DefaultEnjektorServlet;
import com.google.common.collect.ImmutableList;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.util.List;

public class ExampleApplication {
    public static void main(String[] args) throws Exception {
        final DependencyInitializer dependencyInitializer = new DefaultDependencyInitializer();
        final DependencyInitializer webDependencyInitializer = new WebDependencyInitializer();
        final List<DependencyInitializer> dependencyInitializers = ImmutableList.of(dependencyInitializer, webDependencyInitializer);

        final ApplicationContext applicationContext = new PrimitiveApplicationContext(ExampleApplication.class, dependencyInitializers);
        final ExampleRouter bean = applicationContext.getBean(ExampleRouter.class);

        DefaultEnjektorServlet defaultEnjektorServlet = new DefaultEnjektorServlet(bean, ExampleRouter.class);

        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8080);

        server.setConnectors(new Connector[] {connector});

        ServletHolder holderOne = new ServletHolder();
        holderOne.setServlet(defaultEnjektorServlet);


        ServletHandler servletHandler = new ServletHandler();
        server.setHandler(servletHandler);

        servletHandler.addServletWithMapping(holderOne, "/v1/*");

        server.start();
    }
}