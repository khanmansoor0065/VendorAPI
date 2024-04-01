package com.dreamsol.entities;

import java.util.Set;

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
public class Vendor {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String name;
	private long mob;
	private String email;
	private String brief;
	private String profileImage;

	@OneToOne(cascade = CascadeType.ALL)
	private VendorType vendorType;

	@OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL)
	private Set<Product> products;
}
