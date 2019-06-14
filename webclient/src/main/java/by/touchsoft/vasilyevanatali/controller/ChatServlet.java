package by.touchsoft.vasilyevanatali.controller;

import by.touchsoft.vasilyevanatali.connection.WebClientConnection;


import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

@WebServlet(name = "Chat", urlPatterns = {"/chat"})
public class ChatServlet extends HttpServlet {

    WebClientConnection webClientConnection;
    List<String> messages = new LinkedList<>();
    private Thread readerThread;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        readerThread = new Thread(() -> {
            while (true) {
                messages.add(webClientConnection.receiveMessage());
            }
        });
        readerThread.start();

        PrintWriter writer = resp.getWriter();
        for (String message : messages) {
            writer.write(message);
            System.out.println(message);
        }

    }


    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)  {

        String message = req.getParameter("message");
        System.out.println(message);
        webClientConnection.sendMessageToServer(message);
    }


}
