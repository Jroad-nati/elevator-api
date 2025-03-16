package com.elevator.elevator.Service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LogService {

    private final List<String> logs;

    public LogService() {
        logs = new ArrayList<>();
    }

    public void addLogs(String log) {
        logs.add(log);
    }

    public List<String> getLogs() {
        return logs;
    }

    public void clearLogs() {
        logs.clear();
    }
}
