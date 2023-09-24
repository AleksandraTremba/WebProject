package ee.taltech.iti0302.okapi.backend.entities;

import org.springframework.stereotype.Component;

@Component
public class Timer {
    private int timeRemaining;
    private boolean isRunning;

    public Timer() {
        this.timeRemaining = 0; // Initialize to 0 seconds
        this.isRunning = false;
    }

    public synchronized int getTimeRemaining() {
        return timeRemaining;
    }

    public synchronized boolean isRunning() {
        return isRunning;
    }

    // Start countdown in seconds
    public synchronized void start(int seconds) {
        if (!isRunning) {
            timeRemaining = seconds;
            isRunning = true;
            Thread countdownThread = new Thread(() -> {
                while (isRunning && timeRemaining > 0) {
                    try {
                        Thread.sleep(1000); // 1 second
                        timeRemaining--;
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                isRunning = false;
            });
            countdownThread.start();
        }
    }

    //curl -X POST http://localhost:8080/timer/stop
    public synchronized void stop() {
        isRunning = false;
    }

    public synchronized void reset() {
        timeRemaining = 0;
        isRunning = false;
    }

    public synchronized void update() {
        // Implement logic to decrement timer value
        if (isRunning && timeRemaining > 0) {
            timeRemaining--;
        }
    }

    public synchronized String formatTime() {
        int minutes = timeRemaining / 60;
        int seconds = timeRemaining % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
