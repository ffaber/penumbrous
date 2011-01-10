/**
 * Copyright (C) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.penumbrous.posts.p20110109;

import com.google.inject.BindingAnnotation;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class illustrates a simple embedded jetty server, configured through
 * Guice's {@link ServletModule}.
 *
 * @author penumbrousdotcom@gmail.com (Fred Faber)
 */
class EmbeddedJettyExample {

  public static void main(String[] args) throws Exception {
    Injector injector = Guice.createInjector(new EmbeddedJettyExampleModule());
    Server server = injector.getInstance(Server.class);
    server.start();
    server.join();
  }

  static class EmbeddedJettyExampleModule extends ServletModule {
    @Override protected void configureServlets() {
      bind(Server.class)
          .toProvider(JettyServerProvider.onPort(8080))
          .in(Singleton.class);

      bind(String.class).toInstance("Fred");
      bindConstant()
          .annotatedWith(IdentifiesGreetingReceiver .class)
          .to("Fred");
      bind(HelloWorldServlet.class).in(Singleton.class);
      serve("/hello").with(HelloWorldServlet.class);
    }
  }

  static class JettyServerProvider implements Provider<Server> {
    private final int port;
    @Inject Injector injector;

    private JettyServerProvider(int port) {
      this.port = port;
    }

    static JettyServerProvider onPort(int port) {
      return new JettyServerProvider(port);
    }

    @Override public Server get() {
      Server server = new Server(port);
      ServletContextHandler servletContextHandler =
          new ServletContextHandler();
      servletContextHandler.addEventListener(new GuiceServletContextListener() {
        @Override protected Injector getInjector() {
          return injector;
        }
      });

      servletContextHandler.addFilter(GuiceFilter.class, "/*", null);
      servletContextHandler.addServlet(DefaultServlet.class, "/");
      server.setHandler(servletContextHandler);
      return server;
    }
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.PARAMETER)
  @BindingAnnotation
  private @interface IdentifiesGreetingReceiver { }

  static class HelloWorldServlet extends HttpServlet {
    private final String name;

    @Inject HelloWorldServlet(@IdentifiesGreetingReceiver String name) {
      this.name = name;
    }

    @Override protected void doGet(HttpServletRequest req,
        HttpServletResponse resp) throws ServletException, IOException {
      resp.setStatus(HttpServletResponse.SC_OK);
      PrintWriter printWriter = resp.getWriter();
      printWriter.println("Hello World to: " + name);
      printWriter.flush();
    }
  }
}
