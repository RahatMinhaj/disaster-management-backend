package com.disaster.managementsystem.mapper;

import com.disaster.managementsystem.config.ConfigMapper;
import com.disaster.managementsystem.dto.DonationFundDto;
import com.disaster.managementsystem.entity.DonationFund;
import com.disaster.managementsystem.param.DonationFundParam;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = ConfigMapper.class)
public interface DonationFundMapper {
    void paramToEntity(DonationFundParam param, @MappingTarget DonationFund entity);

    DonationFundDto entityToDto(DonationFund donationFund);
}
