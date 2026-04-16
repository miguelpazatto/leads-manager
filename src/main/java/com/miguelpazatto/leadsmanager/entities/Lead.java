package com.miguelpazatto.leadsmanager.entities;

import java.io.Serializable;

import com.miguelpazatto.leadsmanager.entities.enums.LeadClassification;
import com.miguelpazatto.leadsmanager.entities.enums.LeadStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
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
	
}
