package com.zoel.services;

import java.util.List;

import com.zoel.vo.Guestbook;

public interface GuestbookService {

	List<Guestbook> getAllGuestbooks();

	Guestbook getGuestbook(Long id);

	Long createGuestbook(Guestbook guestbook);

	Long updateGuestbook(Guestbook guestbook);

}
