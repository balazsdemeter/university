package hu.cubix.university.security;

import hu.cubix.university.dto.LoginDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JwtLoginController {

	private final AuthenticationManager authenticationManager;
	
	private final JwtService jwtService;
	
	@PostMapping("/api/login")
	public String login(@RequestBody LoginDto loginDto) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
		
		return "\""+ jwtService.creatJwtToken((UserDetails)authentication.getPrincipal()) + "\"";
	}
}
