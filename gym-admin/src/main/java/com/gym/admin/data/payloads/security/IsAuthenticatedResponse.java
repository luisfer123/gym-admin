package com.gym.admin.data.payloads.security;

public class IsAuthenticatedResponse {
	
	private boolean isAuthenticated;
	
	public IsAuthenticatedResponse() {
		isAuthenticated = false;
	}

	public IsAuthenticatedResponse(boolean isAuthenticated) {
		super();
		this.isAuthenticated = isAuthenticated;
	}

	public boolean isAuthenticated() {
		return isAuthenticated;
	}

	public void setAuthenticated(boolean isAuthenticated) {
		this.isAuthenticated = isAuthenticated;
	}

}
