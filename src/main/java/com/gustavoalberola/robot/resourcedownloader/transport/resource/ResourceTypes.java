package com.gustavoalberola.robot.resourcedownloader.transport.resource;

public enum ResourceTypes {
	IMAGE("image/jpg,image/jpeg,image/png,image/gif");
	
	private String types;
	
	private ResourceTypes(String types) {
		this.types = types;
	}
	
	public String getTypes() { return types; }
}
