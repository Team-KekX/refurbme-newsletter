package com.example.refurbmenewsletter.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private long version;

    @Email
    @Column(unique = true)
    private String email;
    private String firstName;
    private String lastName;
    private Boolean acceptedPrivacyPolicy;

    public Customer(String email, String firstName, String lastName, Boolean acceptedPrivacyPolicy) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.acceptedPrivacyPolicy = acceptedPrivacyPolicy;
    }
}
