package com.harshitsahu.FluxMonitor;

import RandomObjects.PidValues;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/processes")
public class ProcessController {

    private final ProcessService processService;

    public ProcessController(ProcessService processService) {
        this.processService = processService;
    }

    @GetMapping
    public ResponseEntity<List<PidValues>> getAllProcesses() {
        return ResponseEntity.ok(processService.getLatest());
    }

    @GetMapping("/top")
    public ResponseEntity<List<PidValues>> getTop(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(
                processService.getLatest()
                        .stream()
                        .sorted(Comparator.comparingDouble(PidValues::getCpuUsage).reversed())
                        .limit(limit)
                        .toList()
        );
    }
}