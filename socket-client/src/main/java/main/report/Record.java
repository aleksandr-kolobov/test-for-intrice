package main.report;

public class Record {
    private int successRequestAmount=0;
    private int badRequestAmount=0;
    private long totalTimeMillSec=0;

    public void incrementRequestAmount(boolean success) {
        if (success) {
            successRequestAmount++;
        } else {
            badRequestAmount++;
        }

    }

    public void addTotalTimeMillSec(long timeMillSec) {
        totalTimeMillSec += timeMillSec;
    }

    public int getSuccessRequestAmount() {
        return successRequestAmount;
    }

    public int getBadRequestAmount() {
        return badRequestAmount;
    }

    public long getTotalTimeMillSec() {
        return totalTimeMillSec;
    }

}
