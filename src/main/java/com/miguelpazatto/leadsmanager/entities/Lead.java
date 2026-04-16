package com.miguelpazatto.leadsmanager.entities;

import java.io.Serializable;
import java.util.Objects;

import com.miguelpazatto.leadsmanager.entities.enums.LeadClassification;
import com.miguelpazatto.leadsmanager.entities.enums.LeadStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_lead")
public class Lead implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String email;
	private String phone;
	private Integer totalScore;
	
	private LeadStatus leadStatus;
	private LeadClassification leadClassification;
	
	public Lead() {
	}
	
	public Lead(Long id, String name, String email, String phone, Integer totalScore, LeadStatus leadStatus,
			LeadClassification leadClassification) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.totalScore = totalScore;
		this.leadStatus = leadStatus;
		this.leadClassification = leadClassification;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(Integer totalScore) {
		this.totalScore = totalScore;
	}

	public LeadStatus getLeadStatus() {
		return leadStatus;
	}

	public void setLeadStatus(LeadStatus leadStatus) {
		this.leadStatus = leadStatus;
	}

	public LeadClassification getLeadClassification() {
		return leadClassification;
	}

	public void setLeadClassification(LeadClassification leadClassification) {
		this.leadClassification = leadClassification;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Lead other = (Lead) obj;
		return Objects.equals(id, other.id);
	}
	
}
