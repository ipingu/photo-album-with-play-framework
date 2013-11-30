package models;

import controllers.RandomChristmasGift;

public class Child {

	private int secretName;
	private String email;
	private String name;

	public int getSecretName() {
		return secretName;
	}

	public String getEmail() {
		return email;
	}

	public String getName() {
		return name;
	}

	public Child(String email, String name, int secretName) {
		super();
		this.email = email;
		this.name = name;
		this.secretName = secretName;
	}
}