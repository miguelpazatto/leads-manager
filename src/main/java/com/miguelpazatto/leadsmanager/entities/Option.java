package com.miguelpazatto.leadsmanager.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "tb_option")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded=true)
@NoArgsConstructor
public class Option implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;
	private String description;
	private Integer weight;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "question_id")
	private Question question;
	
	@JsonIgnore
	@OneToMany(mappedBy = "id.option")
	private List<Answer> leads = new ArrayList<>();

	public Option(Long id, String description, Integer weight, Question question) {
		super();
		this.id = id;
		this.description = description;
		this.weight = weight;
		this.question = question;
	}
	
	
	
}
