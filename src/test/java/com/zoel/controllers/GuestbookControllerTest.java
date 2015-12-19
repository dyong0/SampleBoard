package com.zoel.controllers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.matchers.EndsWith;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.zoel.vo.Guestbook;

public class GuestbookControllerTest {

	@InjectMocks
	GuestbookController controller;

	@Mock
	GuestbookService service;

	MockMvc mockMvc;

	private ArgumentCaptor<Guestbook> guestbookCaptor;

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
		gb.setCreatedDate(LocalDateTime.now());
		gb.setModifiedDate(LocalDateTime.now());
		guestbooks.add(gb);

		gb = new Guestbook();
		gb.setId(2L);
		gb.setEmail("hiworld@naver.com");
		gb.setBody("hiworld");
		gb.setPassword("hiworld");
		gb.setCreatedDate(LocalDateTime.now());
		gb.setModifiedDate(LocalDateTime.now());
		guestbooks.add(gb);

		when(service.getAllGuestbooks()).thenReturn(guestbooks);

		mockMvc.perform(get("/guestbookList"))
		.andExpect(jsonPath("$.guestbooks[*].email", contains(guestbooks.get(0).getEmail(), guestbooks.get(1).getEmail())))
		.andExpect(status().isOk());
	}

	@Test
	public void getExistingGuestbook() throws Exception {
		Guestbook gb = new Guestbook();
		gb.setId(1L);
		gb.setEmail("helloworld@naver.com");
		gb.setBody("helloworld");
		gb.setPassword("helloworld");
		gb.setCreatedDate(LocalDateTime.now());
		gb.setModifiedDate(LocalDateTime.now());

		when(service.getGuestbook()).thenReturn(gb);

		mockMvc.perform(get("/guestbook/" + gb.getId()))
		.andExpect(jsonPath("$.email", is(gb.getEmail())))
		.andExpect(status().isOk());
	}

	@Test
	public void getNotExistingGuestbook() throws Exception {
		when(service.getGuestbook()).thenReturn(null);

		mockMvc.perform(get("/guestbook/" + "1"))
		.andExpect(status().isNotFound());
	}
	
	@Test
	public void createGuestbook() throws Exception{
		Guestbook created = new Guestbook();
		created.setId(1L);
		created.setEmail("helloworld@naver.com");
		created.setBody("helloworld");
		created.setPassword("helloworld");
		created.setCreatedDate(LocalDateTime.now());
		created.setModifiedDate(LocalDateTime.now());
		
		when(service.createGuestbook(Matchers.any(Guestbook.class))).thenReturn(created);
		
		mockMvc.perform(put("/guestbook")
				.content("{"
						+ "\"email\":\"" + created.getEmail() + "\""
						+ "\"body\":\"" + created.getBody() + "\""
						+ "\"password\":\"" + created.getPassword() + "\""
						+"}")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(header().string("Location", Matchers.endsWith("/guestbook/" + created.getId())))
		.andExpect(jsonPath("$.email", is(created.getEmail())))
		.andExpect(status().isCreated());
		
		verify(service).createGuestbook(guestbookCaptor.capture());
		assertEquals("helloworld", guestbookCaptor.getValue().getBody());
	}
	
	@Test
	public void updateExistingGuestbook() throws Exception{
		Guestbook updated = new Guestbook();
		updated.setId(1L);
		updated.setEmail("helloworld@naver.com");
		updated.setBody("helloworld");
		updated.setPassword("helloworld");
		updated.setCreatedDate(LocalDateTime.now());
		updated.setModifiedDate(LocalDateTime.now());
		
		when(service.updateGuestbook(updated)).thenReturn(updated);
		
		mockMvc.perform(post("/guestbook/" + updated.getId())
				.content("{"
						+ "\"email\":\"" + updated.getEmail() + "\""
						+ "\"body\":\"" + updated.getBody() + "\""
						+ "\"password\":\"" + updated.getPassword() + "\""
						+"}")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.body", is(updated.getBody())))
		.andExpect(status().isOk());		
	}
	
	@Test
	public void updateNotExistingGuestbook() throws Exception{
		Guestbook gb = new Guestbook();
		gb.setId(1L);
		gb.setEmail("helloworld@naver.com");
		gb.setBody("helloworld");
		gb.setPassword("helloworld");
		gb.setCreatedDate(LocalDateTime.now());
		gb.setModifiedDate(LocalDateTime.now());
		
		when(service.updateGuestbook(Matchers.any(Guestbook.class))).thenReturn(null);
		
		mockMvc.perform(post("/guestbook/" + gb.getId())
				.content("{"
						+ "\"email\":\"" + gb.getEmail() + "\""
						+ "\"body\":\"" + gb.getBody() + "\""
						+ "\"password\":\"" + gb.getPassword() + "\""
						+"}")
				.contentType(MediaType.APPLICATION_JSON))
		.andDo(print())
		.andExpect(status().isNotFound());		
	}
}
