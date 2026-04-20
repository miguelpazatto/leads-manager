package com.miguelpazatto.leadsmanager.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.miguelpazatto.leadsmanager.entities.enums.LeadClassification;
import com.miguelpazatto.leadsmanager.entities.enums.LeadStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_lead")
@Getter
@Setter
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
	
	private LeadStatus leadStatus;
	private LeadClassification leadClassification;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "salesman_id")
	private Salesman assignedTo;
	
	@OneToMany(mappedBy = "id.lead")
	private List<Answer> options = new ArrayList<>();

	public Lead(Long id, String name, String email, String phone, Integer totalScore, LeadStatus leadStatus,
			LeadClassification leadClassification, Salesman assignedTo) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.totalScore = totalScore;
		this.leadStatus = leadStatus;
		this.leadClassification = leadClassification;
		this.assignedTo = assignedTo;
	}
	
	
	
}
