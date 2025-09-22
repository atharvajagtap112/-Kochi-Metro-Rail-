package com.atharva.kmrlinductionplanningapplication.repository;

import com.atharva.kmrlinductionplanningapplication.entity.Train;
import com.atharva.kmrlinductionplanningapplication.entity.TripHistory;
import com.atharva.kmrlinductionplanningapplication.enums.TrainStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TrainRepository extends JpaRepository<Train, java.lang.Long> {

    Optional<Train> findByTrainNumber(String trainNumber);

    List<Train> findByStatus(TrainStatus status);

    List<Train> findByDepotLocation(String depotLocation);




    // ✅ Fixed: Compare trainId with trainId (both Long)
    @Query("SELECT t FROM Train t WHERE t.status = :status AND " +
            "NOT EXISTS (SELECT jc FROM JobCard jc WHERE jc.trainId = t.trainId AND jc.status != 'CLOSED')")
    List<Train> findAvailableTrainsWithNoOpenJobCards(@Param("status") TrainStatus status);

    @Query("SELECT t FROM Train t WHERE t.currentOdometer - t.odometerAtLastMaintenance >= t.maintenanceInterval")
    List<Train> findTrainsRequiringMaintenance();

    @Query("SELECT t FROM Train t WHERE t.lastCleaningDateTime < :cleaningDueTime")
    List<Train> findTrainsRequiringCleaning(@Param("cleaningDueTime") LocalDateTime cleaningDueTime);

    // ✅ Fixed: Compare trainId with trainId (both Long)
    @Query("SELECT t FROM Train t WHERE EXISTS " +
            "(SELECT jc FROM JobCard jc WHERE jc.trainId = t.trainId AND jc.status != 'CLOSED')")
    List<Train> findTrainsWithOpenJobCards();






    @Query("SELECT th FROM TripHistory th WHERE th.tripStartTime BETWEEN :startTime AND :endTime")
    List<TripHistory> findTripsBetween(@Param("startTime") LocalDateTime startTime,
                                       @Param("endTime") LocalDateTime endTime);

    Train getTrainByTrainId(Long trainId);
}
