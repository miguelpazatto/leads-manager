package com.miguelpazatto.leadsmanager.entities.enums;

public enum LeadStatus {

	NEW(1),
	CONTACTED(2);
	
	private int code;
	
	private LeadStatus(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
	
	public static LeadStatus valueOf(int code) {
		for (LeadStatus value : LeadStatus.values()) {
			if (code == value.getCode()) {
				return value;
			}
		}
		throw new IllegalArgumentException("Invalid LeadStatus code");
	}
	
}
