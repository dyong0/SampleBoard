package com.zoel.services;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.zoel.vo.Guestbook;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/application-context.xml")
public class GuestbookServiceTest {

	@Autowired
	private GuestbookService service;

	private List<Guestbook> guestbooks;

	@Before
	@Transactional
	@Rollback
	public void setup() {
		guestbooks = new ArrayList<Guestbook>();
		
		Guestbook gb = null;

		gb = new Guestbook();
		gb.setEmail("helloworld@naver.com");
		gb.setBody("helloworld");
		gb.setPassword("helloworld");
		gb.setCreatedDate(new Date(System.currentTimeMillis()));
		gb.setModifiedDate(new Date(System.currentTimeMillis()));
		guestbooks.add(gb);
		service.createGuestbook(gb);

		gb = new Guestbook();
		gb.setEmail("hiworld@naver.com");
		gb.setBody("hiworld");
		gb.setPassword("hiworld");
		gb.setCreatedDate(new Date(System.currentTimeMillis()));
		gb.setModifiedDate(new Date(System.currentTimeMillis()));
		guestbooks.add(gb);
		service.createGuestbook(gb);
	}

	@Test
	@Transactional
	public void getAllGuestbooks() {
		List<Guestbook> guestbooksFound = service.getAllGuestbooks();

		Gson gson = new Gson();
		StringBuilder sbExpected = new StringBuilder();
		StringBuilder sbActual = new StringBuilder();
		for (Guestbook gb : guestbooksFound) {
			sbExpected.append(gson.toJson(gb).toString());
		}
		for (Guestbook gb : guestbooks) {			
			sbActual.append(gson.toJson(gb).toString());
		}
		
		assertTrue(sbExpected.toString().equals(sbActual.toString()));
	}
}
