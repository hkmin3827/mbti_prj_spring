package com.whatslovermbti.mbti_prj.repository;

import com.whatslovermbti.mbti_prj.entity.AppSeedHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeedHistoryRepository extends JpaRepository<AppSeedHistory, String> {
}
