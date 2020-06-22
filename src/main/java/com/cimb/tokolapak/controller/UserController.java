package com.cimb.tokolapak.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cimb.tokolapak.dao.UserRepo;
import com.cimb.tokolapak.entity.User;
import com.cimb.tokolapak.util.EmailUtil;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserController {

	@Autowired 
	private UserRepo userRepo;
	private PasswordEncoder pwEncoder = new BCryptPasswordEncoder();
	
	@Autowired
	private EmailUtil emailUtil;
	@PostMapping
	public User registerUser(@RequestBody User user) {
//		Optional<User> findUser = userRepo.findByUsername(user.getUsername());
//		if (findUser.toString()=="Optional.empty") {
//			throw new RuntimeException("Username Exist!");
//		}
//		System.out.println(user);
		String encodedPassword = pwEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		User savedPassword  = userRepo.save(user);
		savedPassword.setPassword(null);
//		String teks = "hello "+ user.getUsername() + " Click Here to Verified http://localhost:8080/users/edit/"+user.getId();
		user.setVerified(false);
		this.emailUtil.sendEmail("lyudokusumo@gmail.com", "Verifikasi Email","<h1>Hallo, "+ user.getUsername() +"</h1><a href=\"http://localhost:8080/users/edit/"+user.getId()+"\">link</a> ini untuk verifikasi email anda");
		return savedPassword;
	}
	
	@GetMapping("/edit/{userId}")
	public User editVerified(@PathVariable int userId) {
		User findUser = userRepo.findById(userId).get();
		if (findUser==null) {
//			throw new RuntimeException("User Not Found");
			System.out.println("User Not Found");
		}
		findUser.setVerified(true);
		return userRepo.save(findUser);
	}
	@PostMapping("/login")
	public User loginUser(@RequestBody User user) {
		User findUser = userRepo.findByUsername(user.getUsername()).get();
							// Password raw/sblm encode  |  password sdh encode
		if(pwEncoder.matches(user.getPassword(), findUser.getPassword())) {
			findUser.setPassword(null);
			return findUser;
		}else {
			return null;
		}
	}
	
	@GetMapping("/login")
	public User getLoginUser(@RequestParam String username,@RequestParam String password) {
		User findUser = userRepo.findByUsername(username).get();
		
		if(pwEncoder.matches(password, findUser.getPassword())) {
			findUser.setPassword(null);
			return findUser;
		}
		return null;
	}

	//localhost:8080/users/login?username=seto&password

	@PostMapping("/sendEmail")
	public String sendEmailTesting() {
		this.emailUtil.sendEmail("lyudokusumo@gmail.com", "Testing Spring Mail", "<h1>Hello world</h1> \n ini lukman? ");
		return "Email Sent";
	}
	
}
