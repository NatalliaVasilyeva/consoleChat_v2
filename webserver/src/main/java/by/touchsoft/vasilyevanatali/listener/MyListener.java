package by.touchsoft.vasilyevanatali.listener;

import by.touchsoft.vasilyevanatali.Server;
import by.touchsoft.vasilyevanatali.User.UsersAction;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class MyListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();
        servletContext.setAttribute("server", );
        new Server( );
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
