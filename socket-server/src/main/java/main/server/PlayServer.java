package main.server;

import main.service.PlayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class PlayServer {

    @Value("${app.server.port}")
    private int port;

    @Autowired
    private PlayService playService;

    @EventListener(ApplicationReadyEvent.class)
    public void start() {//старт программы
        startServer();
    }

    public void startServer() {
        ExecutorService executor = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors());
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port: " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connect: " + clientSocket);
                executor.execute(new Negotiator(clientSocket));
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    private class Negotiator implements Runnable {

        Socket clientSocket;

        public Negotiator(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

                String request = reader.readLine();
                String response = playService.getResponse(request);
                writer.println("Server response: " + response);
                System.out.println("Send response to client: " + response);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}