package com.org.security.controller;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.org.security.jwt.JwtAuthTokenFilter;
import com.org.security.jwt.JwtProvider;
import com.org.security.model.User;
import com.org.security.repository.UserRepository;
import com.org.security.request.EmailRequestDto;
import com.org.security.request.ForgetPasswordRequest;
import com.org.security.request.LoginForm;
import com.org.security.request.PasswordResetRequest;
import com.org.security.request.SetUpPasswordRequest;
import com.org.security.response.JwtResponse;
import com.org.security.service.AccessServiceImpl;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class AuthRestAPIs {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtProvider jwtProvider;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	AccessServiceImpl accessService;

	EmailRequestDto email = null;
	

	@PostMapping("/api/auth/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) {
		System.out.println(encoder.encode("password"));

		System.out.println(encoder.matches(loginRequest.getPassword(), "$2a$10$jbIi/RIYNm5xAW9M7IaE5.WPw6BZgD8wcpkZUg0jm8RHPtdfDcMgm"));
		System.out.println(loginRequest.getUsername());
		System.out.println(loginRequest.getPassword());
		System.out.println(accessService.existUserByEmailId(loginRequest.getUsername()));
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);

	
		String jwt = jwtProvider.generateJwtToken(authentication);
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		User data=accessService.getByEmailId(loginRequest.getUsername());
		
		
		
		return ResponseEntity.ok(new JwtResponse(data.getId(),data.getName(),jwt, userDetails.getUsername(),data.getMobile(),data.getGender(),data.getDepartment(),data.getCity(),data.getHiredDate(), userDetails.getAuthorities()));
	}
	
	
	@PutMapping("/api/reset/{id}")
	public String passwordReset(@PathVariable("id") String id,@RequestBody PasswordResetRequest passwordResetRequest) throws Exception {
		return accessService.resetPasswordById(id, passwordResetRequest);
	}
	
	@GetMapping("/api/auth/existbyemail/{emailId}")
	public boolean isEmail(@PathVariable("emailId") String emailId) {
		return accessService.existUserByEmailId(emailId);
	}
	
	@GetMapping("/api/auth/existByMobile/{mobile}")
	public boolean isMobile(@PathVariable("mobile") String mobile) {
		return accessService.existUserByMobileNumber(mobile);
	}
	
	@GetMapping("/api/auth/findByMobile/{mobile}")
	public User findUserByMobile(@PathVariable("mobile") String mobile) {
		return accessService.findByMobile(mobile);
	}
	
	@PostMapping("/api/auth/forgotPassword")
	public ResponseEntity<?> forgotPassword(@RequestBody ForgetPasswordRequest forgetPasswordRequest) throws Exception {
		
		if(accessService.existUserByMobileNumber(forgetPasswordRequest.getMobile())) {
			
			User userByMobile = accessService.findByMobile(forgetPasswordRequest.getMobile());
			
				if(accessService.existUserByEmailId(forgetPasswordRequest.getEmail())) {
					
					User userByEmail = accessService.getByEmailId(forgetPasswordRequest.getEmail());
					
					if(userByMobile.getEmailId().equals(userByEmail.getEmailId())) {
						
						
						String otp= new DecimalFormat("000000").format(new Random().nextInt(999999));
						
				
						
						User user = new User (userByEmail.getId(),userByEmail.getName(),userByEmail.getEmailId(),
								userByEmail.getMobile(),userByEmail.getGender(),userByEmail.getDepartment(),userByEmail.getCity(),
								userByEmail.getHiredDate(),encoder.encode(otp)
								);
						
						userRepository.save(user);
						
						Authentication authentication = authenticationManager.authenticate(
								new UsernamePasswordAuthenticationToken(userByEmail.getEmailId(), otp));

						SecurityContextHolder.getContext().setAuthentication(authentication);

						String jwt = jwtProvider.generateJwtToken(authentication);
						
						
						email = new EmailRequestDto("Orchestrator-Reset Password",userByEmail.getEmailId(),
								"username : " + userByEmail.getEmailId() + "   OTP : " + otp);
						
						System.out.println(userByEmail.getEmailId());
						
						
						return ResponseEntity.ok(new JwtResponse(userByEmail.getId(),userByEmail.getName(),jwt,userByEmail.getEmailId()));
						
						
					}else {
						throw new Exception("User Not found");
					}
					
					
					
				}else {
					throw new Exception("Email Address is not Registered");
				}
			
			
			
		}else {
			throw new Exception("mobile number is not registered");
		}
		
		
	
	}
	
	
	
	
	@PostMapping("/api/auth/email")
	public String sendEmail(@Valid @RequestBody EmailRequestDto emailRequestDto) {

		
		emailRequestDto.setEmail(email.getEmail());
		emailRequestDto.setBody(email.getBody());
		emailRequestDto.setSubject(email.getSubject());
		emailRequestDto.setFrom("octatenoreply@gmail.com");
		System.out.println(emailRequestDto.getEmail()+"**************************");
		System.out.println(emailRequestDto.getBody()+"**************************");

		
		HttpHeaders headers = new HttpHeaders();
		

		headers.setBearerAuth(JwtAuthTokenFilter.jwt.toString());
		headers.setAccept(Arrays.asList(org.springframework.http.MediaType.APPLICATION_JSON));
		HttpEntity<EmailRequestDto> entity = new HttpEntity<EmailRequestDto>(emailRequestDto, headers);

		ResponseEntity<String> responseEntity = restTemplate.exchange("http://localhost:8082/api/access/hr/email",
				HttpMethod.POST, entity, String.class);
		
		return responseEntity.getBody();
	}
	
	@PostMapping("/api/setPassword/{id}")
	public String setPassword(@PathVariable("id") String id, @RequestBody SetUpPasswordRequest setUpPassword) throws Exception {
		return accessService.setUpPassword(id, setUpPassword);
	}
	
	
	@GetMapping("/api/auth/getallUsers")
	public List<User> getAllDataUser() {
		return userRepository.findAll();
	}
	
	
	
	

	

}