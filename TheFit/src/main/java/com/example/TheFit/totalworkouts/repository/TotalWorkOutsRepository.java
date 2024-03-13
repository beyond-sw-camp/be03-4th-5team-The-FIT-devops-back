package com.example.TheFit.totalworkouts.repository;

import com.example.TheFit.totalworkouts.domain.TotalWorkOuts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TotalWorkOutsRepository extends JpaRepository<TotalWorkOuts, Long> {
}
