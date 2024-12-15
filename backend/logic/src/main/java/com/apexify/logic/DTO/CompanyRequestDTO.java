package com.apexify.logic.DTO;

import lombok.Data;

@Data
public class CompanyRequestDTO {
    private String email;
    private String password;
    private String companyName;
    private String businessLicense;
}
