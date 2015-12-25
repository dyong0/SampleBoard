package com.zoel.services;

import java.util.List;

import com.zoel.exceptions.DBWriteFailureException;
import com.zoel.vo.Guestbook;

public interface GuestbookService {

	List<Guestbook> getAllGuestbooks();

	Guestbook getGuestbook(Long id);

	void createGuestbook(Guestbook guestbook) throws DBWriteFailureException;

	void updateGuestbook(Guestbook guestbook) throws DBWriteFailureException;

	boolean validateOwner(Guestbook guestbook);

}
