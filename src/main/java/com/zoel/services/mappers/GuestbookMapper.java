package com.zoel.services.mappers;

import java.util.List;

import com.zoel.vo.Guestbook;

public interface GuestbookMapper {
	public List<Guestbook> selectGuestbook(Long id);
	public Long createGuestbook(Guestbook guestbook);
	public Long updateGuestbook(Guestbook guestbook);
}
