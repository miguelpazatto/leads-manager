package com.miguelpazatto.leadsmanager.entities.enums;

public enum Role {

	ADMIN(1),
	COLLABORATOR(2);
	
	private int code;
	
	private Role(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
	
	public static Role valueof(int code) {
		for (Role value : Role.values()) {
			if (code == value.getCode()) {
				return value;
			}
		}
		throw new IllegalArgumentException("Invalid Role code");
	}
	
}
