package com.miguelpazatto.leadsmanager.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.miguelpazatto.leadsmanager.entities.enums.LeadClassification;
import com.miguelpazatto.leadsmanager.entities.enums.LeadStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_lead")
@EqualsAndHashCode(onlyExplicitlyIncluded=true)
@NoArgsConstructor
public class Lead implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;
	private String name;
	private String email;
	private String phone;
	private Integer totalScore;
	
	@Enumerated(EnumType.STRING)
	private LeadStatus leadStatus;
	
	@Enumerated(EnumType.STRING)
	private LeadClassification leadClassification;
	
	@ManyToOne
	@JoinColumn(name = "salesman_id")
	private Salesman assignedTo;

	@OneToMany(mappedBy = "id.lead", cascade = CascadeType.ALL, orphanRemoval = true)	 //provisório
	private List<Answer> options = new ArrayList<>();

	public Lead(Long id, String name, String email, String phone, LeadStatus leadStatus,
			LeadClassification leadClassification, Salesman assignedTo) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.leadStatus = leadStatus;
		this.leadClassification = leadClassification;
		this.assignedTo = assignedTo;
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

	public void setTotalScore() {
		this.totalScore = this.options.stream().mapToInt(Answer::getWeight).sum();
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

	public Salesman getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(Salesman assignedTo) {
		this.assignedTo = assignedTo;
	}

	public List<Answer> getOptions() {
		return options;
	}

	public void setOptions(List<Answer> options) {
		this.options = options;
	}

}
