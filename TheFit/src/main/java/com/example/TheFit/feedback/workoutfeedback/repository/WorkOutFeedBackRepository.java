package com.example.TheFit.feedback.workoutfeedback.repository;



import com.example.TheFit.feedback.workoutfeedback.domain.WorkOutFeedBack;
import com.example.TheFit.workoutlist.domain.WorkOutList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface WorkOutFeedBackRepository extends JpaRepository<WorkOutFeedBack, Long> {
   Optional<WorkOutFeedBack> findByWorkOutListId(Long workoutListId);
}