package com.zoel.controllers;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.zoel.services.GuestbookService;
import com.zoel.vo.Guestbook;

public class GuestbookControllerTest {

	@InjectMocks
	GuestbookController controller;

	@Mock
	GuestbookService service;

	MockMvc mockMvc;

	ArgumentCaptor<Guestbook> guestbookCaptor;
	

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

		guestbookCaptor = ArgumentCaptor.forClass(Guestbook.class);
	}

	@Test
	public void getGuestbookList() throws Exception {
		List<Guestbook> guestbooks = new ArrayList<>();
		Guestbook gb = null;

		gb = new Guestbook();
		gb.setId(1L);
		gb.setEmail("helloworld@naver.com");
		gb.setBody("helloworld");
		gb.setPassword("helloworld");
		gb.setCreatedDate(new Date(System.currentTimeMillis()));
		gb.setModifiedDate(new Date(System.currentTimeMillis()));
		guestbooks.add(gb);

		gb = new Guestbook();
		gb.setId(2L);
		gb.setEmail("hiworld@naver.com");
		gb.setBody("hiworld");
		gb.setPassword("hiworld");
		gb.setCreatedDate(new Date(System.currentTimeMillis()));
		gb.setModifiedDate(new Date(System.currentTimeMillis()));
		guestbooks.add(gb);

		when(service.getAllGuestbooks()).thenReturn(guestbooks);

		mockMvc.perform(get("/guestbooks"))
		.andExpect(status().isOk())
		.andExpect(view().name("guestbook"))
		.andExpect(model().attribute("guestbooks", hasSize(2)))
		.andExpect(model().attribute("guestbooks", hasItem(
				allOf(
						hasProperty("email", is(guestbooks.get(0).getEmail())),
						hasProperty("body", is(guestbooks.get(0).getBody())),
						hasProperty("password", is(guestbooks.get(0).getPassword()))
					))))
		.andExpect(model().attribute("guestbooks", hasItem(
				allOf(
						hasProperty("email", is(guestbooks.get(1).getEmail())),
						hasProperty("body", is(guestbooks.get(1).getBody())),
						hasProperty("password", is(guestbooks.get(1).getPassword()))
					))));
	}
	
	@Test
	public void createGuestbookWithValidEmail() throws Exception{
		Guestbook created = new Guestbook();
		created.setId(1L);
		created.setEmail("helloworld@naver.com");
		created.setBody("helloworld");
		created.setPassword("helloworld");
		created.setCreatedDate(new Date(System.currentTimeMillis()));
		created.setModifiedDate(new Date(System.currentTimeMillis()));
		
		mockMvc.perform(post("/guestbooks")
				.param("email", created.getEmail())
				.param("body", created.getBody())
				.param("password", created.getPassword()))
		.andExpect(redirectedUrl("/guestbooks"));
		
		verify(service).createGuestbook(guestbookCaptor.capture());
		assertEquals(created.getBody(), guestbookCaptor.getValue().getBody());
	}
	
	@Test
	public void createGuestbookWithInvalidEmail() throws Exception{
		Guestbook created = new Guestbook();
		created.setId(1L);
		created.setEmail("rthndfnom");
		created.setBody("helloworld");
		created.setPassword("helloworld");
		created.setCreatedDate(new Date(System.currentTimeMillis()));
		created.setModifiedDate(new Date(System.currentTimeMillis()));
		
		mockMvc.perform(post("/guestbooks")
				.param("email", created.getEmail())
				.param("body", created.getBody())
				.param("password", created.getPassword()))
		.andExpect(status().isConflict());
	}
	
	@Test
	public void updateExistingGuestbook() throws Exception{
		Guestbook updated = new Guestbook();
		updated.setId(1L);
		updated.setEmail("helloworld@naver.com");
		updated.setBody("helloworld");
		updated.setPassword("helloworld");
		updated.setCreatedDate(new Date(System.currentTimeMillis()));
		updated.setModifiedDate(new Date(System.currentTimeMillis()));
		
		when(service.validateOwner(Matchers.any(Guestbook.class))).thenReturn(true);
		
		mockMvc.perform(post("/guestbooks/" + updated.getId())
				.param("email", updated.getEmail())
				.param("body", updated.getBody())
				.param("password", updated.getPassword()))
		.andExpect(redirectedUrl("/guestbooks"));
	}
	
	@Test
	public void updateNotExistingGuestbook() throws Exception{
		Guestbook gb = new Guestbook();
		gb.setId(1L);
		gb.setEmail("helloworld@naver.com");
		gb.setBody("helloworld");
		gb.setPassword("helloworld");
		gb.setCreatedDate(new Date(System.currentTimeMillis()));
		gb.setModifiedDate(new Date(System.currentTimeMillis()));
		
		when(service.validateOwner(Matchers.any(Guestbook.class))).thenReturn(false);
		
		mockMvc.perform(post("/guestbooks/" + gb.getId())
				.param("email", gb.getEmail())
				.param("body", gb.getBody())
				.param("password", gb.getPassword()))
		
		.andExpect(status().isNotFound());
	}
	
	
}
