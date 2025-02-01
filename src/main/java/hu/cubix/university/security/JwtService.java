package hu.cubix.university.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import hu.cubix.university.model.UniversityUser;
import hu.cubix.university.model.UserProfile;
import hu.cubix.university.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtService {
    private static final String FACEBOOK_GRAPH_API_URL = "https://graph.facebook.com/me?fields=id,name,email&access_token=";

    private static final String AUTH = "auth";
    private final Algorithm alg = Algorithm.HMAC256("mysecret");
    private final String issuer = "UniversityApp";

    private final UserRepository userRepository;

    public String creatJwtToken(UserDetails principal) {
        return JWT.create()
                .withSubject(principal.getUsername())
                .withArrayClaim(AUTH, principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new))
                .withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(20)))
                .withIssuer(issuer)
                .sign(alg);

    }

    public UserDetails parseJwt(String jwtToken) {

        DecodedJWT decodedJwt = JWT.require(alg)
                .withIssuer(issuer)
                .build()
                .verify(jwtToken);
        return new User(decodedJwt.getSubject(), "dummy",
                decodedJwt.getClaim(AUTH).asList(String.class)
                        .stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
        );
    }

    public String createJwtTokenByFacebook(String accessToken) {
        UserProfile userProfile = getUserProfileByFacebookAccessToken(accessToken);
        if (userProfile == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Optional<UniversityUser> optional = userRepository.findByFacebookId(userProfile.getId());
        UniversityUser user = optional.orElseGet(() -> userRepository.save(new UniversityUser(userProfile.getEmail(), null, Set.of("user"), userProfile.getId())));

        return JWT.create()
                .withSubject(user.getUsername())
                .withArrayClaim(AUTH, user.getRoles().toArray(new String[0]))
                .withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(20)))
                .withIssuer(issuer)
                .sign(alg);
    }

    public UserProfile getUserProfileByFacebookAccessToken(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<UserProfile> response = restTemplate.getForEntity(FACEBOOK_GRAPH_API_URL + accessToken, UserProfile.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
}