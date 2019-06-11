package by.touchsoft.vasilyevanatali.listener;

import by.touchsoft.vasilyevanatali.Server;
import by.touchsoft.vasilyevanatali.User.UsersAction;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class MyListener implements ServletContextListener {

    Server server;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        server = new Server(new UsersAction());
        ServletContext servletContext = servletContextEvent.getServletContext();
        servletContext.setAttribute("server", server);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        server.disconnectServer();
    }
}
