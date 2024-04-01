package com.dreamsol.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class VendorType {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String typeName;
	@OneToOne(mappedBy = "vendorType")
	private Vendor vendor;
}
