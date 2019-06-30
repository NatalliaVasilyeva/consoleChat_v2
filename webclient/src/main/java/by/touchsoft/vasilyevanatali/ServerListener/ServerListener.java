package by.touchsoft.vasilyevanatali.ServerListener;

import by.touchsoft.vasilyevanatali.Server;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


public class ServerListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        Server server = new Server();
        ServletContext servletContext = servletContextEvent.getServletContext();
        servletContext.setAttribute("server", server);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
