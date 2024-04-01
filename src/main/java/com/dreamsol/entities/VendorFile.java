package com.dreamsol.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class VendorFile
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String originalFileName;
    private String generatedFileName;
    private String fileType;
}
