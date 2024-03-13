package com.dreamsol.entities;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

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

	public int getId() {
		return id;
	}

	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getMob() {
		return mob;
	}

	public void setMob(long mob) {
		this.mob = mob;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public VendorType getVendorType() {
		return vendorType;
	}

	public void setVendorType(VendorType vendorType) {
		this.vendorType = vendorType;
	}

	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

}
