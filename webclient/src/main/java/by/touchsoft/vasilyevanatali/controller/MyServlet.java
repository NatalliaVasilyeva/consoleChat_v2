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
import java.util.LinkedList;
import java.util.List;


@WebServlet(name = "Controller", urlPatterns = {"/registration"})
public class MyServlet extends HttpServlet {

    WebClientConnection webClientConnection;
    boolean isRegister = false;
    HttpSession session;
    List<String> messages = new LinkedList<>();
    private  Thread readerThread;


    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        PrintWriter writer = resp.getWriter();
        for (String message : messages) {
            writer.write(message);
            System.out.println(message);
        }

    }


    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Server server = (Server) getServletContext().getAttribute("server");
        session = req.getSession(true);
        String registrationMessage = req.getParameter("regMessage");
        String message = req.getParameter("message");
        if (registrationMessage != null && message == null && isRegister == false) {
            webClientConnection = new WebClientConnection();
            webClientConnection.connectToServer();
            webClientConnection.checkFirstMessage(registrationMessage);
            if (webClientConnection.checkFirstMessage(registrationMessage) == false) {
                req.setAttribute("answerFromServletAboutBadRegistration", "Please, check information about you registration");
                req.getRequestDispatcher("index.jsp").forward(req, resp);
            } else {
                isRegister = true;
                readerThread = new Thread(() -> {
                    while (true) {
                        messages.add(webClientConnection.receiveMessage());
                    }
                });
                readerThread.start();

                webClientConnection.sendMessageToServer(registrationMessage);
                req.setAttribute("welcomeToChat", "Welcome to chat. Please, write you message and wait the answer from client");
                req.getRequestDispatcher("chatRoom.jsp").forward(req, resp);
            }

        } else if (message != null) {
            webClientConnection.sendMessageToServer(message);
        }
        PrintWriter writer = resp.getWriter();
        for (String messageges : messages) {
            writer.write(messageges);
            System.out.println(messageges);
        }
    }
}
