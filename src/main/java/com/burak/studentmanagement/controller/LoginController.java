package com.burak.studentmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
	
	@GetMapping("/showLoginPage")
	public String showLoginPage() {
		return "login/login-form";
	}
	
	//authenticateTheUser is automatically done by spring boot
	
	@GetMapping("/access-denied")
	public String showAccessDenied() {
		return "access-denied";
	}
}
