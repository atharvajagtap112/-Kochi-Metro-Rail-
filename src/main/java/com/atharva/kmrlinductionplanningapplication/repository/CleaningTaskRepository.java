package com.atharva.kmrlinductionplanningapplication.repository;

import com.atharva.kmrlinductionplanningapplication.entity.CleaningTask;
import com.atharva.kmrlinductionplanningapplication.entity.JobCard;
import com.atharva.kmrlinductionplanningapplication.entity.Train;
import com.atharva.kmrlinductionplanningapplication.enums.CleaningType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CleaningTaskRepository extends JpaRepository<CleaningTask, String> {




    // ✅ Using trainId
    List<CleaningTask> findByTrainId(Long trainId);
    List<CleaningTask> findByStatus(CleaningTask.TaskStatus status);

    List<CleaningTask> findByCleaningType(CleaningType cleaningType);

    List<CleaningTask> findByAssignedTeamId(String teamId);

    @Query("SELECT ct FROM CleaningTask ct WHERE ct.scheduledStart BETWEEN :startTime AND :endTime")
    List<CleaningTask> findTasksScheduledBetween(@Param("startTime") LocalDateTime startTime,
                                                 @Param("endTime") LocalDateTime endTime);



    @Query("SELECT ct FROM CleaningTask ct WHERE ct.status = 'IN_PROGRESS'")
    List<CleaningTask> findActiveCleaningTasks();
}