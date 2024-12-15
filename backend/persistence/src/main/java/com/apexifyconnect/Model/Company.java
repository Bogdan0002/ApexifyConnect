package com.apexifyconnect.Model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("COMPANY")
@Getter @Setter
public class Company extends User {
    private String companyName;
    private String businessLicense;
    // Other fields specific to companies
}
