package com.miguelpazatto.leadsmanager.entities;

import java.io.Serializable;
import java.util.Objects;

import com.miguelpazatto.leadsmanager.entities.pk.AnswerPK;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_answer")
public class Answer implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private AnswerPK id = new AnswerPK();
	
	private Integer recordedWeight;
	private String recordedString;

	public Answer() {
	}
	
	public Answer(Option option, Lead lead) {
		id.setOption(option);
		id.setLead(lead);
		this.recordedWeight = option.getWeight();
		this.recordedString = option.getDescription();
	}

	public Option getOption() {
		return id.getOption();
	}

	public void setOption(Option option) {
		id.setOption(option);
	}
	
	public Lead getLead() {
		return id.getLead();
	}

	public void setLead(Lead lead) {
		id.setLead(lead);
	}

	public Integer getRecordedWeight() {
		return recordedWeight;
	}

	public String getRecordedString() {
		return recordedString;
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
		Answer other = (Answer) obj;
		return Objects.equals(id, other.id);
	}
	
}
