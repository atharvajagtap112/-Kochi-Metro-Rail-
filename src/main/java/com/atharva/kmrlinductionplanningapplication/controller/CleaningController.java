package com.atharva.kmrlinductionplanningapplication.controller;


import com.atharva.kmrlinductionplanningapplication.entity.CleaningTask;
import com.atharva.kmrlinductionplanningapplication.service.CleaningService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/cleaning")

@CrossOrigin(origins = "*")
public class CleaningController {
    @Autowired
    private  CleaningService cleaningService;

    @GetMapping
    public ResponseEntity<List<CleaningTask>> getAllCleaningTasks() {
        List<CleaningTask> tasks = cleaningService.getAllCleaningTasks();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<CleaningTask> getCleaningTaskById(@PathVariable String taskId) {
        Optional<CleaningTask> task = cleaningService.getCleaningTaskById(taskId);
        return task.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/train/{trainId}")
    public ResponseEntity<List<CleaningTask>> getCleaningTasksByTrain(@PathVariable Long trainId) {
        List<CleaningTask> tasks = cleaningService.getCleaningTasksByTrain(trainId);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/active")
    public ResponseEntity<List<CleaningTask>> getActiveCleaningTasks() {
        List<CleaningTask> activeTasks = cleaningService.getActiveCleaningTasks();
        return ResponseEntity.ok(activeTasks);
    }

    @GetMapping("/scheduled/{date}")
    public ResponseEntity<List<CleaningTask>> getTasksScheduledForDate(@PathVariable String date) {
        LocalDate scheduleDate = LocalDate.parse(date);
        List<CleaningTask> tasks = cleaningService.getTasksScheduledForDate(scheduleDate);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/today")
    public ResponseEntity<List<CleaningTask>> getTodaysCleaningTasks() {
        List<CleaningTask> todaysTasks = cleaningService.getTodaysCleaningTasks();
        return ResponseEntity.ok(todaysTasks);
    }

    @GetMapping("/due")
    public ResponseEntity<List<Long>> getTrainsNeedingCleaning() {
        List<Long> trainsNeedingCleaning = cleaningService.getTrainsNeedingCleaning();
        return ResponseEntity.ok(trainsNeedingCleaning);
    }

    @GetMapping("/schedule-daily")
    public ResponseEntity<List<CleaningTask>> scheduleDailyCleaning() {
        List<CleaningTask> scheduledTasks = cleaningService.scheduleDailyCleaning();
        return ResponseEntity.ok(scheduledTasks);
    }

    @PostMapping
    public ResponseEntity<CleaningTask> createCleaningTask(@RequestBody CleaningTask cleaningTask) {
        CleaningTask savedTask = cleaningService.createCleaningTask(cleaningTask);
        return ResponseEntity.ok(savedTask);
    }

    @PostMapping("/schedule-for-train/{trainId}")
    public ResponseEntity<CleaningTask> scheduleCleaningForTrain(@PathVariable Long trainId,
                                                                 @RequestBody Map<String, Object> request) {
        String cleaningType = (String) request.get("cleaningType");
        String assignedTeam = (String) request.get("assignedTeam");

        CleaningTask task = cleaningService.scheduleCleaningForTrain(trainId, cleaningType, assignedTeam);
        return ResponseEntity.ok(task);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<CleaningTask> updateCleaningTask(@PathVariable String taskId,
                                                           @RequestBody CleaningTask cleaningTask) {
        cleaningTask.setTaskId(taskId);
        CleaningTask updatedTask = cleaningService.updateCleaningTask(cleaningTask);
        return ResponseEntity.ok(updatedTask);
    }

    @PutMapping("/{taskId}/start")
    public ResponseEntity<String> startCleaningTask(@PathVariable String taskId) {
        boolean success = cleaningService.startCleaningTask(taskId);
        if (success) {
            return ResponseEntity.ok("Cleaning task started successfully");
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{taskId}/complete")
    public ResponseEntity<String> completeCleaningTask(@PathVariable String taskId,
                                                       @RequestBody Map<String, String> request) {
        String remarks = request.get("remarks");
        boolean success = cleaningService.completeCleaningTask(taskId, remarks);
        if (success) {
            return ResponseEntity.ok("Cleaning task completed successfully");
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<String> deleteCleaningTask(@PathVariable String taskId) {
        boolean success = cleaningService.deleteCleaningTask(taskId);
        if (success) {
            return ResponseEntity.ok("Cleaning task deleted successfully");
        }
        return ResponseEntity.notFound().build();
    }
}