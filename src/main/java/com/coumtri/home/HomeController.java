package com.coumtri.home;

import java.security.Principal;
import java.util.List;

import com.coumtri.account.IUserService;
import com.coumtri.account.UserService;
import com.coumtri.home.form.UserInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

	@Autowired
	private IUserService userService;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Principal principal) {
		return principal != null ? "home/homeSignedIn" : "home/homeNotSignedIn";
	}

	/**
	 * Source : http://www.mkyong.com/spring-mvc/spring-3-mvc-and-json-example/
	 * @return the details of the users allowed to access the application
	 */
	@RequestMapping(value = "/showUserDetails", method = RequestMethod.POST)
	//@ResponseBody
	public List<UserInformation> ajaxUserDetails() {
		return userService.showUserDetails();
	}
}
