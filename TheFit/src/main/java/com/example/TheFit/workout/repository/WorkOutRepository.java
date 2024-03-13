package com.example.TheFit.workout.repository;

import com.example.TheFit.workout.domain.WorkOut;
import com.example.TheFit.workoutlist.domain.WorkOutList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WorkOutRepository extends JpaRepository<WorkOut, Long> {

}
