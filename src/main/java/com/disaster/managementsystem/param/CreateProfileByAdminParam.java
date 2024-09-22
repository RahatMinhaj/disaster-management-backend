package com.disaster.managementsystem.param;

import com.disaster.managementsystem.enums.GenderType;
import lombok.*;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProfileByAdminParam extends BaseParam {

    private String name;
    private String mobileNo;
    private String address;
    private GenderType genderType;
    private LocalDate dateOfBirth;


    private String userName;
    private String password;
    private String email;
}
