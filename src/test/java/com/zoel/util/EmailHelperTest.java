package com.zoel.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class EmailHelperTest {
	private EmailHelper helper = new EmailHelper();
	
	@Test
	public void validateValidEmail() {
		String[] validEmails = new String[] { "mkyong@yahoo.com",
				"mkyong-100@yahoo.com", "mkyong.100@yahoo.com",
				"mkyong111@mkyong.com", "mkyong-100@mkyong.net",
				"mkyong.100@mkyong.com.au", "mkyong@1.com",
				"mkyong@gmail.com.com", "mkyong+100@gmail.com",
				"mkyong-100@yahoo-test.com" };
		
		for (String email : validEmails) {
			assertEquals(true, helper.validate(email));
		}
	}

	@Test
	public void InValidEmailTest() {
		String[] invalidEmails = new String[] { "mkyong", "mkyong@.com.my",
				"mkyong123@gmail.a", "mkyong123@.com", "mkyong123@.com.com",
				".mkyong@mkyong.com", "mkyong()*@gmail.com", "mkyong@%*.com",
				"mkyong..2002@gmail.com", "mkyong.@gmail.com",
				"mkyong@mkyong@gmail.com", "mkyong@gmail.com.1a" };
		
		for (String email : invalidEmails) {
			assertEquals(false, helper.validate(email));
		}
	}
}
