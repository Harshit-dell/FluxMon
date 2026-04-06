package com.harshitsahu.FluxMonitor;

import Resources.HeaderResources;
import Resources.Resource;
import RandomObjects.PidValues;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ProcessService {

    private final HashMap<Integer, String> users = new HashMap<>();
    private List<PidValues> latestProcesses = new ArrayList<>();
    private long prevCpu = -1;

    public ProcessService() {
        loadUsers();
    }

    private void loadUsers() {
        try {
            Files.readAllLines(Path.of("/etc/passwd")).forEach(line -> {
                String[] parts = line.split(":");
                users.put(Integer.parseInt(parts[2]), parts[0]);
            });
        } catch (IOException e) {
            System.err.println("Could not load users: " + e.getMessage());
        }
    }

    @Scheduled(fixedDelay = 2000)
    public void refresh() {
        try {
            long currCpu = new HeaderResources().totalCpu();
            if (prevCpu == -1) {
                prevCpu = currCpu;
                return;
            }
            latestProcesses = new Resource(users)
                    .getPidsValues(currCpu, prevCpu);
            prevCpu = currCpu;
        } catch (Exception e) {
            System.err.println("Refresh failed: " + e.getMessage());
        }
    }

    public List<PidValues> getLatest() {
        return latestProcesses;
    }
}