package com.example.TheFit.workoutlist.repository;

import com.example.TheFit.user.member.domain.Member;
import com.example.TheFit.workoutlist.domain.WorkOutList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkOutListRepository extends JpaRepository<WorkOutList, Long> {
    List<WorkOutList> findByMemberId(Long memberId);
    Optional<WorkOutList> findByMemberIdAndWorkOutDate(Long memberId, LocalDate workOutDate);
}
