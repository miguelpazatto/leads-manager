package com.miguelpazatto.leadsmanager.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Entity
@Table(name = "tb_salesman")
public class Salesman implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;
	private String name;
	private String email;
	private String phone;
	
	@OneToMany(mappedBy = "assignedTo", cascade = CascadeType.ALL, orphanRemoval = true) //provisório
	private List<Lead> leads = new ArrayList<>();
	
	public Salesman(Long id, String name, String email, String phone) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.phone = phone;
	}
	
}
