package com.example.TheFit.diet.repository;

import com.example.TheFit.diet.domain.Diet;
import com.example.TheFit.diet.dto.DietResDto;
import com.example.TheFit.workoutlist.domain.WorkOutList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DietRepository extends JpaRepository<Diet, Long> {
    List<Diet> findByMemberId(Long id);
    List<Diet> findByMemberIdAndDietDate(Long memberId, LocalDate dietDate);

}