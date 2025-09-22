package com.atharva.kmrlinductionplanningapplication.controller;


import com.atharva.kmrlinductionplanningapplication.entity.JobCard;
import com.atharva.kmrlinductionplanningapplication.service.JobCardService;
;
import jakarta.transaction.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/jobcards")

@CrossOrigin(origins = "*")
public class JobCardController {
    @Autowired
    private  JobCardService jobCardService;

    @GetMapping
    public ResponseEntity<List<JobCard>> getAllJobCards() {
        List<JobCard> jobCards = jobCardService.getAllJobCards();
        return ResponseEntity.ok(jobCards);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobCard> getJobCardById(@PathVariable String id) {
        Optional<JobCard> jobCard = jobCardService.getJobCardById(id);
        return jobCard.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/open")
    public ResponseEntity<List<JobCard>> getAllOpenJobCards() {
        List<JobCard> openJobCards = jobCardService.getAllOpenJobCards();
        return ResponseEntity.ok(openJobCards);
    }

    @GetMapping("/critical")
    public ResponseEntity<List<JobCard>> getCriticalOpenJobCards() {
        List<JobCard> criticalJobCards = jobCardService.getCriticalOpenJobCards();
        return ResponseEntity.ok(criticalJobCards);
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<JobCard>> getOverdueJobCards() {
        List<JobCard> overdueJobCards = jobCardService.getOverdueJobCards();
        return ResponseEntity.ok(overdueJobCards);
    }

    @GetMapping("/train/{trainId}")
    public ResponseEntity<List<JobCard>> getJobCardsByTrain(@PathVariable Long trainId) {
        List<JobCard> jobCards = jobCardService.getJobCardsByTrain(trainId);
        return ResponseEntity.ok(jobCards);
    }

    @GetMapping("/trainset/{trainsetId}")
    public ResponseEntity<List<JobCard>> getJobCardsByTrainsetId(@PathVariable String trainsetId) {
        List<JobCard> jobCards = jobCardService.getJobCardsByTrainsetId(trainsetId);
        return ResponseEntity.ok(jobCards);
    }

    @GetMapping("/team/{teamName}")
    public ResponseEntity<List<JobCard>> getJobCardsByTeam(@PathVariable String teamName) {
        List<JobCard> jobCards = jobCardService.getJobCardsByTeam(teamName);
        return ResponseEntity.ok(jobCards);
    }

    @PostMapping
    public ResponseEntity<JobCard> createJobCard(@RequestBody JobCard jobCard) {
        JobCard savedJobCard = jobCardService.createJobCard(jobCard);
        return ResponseEntity.ok(savedJobCard);
    }

    @PostMapping("/create_all")
    public ResponseEntity<List<JobCard>> createAllJobCard(@RequestBody List<JobCard> jobCards) {
        if (jobCards.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        for (JobCard cards:jobCards){
                 jobCardService.createJobCard(cards);}
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Endpoint to receive job card data from IBM Maximo or external systems
    @PostMapping("/external")
    public ResponseEntity<JobCard> receiveJobCardFromExternalSystem(@RequestBody JobCard jobCard) {
        JobCard savedJobCard = jobCardService.receiveJobCardFromExternalSystem(jobCard);
        return ResponseEntity.ok(savedJobCard);
    }

    // Bulk endpoint to receive multiple job cards from external systems
    @PostMapping("/external/bulk")
    public ResponseEntity<List<JobCard>> receiveJobCardsFromExternalSystem(@RequestBody List<JobCard> jobCards) {
        List<JobCard> savedJobCards = jobCardService.receiveJobCardsFromExternalSystem(jobCards);
        return ResponseEntity.ok(savedJobCards);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobCard> updateJobCard(@PathVariable String id, @RequestBody JobCard jobCard) {
        jobCard.setJobCardId(id);
        JobCard updatedJobCard = jobCardService.updateJobCard(jobCard);
        return ResponseEntity.ok(updatedJobCard);
    }

    @PutMapping("/{id}/close")
    public ResponseEntity<String> closeJobCard(@PathVariable String id) {
        boolean success = jobCardService.closeJobCard(id);
        if (success) {
            return ResponseEntity.ok("Job card closed successfully");
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/start")
    public ResponseEntity<String> startJobCard(@PathVariable String id,
                                               @RequestBody Map<String, String> request) {
        String teamName = request.get("teamName"); // Changed from technicianId
        boolean success = jobCardService.startJobCard(id, teamName);
        if (success) {
            return ResponseEntity.ok("Job card started successfully");
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<String> completeJobCard(@PathVariable String id,
                                                  @RequestBody Map<String, Object> request) {
        String details = (String) request.get("details"); // Changed from technicianNotes
        Double laborHours = (Double) request.get("laborHoursLogged");
        boolean success = jobCardService.completeJobCard(id, details, laborHours);
        if (success) {
            return ResponseEntity.ok("Job card completed successfully");
        }
        return ResponseEntity.notFound().build();
    }
}