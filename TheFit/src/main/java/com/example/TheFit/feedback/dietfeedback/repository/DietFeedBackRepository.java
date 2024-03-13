package com.example.TheFit.feedback.dietfeedback.repository;

import com.example.TheFit.feedback.dietfeedback.domain.DietFeedBack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DietFeedBackRepository extends JpaRepository<DietFeedBack, Long> {
    Optional<DietFeedBack> findByUploadDateAndMemberId(String uploadDate,Long memberId);

    Optional<DietFeedBack> findByTrainerId(Long trainerId);
}