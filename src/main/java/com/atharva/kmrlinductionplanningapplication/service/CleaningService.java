package com.atharva.kmrlinductionplanningapplication.service;

import com.atharva.kmrlinductionplanningapplication.entity.CleaningTask;
import com.atharva.kmrlinductionplanningapplication.entity.Train;
import com.atharva.kmrlinductionplanningapplication.enums.CleaningType;
import com.atharva.kmrlinductionplanningapplication.repository.CleaningTaskRepository;
import com.atharva.kmrlinductionplanningapplication.repository.TrainRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CleaningService {
    @Autowired
    private CleaningTaskRepository cleaningTaskRepository;
    @Autowired
    private TrainRepository trainRepository;

    public List<CleaningTask> getAllCleaningTasks() {
        return cleaningTaskRepository.findAll();
    }

    public Optional<CleaningTask> getCleaningTaskById(String taskId) {
        return cleaningTaskRepository.findById(taskId);
    }

    // ✅ Fixed to use trainId
    public List<CleaningTask> getCleaningTasksByTrain(Long trainId) {
        return cleaningTaskRepository.findByTrainId(trainId);
    }

    public List<CleaningTask> getActiveCleaningTasks() {
        return cleaningTaskRepository.findActiveCleaningTasks();
    }

    public List<CleaningTask> getTasksScheduledForDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);
        return cleaningTaskRepository.findTasksScheduledBetween(startOfDay, endOfDay);
    }

    public List<CleaningTask> getTodaysCleaningTasks() {
        return getTasksScheduledForDate(LocalDate.now());
    }

    public List<Long> getTrainsNeedingCleaning() {
        LocalDateTime cleaningDueTime = LocalDateTime.now().minusHours(24);
        return trainRepository.findTrainsRequiringCleaning(cleaningDueTime)
                .stream()
                .map(Train::getTrainId)
                .toList();
    }

    public CleaningTask createCleaningTask(CleaningTask cleaningTask) {
        if (cleaningTask.getTaskId() == null || cleaningTask.getTaskId().isEmpty()) {
            cleaningTask.setTaskId(generateTaskId());
        }
        cleaningTask.setStatus(CleaningTask.TaskStatus.SCHEDULED);
        return cleaningTaskRepository.save(cleaningTask);
    }

    // ✅ Fixed to use trainId
    public CleaningTask scheduleCleaningForTrain(Long trainId, String cleaningTypeStr, String assignedTeam) {
        Optional<Train> trainOpt = trainRepository.findById(trainId);
        if (trainOpt.isEmpty()) {
            throw new RuntimeException("Train not found with ID: " + trainId);
        }

        CleaningType cleaningType = CleaningType.valueOf(cleaningTypeStr.toUpperCase());

        CleaningTask task = new CleaningTask();
        task.setTaskId(generateTaskId());
        task.setTrainId(trainId); // ✅ Using trainId directly
        task.setCleaningType(cleaningType);
        task.setAssignedTeamId(assignedTeam);
        task.setStatus(CleaningTask.TaskStatus.SCHEDULED);

        LocalDateTime tonight = LocalDate.now().atTime(22, 0);
        LocalDateTime endTime = tonight.plusHours(4);

        task.setScheduledStart(tonight);
        task.setScheduledEnd(endTime);
        task.setRemarks("Scheduled daily cleaning");

        return cleaningTaskRepository.save(task);
    }

    public List<CleaningTask> scheduleDailyCleaning() {
        List<Long> trainsNeedingCleaning = getTrainsNeedingCleaning();
        List<CleaningTask> scheduledTasks = new ArrayList<>();

        String[] cleaningTeams = {"TEAM-1", "TEAM-2", "TEAM-3", "TEAM-4"};
        int teamIndex = 0;

        for (Long trainId : trainsNeedingCleaning) {
            String assignedTeam = cleaningTeams[teamIndex % cleaningTeams.length];

            try {
                CleaningTask task = scheduleCleaningForTrain(trainId, "INTERIOR", assignedTeam);
                scheduledTasks.add(task);
                teamIndex++;
            } catch (Exception e) {
                System.err.println("Failed to schedule cleaning for train " + trainId + ": " + e.getMessage());
            }
        }

        return scheduledTasks;
    }

    public CleaningTask updateCleaningTask(CleaningTask cleaningTask) {
        return cleaningTaskRepository.save(cleaningTask);
    }

    public boolean startCleaningTask(String taskId) {
        Optional<CleaningTask> taskOpt = cleaningTaskRepository.findById(taskId);
        if (taskOpt.isPresent()) {
            CleaningTask task = taskOpt.get();
            task.setStatus(CleaningTask.TaskStatus.IN_PROGRESS);
            task.setActualStart(LocalDateTime.now());
            cleaningTaskRepository.save(task);
            return true;
        }
        return false;
    }

    // ✅ Fixed to use trainId
    public boolean completeCleaningTask(String taskId, String remarks) {
        Optional<CleaningTask> taskOpt = cleaningTaskRepository.findById(taskId);
        if (taskOpt.isPresent()) {
            CleaningTask task = taskOpt.get();
            task.setStatus(CleaningTask.TaskStatus.DONE);
            task.setActualEnd(LocalDateTime.now());
            task.setRemarks(remarks);

            // Update train's last cleaning time
            Optional<Train> trainOpt = trainRepository.findById(task.getTrainId());
            if (trainOpt.isPresent()) {
                Train train = trainOpt.get();
                train.setLastCleaningDateTime(LocalDateTime.now());
                trainRepository.save(train);
            }

            cleaningTaskRepository.save(task);
            return true;
        }
        return false;
    }

    public boolean deleteCleaningTask(String taskId) {
        if (cleaningTaskRepository.existsById(taskId)) {
            cleaningTaskRepository.deleteById(taskId);
            return true;
        }
        return false;
    }

    private String generateTaskId() {
        LocalDate today = LocalDate.now();
        String dateStr = today.toString().replace("-", "");
        String uuid = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        return "CLN-" + dateStr + "-" + uuid;
    }
}