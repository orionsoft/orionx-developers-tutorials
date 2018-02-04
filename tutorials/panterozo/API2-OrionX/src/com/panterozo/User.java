package com.panterozo;

public class User {

	private String UserName;
	private String Email;
	private String Wallet;
	/**/
	private String apiKeyPublic;
	private String secretKey;
	
	/*Constructor*/
	public User(){}
	
	/*Getters & Setters*/
	
	public String getUserName() {
		return UserName;
	}


	public void setUserName(String user) {
		UserName = user;
	}


	public String getEmail() {
		return Email;
	}


	public void setEmail(String email) {
		Email = email;
	}


	public String getWallet() {
		return Wallet;
	}


	public void setWallet(String wallet) {
		Wallet = wallet;
	}


	public String getApiKeyPublic() {
		return apiKeyPublic;
	}


	public void setApiKeyPublic(String apiKeyPublic) {
		this.apiKeyPublic = apiKeyPublic;
	}


	public String getSecretKey() {
		return secretKey;
	}


	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
}
