package com.zoel.services;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.zoel.exceptions.DBWriteFailureException;
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
	public void setup() throws InterruptedException, DBWriteFailureException {
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

		// delay to make different time data for each guestbook
		Thread.sleep(1000);

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

		guestbooks.sort((lhs, rhs) -> {
			return rhs.getCreatedDate().compareTo(lhs.getCreatedDate());
		});

		// Guestbook.equals method cannot satisfy this test
		assertEquals(guestbooksFound.size(), guestbooks.size());
		for (int i = 0; i < guestbooksFound.size(); ++i) {
			Guestbook lhs = guestbooksFound.get(i);
			Guestbook rhs = guestbooks.get(i);
			assertEquals(lhs.getId(), rhs.getId());
			assertEquals(lhs.getEmail(), rhs.getEmail());
			assertEquals(lhs.getBody(), rhs.getBody());
		}
	}

	@Test
	@Transactional
	public void getExistingGuestbook() {
		Guestbook gb = guestbooks.get(0);

		assertNotNull(service.getGuestbook(gb.getId()));
	}

	@Test
	@Transactional
	public void getNotExistingGuestbook() {
		Guestbook gbExpected = null;
		Long notExistingId = -1L;

		Guestbook gbActual = service.getGuestbook(notExistingId);

		assertEquals(gbExpected, gbActual);
	}

	@Test
	@Transactional
	@Rollback
	public void createGuestbook() {
		Guestbook gb = new Guestbook();
		gb.setEmail("helloworld@naver.com");
		gb.setBody("helloworld");
		gb.setPassword("helloworld");

		try {
			service.createGuestbook(gb);
		} catch (DBWriteFailureException e) {
			e.printStackTrace();
		}
	}

	@Test(expected=DBWriteFailureException.class)
	@Transactional
	@Rollback
	public void updateNotExistingGuestbook(){
		Guestbook gbNotExisting = new Guestbook();
		gbNotExisting.setId(-1L);
		service.updateGuestbook(gbNotExisting);
	}
	@Test
	@Transactional
	@Rollback
	public void updateExistingGuestbook() throws DBWriteFailureException {
		Guestbook gbExpected = guestbooks.get(0);
		gbExpected.setBody("test body");
		service.updateGuestbook(gbExpected);
		Guestbook gbActual = service.getGuestbook(gbExpected.getId());

		assertEquals(gbExpected.getBody(), gbActual.getBody());
		assert (gbExpected.getModifiedDate().before(gbActual.getModifiedDate()));
	}

	@Test
	@Transactional
	public void validateOwner() {
		Guestbook gbValidOwner = guestbooks.get(0);

		Guestbook gbInvalidOwner = new Guestbook();
		gbInvalidOwner.setEmail(gbValidOwner.getEmail());
		gbInvalidOwner.setBody(gbValidOwner.getBody());
		gbInvalidOwner.setPassword("invalid password");

		assertTrue(service.validateOwner(gbValidOwner));
		assertFalse(service.validateOwner(gbInvalidOwner));
	}
}
