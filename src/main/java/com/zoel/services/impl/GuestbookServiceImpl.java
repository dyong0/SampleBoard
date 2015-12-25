package com.zoel.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zoel.exceptions.DBWriteFailureException;
import com.zoel.services.GuestbookService;
import com.zoel.services.mappers.GuestbookMapper;
import com.zoel.vo.Guestbook;

@Service
@Transactional
public class GuestbookServiceImpl implements GuestbookService {

	@Autowired
	GuestbookMapper mapper;

	@Override
	public List<Guestbook> getAllGuestbooks() {
		return mapper.selectGuestbook(null);
	}

	@Override
	public Guestbook getGuestbook(Long id) {
		List<Guestbook> guestbooks = mapper.selectGuestbook(id);
		if (!guestbooks.isEmpty()) {
			return guestbooks.get(0);
		} else {
			return null;
		}
	}

	@Override
	public void createGuestbook(Guestbook guestbook) throws DBWriteFailureException{
		if(mapper.createGuestbook(guestbook) != 1L){
			throw new DBWriteFailureException();
		}	
	}

	@Override
	public void updateGuestbook(Guestbook guestbook) throws DBWriteFailureException {
		if(mapper.updateGuestbook(guestbook) != 1L){
			throw new DBWriteFailureException();
		}
	}

	@Override
	public boolean validateOwner(Guestbook guestbook) {
		Guestbook expected = guestbook;
		Guestbook actual = this.getGuestbook(guestbook.getId());
		
		return expected.getEmail().equals(actual.getEmail()) 
				&& expected.getPassword().equals(actual.getPassword());
				
	}
	

}
