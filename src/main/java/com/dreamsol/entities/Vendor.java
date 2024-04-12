package com.dreamsol.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Vendor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
    @Column(length = 40 , nullable = false)
	private String name;
	@Column(nullable = false,unique = true)
	private long mob;
	@Column(length = 50 ,nullable = false)
	private String email;
	@Column(length = 200 )
	private String brief;
	private String file;

	@OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	private VendorType vendorType;

	@OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL)
	private Set<Product> products;
}
