package com.miguelpazatto.leadsmanager.entities.enums;

public enum LeadClassification {

	HOT(1),
	WARM(2),
	COLD(3);
	
	private int code;
	
	private LeadClassification(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
	
	public static LeadClassification valueOf(int code) {
		for (LeadClassification value : LeadClassification.values()) {
			if (code == value.getCode()) {
				return value;
			}
		}
		throw new IllegalArgumentException("Invalid LeadClassification code");
	}
	
}
