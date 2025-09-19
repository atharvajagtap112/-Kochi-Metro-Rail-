package com.atharva.kmrlinductionplanningapplication.repository;



import com.atharva.kmrlinductionplanningapplication.entity.JobCard;
import com.atharva.kmrlinductionplanningapplication.entity.Train;
import com.atharva.kmrlinductionplanningapplication.enums.JobCardStatus;
import com.atharva.kmrlinductionplanningapplication.enums.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JobCardRepository extends JpaRepository<JobCard, String> {

    List<JobCard> findByStatus(JobCardStatus status);

    List<JobCard> findByTrain(Train train);

    List<JobCard> findByTrainAndStatus(Train train, JobCardStatus status);

    List<JobCard> findByPriority(Priority priority);

    List<JobCard> findByAssignedTo(String teamName); // Changed from technician to team

    @Query("SELECT jc FROM JobCard jc WHERE jc.status != 'CLOSED'")
    List<JobCard> findAllOpenJobCards();

    @Query("SELECT jc FROM JobCard jc WHERE jc.targetCompletionDate < :currentTime AND jc.status != 'CLOSED'")
    List<JobCard> findOverdueJobCards(@Param("currentTime") LocalDateTime currentTime);

    @Query("SELECT jc FROM JobCard jc WHERE jc.train.trainId = :trainId AND jc.status != 'CLOSED'")
    List<JobCard> findOpenJobCardsByTrainId(@Param("trainId") Long trainId);

    @Query("SELECT jc FROM JobCard jc WHERE jc.assignedTo = :teamName AND jc.status = 'INPRG'")
    List<JobCard> findActiveJobCardsByTeam(@Param("teamName") String teamName);

    // New query to find job cards by trainset ID (external system ID)
    @Query("SELECT jc FROM JobCard jc WHERE jc.trainsetId = :trainsetId")
    List<JobCard> findByTrainsetId(@Param("trainsetId") String trainsetId);

    // Critical job cards that are blocking train service
    @Query("SELECT jc FROM JobCard jc WHERE jc.priority = 'CRITICAL' AND jc.status != 'CLOSED'")
    List<JobCard> findCriticalOpenJobCards();
}