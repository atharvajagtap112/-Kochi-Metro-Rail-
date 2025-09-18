package com.atharva.kmrlinductionplanningapplication.service;


import com.atharva.kmrlinductionplanningapplication.entity.JobCard;
import com.atharva.kmrlinductionplanningapplication.entity.Train;
import com.atharva.kmrlinductionplanningapplication.enums.JobCardStatus;
import com.atharva.kmrlinductionplanningapplication.repository.JobCardRepository;
import com.atharva.kmrlinductionplanningapplication.repository.TrainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JobCardService {

    private final JobCardRepository jobCardRepository;
    private final TrainRepository trainRepository;

    public List<JobCard> getAllJobCards() {
        return jobCardRepository.findAll();
    }

    public Optional<JobCard> getJobCardById(String jobCardId) {
        return jobCardRepository.findById(jobCardId);
    }

    public List<JobCard> getAllOpenJobCards() {
        return jobCardRepository.findAllOpenJobCards();
    }

    public List<JobCard> getJobCardsByTrain(Long trainId) {
        Optional<Train> train = trainRepository.findById(trainId);
        if (train.isPresent()) {
            return jobCardRepository.findByTrain(train.get());
        }
        return List.of();
    }

    public List<JobCard> getOverdueJobCards() {
        return jobCardRepository.findOverdueJobCards(LocalDateTime.now());
    }

    public JobCard createJobCard(JobCard jobCard) {
        jobCard.setStatus(JobCardStatus.WAPPR); // Default status
        return jobCardRepository.save(jobCard);
    }

    public JobCard updateJobCard(JobCard jobCard) {
        return jobCardRepository.save(jobCard);
    }

    public boolean closeJobCard(String jobCardId) {
        Optional<JobCard> jobCardOpt = jobCardRepository.findById(jobCardId);
        if (jobCardOpt.isPresent()) {
            JobCard jobCard = jobCardOpt.get();
            jobCard.setStatus(JobCardStatus.CLOSED);
            jobCard.setActualEnd(LocalDateTime.now());
            jobCardRepository.save(jobCard);
            return true;
        }
        return false;
    }

    public boolean startJobCard(String jobCardId, String technicianId) {
        Optional<JobCard> jobCardOpt = jobCardRepository.findById(jobCardId);
        if (jobCardOpt.isPresent()) {
            JobCard jobCard = jobCardOpt.get();
            jobCard.setStatus(JobCardStatus.INPRG);
            jobCard.setActualStart(LocalDateTime.now());
            jobCard.setAssignedTechnicianId(technicianId);
            jobCardRepository.save(jobCard);
            return true;
        }
        return false;
    }

    public boolean completeJobCard(String jobCardId, String technicianNotes, Double laborHours) {
        Optional<JobCard> jobCardOpt = jobCardRepository.findById(jobCardId);
        if (jobCardOpt.isPresent()) {
            JobCard jobCard = jobCardOpt.get();
            jobCard.setStatus(JobCardStatus.COMP);
            jobCard.setActualEnd(LocalDateTime.now());
            jobCard.setTechnicianNotes(technicianNotes);
            jobCard.setLaborHours(laborHours);
            jobCardRepository.save(jobCard);
            return true;
        }
        return false;
    }

    public List<JobCard> getJobCardsByTechnician(String technicianId) {
        return jobCardRepository.findActiveJobCardsByTechnician(technicianId);
    }
}