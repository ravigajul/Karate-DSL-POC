package com.karate.helpers;

import com.github.javafaker.Faker;

public class DataGenerator {
	static String  userName;
	public static String generateRandomUser(){
		
		Faker faker = new Faker();
		userName = faker.name().firstName().toLowerCase()+faker.random().nextInt(0, 100).toString();
		return userName;
	}
	
	public static String generateRandomEmail(){
		return userName+"@test.com";
	}
}
