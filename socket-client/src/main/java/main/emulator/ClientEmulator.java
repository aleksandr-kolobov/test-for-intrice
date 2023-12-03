package main.emulator;

import main.report.Record;
import main.report.Report;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@Component
public class ClientEmulator {

    @Value("${app.reportFile}")
    private String reportFile;

    @Value("${app.port}")
    private int port;

    @Value("${app.host}")
    private String host;

    @Value("${app.minBet}")
    private int minBet;

    @Value("${app.maxBet}")
    private int maxBet;

    @Value("${app.playerAmount}")
    private int playerAmount;

    @Value("${app.requestAmount}")
    private int requestAmount;

    @Value("${app.delayMillSec}")
    private int delayMillSec;

    @EventListener(ApplicationReadyEvent.class)
    public void start() {//старт программы

    	Report.init(playerAmount);
        startTest();
        Report.printInConsol();
        Report.saveInFile(reportFile);
    }

    public void startTest() {

        System.out.println(port + host);

        for (int k = 0; k < requestAmount; k++) {
            try {
                Thread.sleep(delayMillSec);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            for (int i = 1; i <= playerAmount; i++) {
                String playerName = "Player" + i;
                int bet = minBet + (int)((maxBet
                        - minBet) * Math.random());
                Thread playerThread = new Thread(() -> sendBetToServer(playerName,
                        //"always on the eagle!"
                        //"always heads up!"
                        " bet: " + bet));
                playerThread.start();
            }
        }
    }

    private void sendBetToServer(String playerName, String betData) {
        try {
            long begin = System.currentTimeMillis();
            Socket socket = new Socket(host, port);

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            writer.println(playerName + " " + betData);
            System.out.println("Send: " + playerName + " " + betData);

            String response = reader.readLine();
            System.out.println("Received: " + response);

            Record record = Report.get(playerName);
            record.addTotalTimeMillSec(System.currentTimeMillis() - begin);
            record.incrementRequestAmount(!response.isEmpty());

        } catch (IOException e) {
            Report.get(playerName).incrementRequestAmount(false);
            throw new RuntimeException();
        }
    }
}