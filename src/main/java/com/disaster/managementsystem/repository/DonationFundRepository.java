package com.disaster.managementsystem.repository;

import com.disaster.managementsystem.dto.DayWiseDonationSummaryDto;
import com.disaster.managementsystem.entity.DonationFund;
import com.disaster.managementsystem.repository.core.CustomRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface DonationFundRepository extends CustomRepository<DonationFund, UUID> {

    List<DonationFund> findByCreatedAtGreaterThanEqual(LocalDateTime startDate);

}
