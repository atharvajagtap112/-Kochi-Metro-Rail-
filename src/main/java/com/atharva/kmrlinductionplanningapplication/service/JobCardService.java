package com.atharva.kmrlinductionplanningapplication.service;


import com.atharva.kmrlinductionplanningapplication.entity.JobCard;
import com.atharva.kmrlinductionplanningapplication.entity.Train;
import com.atharva.kmrlinductionplanningapplication.enums.JobCardStatus;
import com.atharva.kmrlinductionplanningapplication.enums.TrainStatus;
import com.atharva.kmrlinductionplanningapplication.repository.JobCardRepository;
import com.atharva.kmrlinductionplanningapplication.repository.TrainRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service

public class JobCardService {

    private  JobCardRepository jobCardRepository;
    private  TrainRepository trainRepository;

    public JobCardService(JobCardRepository jobCardRepository, TrainRepository trainRepository) {
        this.jobCardRepository = jobCardRepository;
        this.trainRepository = trainRepository;
    }

    public List<JobCard> getAllJobCards() {
        return jobCardRepository.findAll();
    }

    public Optional<JobCard> getJobCardById(String jobCardId) {
        return jobCardRepository.findById(jobCardId);
    }

    public List<JobCard> getAllOpenJobCards() {
        return jobCardRepository.findAllOpenJobCards();
    }

    public List<JobCard> getCriticalOpenJobCards() {
        return jobCardRepository.findCriticalOpenJobCards();
    }

    public List<JobCard> getJobCardsByTrain(java.lang.Long trainId) {
        Optional<Train> train = trainRepository.findById(trainId);
        if (train.isPresent()) {
            return jobCardRepository.findByTrainId(train.get().getTrainId());
        }
        return List.of();
    }

    public List<JobCard> getJobCardsByTrainsetId(String trainsetId) {
        return jobCardRepository.findByTrainsetId(trainsetId);
    }

    public List<JobCard> getOverdueJobCards() {
        return jobCardRepository.findOverdueJobCards(LocalDateTime.now());
    }

    public JobCard createJobCard(JobCard jobCard) {
        jobCard.setStatus(JobCardStatus.OPEN); // Default status
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

            Train train=trainRepository.getTrainByTrainId(jobCardOpt.get().getTrainId());
            train.setStatus(TrainStatus.ACTIVE);
            return true;
        }
        return false;
    }

    public boolean startJobCard(String jobCardId, String teamName) {
        Optional<JobCard> jobCardOpt = jobCardRepository.findById(jobCardId);

        if (jobCardOpt.isPresent()) {
            JobCard jobCard = jobCardOpt.get();
            jobCard.setStatus(JobCardStatus.INPRG);
            jobCard.setActualStart(LocalDateTime.now());
            jobCard.setAssignedTo(teamName);
            jobCardRepository.save(jobCard);


            Train train=trainRepository.getTrainByTrainId(jobCardOpt.get().getTrainId());
             train.setStatus(TrainStatus.MAINTENANCE);

            return true;
        }
        return false;
    }

    public boolean completeJobCard(String jobCardId, String details, Double laborHours) {
        Optional<JobCard> jobCardOpt = jobCardRepository.findById(jobCardId);
        if (jobCardOpt.isPresent()) {
            JobCard jobCard = jobCardOpt.get();
            jobCard.setStatus(JobCardStatus.COMP);
            jobCard.setActualEnd(LocalDateTime.now());
            jobCard.setDetails(details);
            jobCard.setLaborHoursLogged(laborHours);
            jobCardRepository.save(jobCard);


            return true;
        }
        return false;
    }

    public List<JobCard> getJobCardsByTeam(String teamName) {
        return jobCardRepository.findActiveJobCardsByTeam(teamName);
    }

    // Method to receive job card data from IBM Maximo (or similar external system)
    public JobCard receiveJobCardFromExternalSystem(JobCard externalJobCard) {
        // Map trainset ID to internal train ID if needed
        Optional<Train> train = trainRepository.findByTrainNumber(externalJobCard.getTrainsetId());
        if (train.isPresent()) {
            externalJobCard.setTrainId(train.get().getTrainId());
        }

        return jobCardRepository.save(externalJobCard);
    }

    // Bulk import from external system
    public List<JobCard> receiveJobCardsFromExternalSystem(List<JobCard> externalJobCards) {
        return externalJobCards.stream()
                .map(this::receiveJobCardFromExternalSystem)
                .toList();
    }
}