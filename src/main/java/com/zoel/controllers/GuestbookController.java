package com.zoel.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.zoel.exceptions.ConflictException;
import com.zoel.exceptions.DBWriteFailureException;
import com.zoel.exceptions.GuestbookNotFoundException;
import com.zoel.services.GuestbookService;
import com.zoel.util.EmailHelper;
import com.zoel.vo.Guestbook;

@Controller
@RequestMapping("/guestbooks")
public class GuestbookController {

	@Autowired
	GuestbookService guestbookService;

	EmailHelper emailHelper;

	public GuestbookController() {
		this.emailHelper = new EmailHelper();
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ModelAndView viewGuestbook() {
		List<Guestbook> guestbooks = guestbookService.getAllGuestbooks();

		ModelAndView mv = new ModelAndView();
		mv.addObject("guestbooks", guestbooks);
		mv.setViewName("guestbook");

		return mv;
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public ModelAndView createGuestbook(@ModelAttribute Guestbook param) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/guestbooks");

		boolean bValidEmail = this.emailHelper.validate(param.getEmail());
		if (bValidEmail) {
			try{
				guestbookService.createGuestbook(param);
			} catch(DBWriteFailureException e){
				throw new ConflictException();
			}
		} else {
			throw new ConflictException();			
		}

		return mv;
	}

	@RequestMapping(value = "/{guestbookId}", method = RequestMethod.POST)
	public ModelAndView updateGuestbook(@PathVariable Long guestbookId, @ModelAttribute Guestbook param) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/guestbooks");

		param.setId(guestbookId);

		boolean bValidAuth = guestbookService.validateOwner(param);
		if (bValidAuth) {
			try {
				guestbookService.updateGuestbook(param);
			} catch (DBWriteFailureException e) {
				throw new ConflictException();
			}
		} else {
			throw new GuestbookNotFoundException();
		}

		return mv;
	}
}
