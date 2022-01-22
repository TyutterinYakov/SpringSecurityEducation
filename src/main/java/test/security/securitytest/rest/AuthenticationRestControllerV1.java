package test.security.securitytest.rest;

import java.util.HashMap;
import java.util.Map;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import test.security.securitytest.model.User;
import test.security.securitytest.repository.UserRepository;
import test.security.securitytest.security.JwtTokenProvider;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationRestControllerV1 {
	
	private final AuthenticationManager authenticationManager;
	private UserRepository userDao;
	private JwtTokenProvider jwtTokenProvider;
	
	
	
	public AuthenticationRestControllerV1(AuthenticationManager authenticationManager, UserRepository userDao,
			JwtTokenProvider jwtTokenProvider) {
		super();
		this.authenticationManager = authenticationManager;
		this.userDao = userDao;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@PostMapping("/login")
	public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequestDTO request){
//		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
			User user = userDao.findByEmail(request.getEmail()).orElseThrow(()-> new UsernameNotFoundException("User doesnt not exists"));
			String token = jwtTokenProvider.createToken(request.getEmail(), user.getRole().name());
			Map<Object, Object> response = new HashMap<>();
			response.put("email", request.getEmail());
			response.put("token", token);
			return ResponseEntity.ok(response);
//		} catch(AuthenticationException ex) {
//			return new ResponseEntity<>("Invalid password/email combination", HttpStatus.FORBIDDEN);
//		}
		
	}
	
	@PostMapping("/logout")
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		SecurityContextLogoutHandler securityContextLogout = new SecurityContextLogoutHandler();
		securityContextLogout.logout(request, response, null);
	}
}
