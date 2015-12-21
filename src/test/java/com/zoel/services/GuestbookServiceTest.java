package com.zoel.services;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

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
		gb.setCreatedDate(LocalDateTime.now());
		gb.setModifiedDate(LocalDateTime.now());
		guestbooks.add(gb);
		service.createGuestbook(gb);

		gb = new Guestbook();
		gb.setEmail("hiworld@naver.com");
		gb.setBody("hiworld");
		gb.setPassword("hiworld");
		gb.setCreatedDate(LocalDateTime.now());
		gb.setModifiedDate(LocalDateTime.now());
		guestbooks.add(gb);
		service.createGuestbook(gb);
	}

	@Test
	@Transactional
	public void getAllGuestbooks() {
		List<Guestbook> guestbooksFound = service.getAllGuestbooks();

		assert(guestbooksFound.containsAll(guestbooks));
	}
}
