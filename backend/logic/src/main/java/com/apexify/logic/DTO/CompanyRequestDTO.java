package com.apexify.logic.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter @Setter
public class CompanyRequestDTO {
    // No-argument constructor
    public CompanyRequestDTO() {}

    // Constructor with arguments
    public CompanyRequestDTO(String email, String password, String companyName, String businessLicense) {
        this.email = email;
        this.password = password;
        this.companyName = companyName;
        this.businessLicense = businessLicense;
    }
    private String email;
    private String password;
    private String companyName;
    private String businessLicense;
}