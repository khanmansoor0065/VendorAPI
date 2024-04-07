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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(length = 30,nullable = false)
	private String typeName;
	@OneToOne(mappedBy = "vendorType",fetch = FetchType.EAGER)
	private Vendor vendor;
}
