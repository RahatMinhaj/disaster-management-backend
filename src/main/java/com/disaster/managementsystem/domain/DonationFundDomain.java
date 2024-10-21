package com.disaster.managementsystem.domain;

import com.disaster.managementsystem.component.error.exception.DataNotFoundException;
import com.disaster.managementsystem.domain.core.BaseDomain;
import com.disaster.managementsystem.dto.DayWiseDonationSummaryDto;
import com.disaster.managementsystem.dto.DonationFundDto;
import com.disaster.managementsystem.entity.DonationFund;
import com.disaster.managementsystem.mapper.DonationFundMapper;
import com.disaster.managementsystem.param.DonationFundParam;
import com.disaster.managementsystem.repository.DonationFundRepository;
import com.disaster.managementsystem.repository.core.CustomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@Slf4j
public class DonationFundDomain extends BaseDomain<DonationFund, UUID> {
    private final DonationFundRepository donationFundRepository;
    private final DonationFundMapper donationFundMapper;

    public DonationFundDomain(CustomRepository<DonationFund, UUID> repository, DonationFundRepository donationFundRepository, DonationFundMapper donationFundMapper) {
        super(repository);
        this.donationFundRepository = donationFundRepository;
        this.donationFundMapper = donationFundMapper;
    }

    public Optional<DonationFundDto> getById(UUID id) {
        Optional<DonationFund> donationFund = findById(id);
        return Optional.ofNullable(donationFundMapper.entityToDto(donationFund.orElse(null)));
    }

    @Transactional
    public Optional<DonationFundDto> create(DonationFundParam param) throws Exception {
        return Optional.ofNullable(entityToDto(createReturnEntity(param)));
    }

    @Transactional
    public Optional<DonationFundDto> update(DonationFundParam param) throws Exception {
        return Optional.ofNullable(entityToDto(updateReturnEntity(param)));
    }

    @Transactional
    public void delete(UUID id) throws Exception {
        deleteById(id);
    }

    private DonationFund updateReturnEntity(DonationFundParam param) throws Exception {
        Optional<DonationFund> educationalQualification = findById(param.getId());
        if (educationalQualification.isEmpty()) {
            throw new DataNotFoundException(getMessage("data.not.found"));
        }
        return updateByEntity(paramToEntity(param, educationalQualification.get()));
    }


    @Transactional
    private DonationFund createReturnEntity(DonationFundParam param) throws Exception {
        DonationFund donationFund = paramToEntity(param, new DonationFund());
        return createByEntity(donationFund);
    }

    public List<DonationFundDto> getAll(Specification<DonationFund> specification, Sort sort) {
        return donationFundRepository.findAll(specification, sort).stream().map(entity -> {
            try {
                log.debug("getAll {}", entity);
                return entityToDto(entity);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }

    public Page<DonationFundDto> getAll(Specification<DonationFund> specification, Pageable pageable) {
        return donationFundRepository.findAll(specification, pageable).map(entity -> {
            try {
                log.debug("getAll {}", entity);
                return entityToDto(entity);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private DonationFundDto entityToDto(DonationFund donationFund) {
        DonationFundDto educationalDegreeDto = donationFundMapper.entityToDto(donationFund);
        return educationalDegreeDto;
    }

    private DonationFund paramToEntity(DonationFundParam param, DonationFund donationFund) {
        donationFundMapper.paramToEntity(param, donationFund);
        return donationFund;
    }

    private List<DonationFund> lastSevenDaysDonation() {
        return donationFundRepository.findByCreatedAtGreaterThanEqual(LocalDateTime.now().minusDays(7));
    }

    public List<DayWiseDonationSummaryDto> findLast7DayWiseDonation() {
        List<DonationFund> funds = donationFundRepository.findByCreatedAtGreaterThanEqual(LocalDateTime.now().minusDays(7));
        Map<LocalDate, Double> dailyDonations = funds.stream()
                .collect(Collectors.groupingBy(
                        fund -> fund.getCreatedAt().toLocalDate(),
                        Collectors.summingDouble(DonationFund::getDonationAmount)
                ));
        return dailyDonations.entrySet().stream()
                .map(entry -> new DayWiseDonationSummaryDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
}
