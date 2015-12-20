package com.zoel.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.zoel.services.GuestbookService;
import com.zoel.vo.Guestbook;

@Controller
public class GuestbookController {
	
	@Autowired
	GuestbookService guestbookService;
	
	@RequestMapping("/guestbookList")
	public ModelAndView guestbookList(){
		List<Guestbook> guestbooks = guestbookService.getAllGuestbooks();
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("guestbooks", guestbooks);
		mv.setViewName("guestbookList");
		
		return mv;
	}
}
