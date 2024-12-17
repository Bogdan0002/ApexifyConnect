package com.apexifyconnect.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@DiscriminatorValue("COMPANY")
@Getter
@Setter
public class Company extends User {
    private String companyName;
    private String businessLicense;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<JobPost> jobPosts; // List of job posts created by the company
}
