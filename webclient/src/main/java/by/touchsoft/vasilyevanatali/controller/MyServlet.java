package by.touchsoft.vasilyevanatali.controller;


import by.touchsoft.vasilyevanatali.Server;
import by.touchsoft.vasilyevanatali.connection.WebClientConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;



@WebServlet(name = "Chat", urlPatterns = {"/chat"})
public class MyServlet extends HttpServlet {

    WebClientConnection webClientConnection;
    boolean isRegister = false;
    HttpSession session;


    @Override
    public void init() {
        Server server = (Server) getServletContext().getAttribute("server");
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {}

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        session = req.getSession(true);
        String registrationMessage = req.getParameter("regMessage");

        if (registrationMessage != null && isRegister == false) {
            webClientConnection = new WebClientConnection();
            webClientConnection.connectToServer();
            webClientConnection.checkFirstMessage(registrationMessage);
            if (webClientConnection.checkFirstMessage(registrationMessage) == false) {
                req.getRequestDispatcher("index.html").forward(req, resp);
            } else {
                isRegister = true;
                webClientConnection.sendMessageToServer(registrationMessage);
                req.getRequestDispatcher("chatRoom.html").forward(req, resp);
            }
        }
    }
}
