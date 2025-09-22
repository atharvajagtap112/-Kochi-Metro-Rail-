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

    List<JobCard> findByTrainId(Long trainId);

    // ✅ Removed - no longer needed since we use trainId
    // List<JobCard> findByTrainAndStatus(Train train, JobCardStatus status);

    // ✅ Added replacement method using trainId
    List<JobCard> findByTrainIdAndStatus(Long trainId, JobCardStatus status);

    List<JobCard> findByPriority(Priority priority);

    List<JobCard> findByAssignedTo(String teamName);

    @Query("SELECT jc FROM JobCard jc WHERE jc.status != 'CLOSED'")
    List<JobCard> findAllOpenJobCards();

    @Query("SELECT jc FROM JobCard jc WHERE jc.targetCompletionDate < :currentTime AND jc.status != 'CLOSED'")
    List<JobCard> findOverdueJobCards(@Param("currentTime") LocalDateTime currentTime);

    // ✅ Fixed: Just use trainId directly
    @Query("SELECT jc FROM JobCard jc WHERE jc.trainId = :trainId AND jc.status != 'CLOSED'")
    List<JobCard> findOpenJobCardsByTrainId(@Param("trainId") Long trainId);

    @Query("SELECT jc FROM JobCard jc WHERE jc.assignedTo = :teamName AND jc.status = 'IN_PROGRESS'")
    List<JobCard> findActiveJobCardsByTeam(@Param("teamName") String teamName);

    @Query("SELECT jc FROM JobCard jc WHERE jc.trainsetId = :trainsetId")
    List<JobCard> findByTrainsetId(@Param("trainsetId") String trainsetId);

    @Query("SELECT jc FROM JobCard jc WHERE jc.priority = 'CRITICAL' AND jc.status != 'CLOSED'")
    List<JobCard> findCriticalOpenJobCards();
}