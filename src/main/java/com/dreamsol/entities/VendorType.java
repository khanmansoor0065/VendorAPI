package com.dreamsol.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class VendorType {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String typeName;

	public VendorType() {
	}

	public VendorType(int id, String typeName) {
		super();
		this.id = id;
		this.typeName = typeName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

}
