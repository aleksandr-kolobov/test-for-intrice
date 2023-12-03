package main.report;

import main.SocketClientApplication;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Report {

    private static int playerAmount = 0;

    private static Map<String, Record> records = new HashMap<>();

    public static void put(String key, Record record) {
        records.put(key, record);
    }

    public static Record get(String key) {
        return records.get(key);
    }

    public static void init(int amount) {
        playerAmount = amount;
        for (int i = 1; i <= playerAmount; i++) {
            Report.put("Player" + i, new Record());
        }
    }

    private static void delay(long l) {
        try {
            Thread.sleep(l);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void printInConsol() {
        delay(2000);
        System.out.println(scribble());
    }

    public static void saveInFile(String file) {
        delay(1000);
        try {
            FileWriter fw = new FileWriter(new File(file));
            fw.write(scribble());
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String scribble() {
        String str = "";
        for (int i = 1; i <= playerAmount; i++) {
            Record record = get("Player" + i);
            str += "Player" + i + " -> " +
                    "Success: " + record.getSuccessRequestAmount() +
                    "; Bad: " + record.getBadRequestAmount() +
                    "; MidTime: " + record.getTotalTimeMillSec()
                    /(record.getBadRequestAmount() + record.getSuccessRequestAmount())
                    + System.lineSeparator();
        }
        return str;
    }

}
