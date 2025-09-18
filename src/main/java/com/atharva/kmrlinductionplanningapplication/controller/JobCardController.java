package com.atharva.kmrlinductionplanningapplication.controller;


import com.atharva.kmrlinductionplanningapplication.entity.JobCard;
import com.atharva.kmrlinductionplanningapplication.service.JobCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/jobcards")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class JobCardController {

    private final JobCardService jobCardService;

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

    @GetMapping("/technician/{technicianId}")
    public ResponseEntity<List<JobCard>> getJobCardsByTechnician(@PathVariable String technicianId) {
        List<JobCard> jobCards = jobCardService.getJobCardsByTechnician(technicianId);
        return ResponseEntity.ok(jobCards);
    }

    @PostMapping
    public ResponseEntity<JobCard> createJobCard(@RequestBody JobCard jobCard) {
        JobCard savedJobCard = jobCardService.createJobCard(jobCard);
        return ResponseEntity.ok(savedJobCard);
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
        String technicianId = request.get("technicianId");
        boolean success = jobCardService.startJobCard(id, technicianId);
        if (success) {
            return ResponseEntity.ok("Job card started successfully");
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<String> completeJobCard(@PathVariable String id,
                                                  @RequestBody Map<String, Object> request) {
        String technicianNotes = (String) request.get("technicianNotes");
        Double laborHours = (Double) request.get("laborHours");
        boolean success = jobCardService.completeJobCard(id, technicianNotes, laborHours);
        if (success) {
            return ResponseEntity.ok("Job card completed successfully");
        }
        return ResponseEntity.notFound().build();
    }
}