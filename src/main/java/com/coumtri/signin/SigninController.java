package com.coumtri.signin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class SigninController {

	@RequestMapping(value = "signin")
	public String signin(HttpServletRequest request) {
        return "signin/signin";
    }
}
