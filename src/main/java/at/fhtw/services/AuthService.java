package at.fhtw.services;

import at.fhtw.converter.JsonConverter;
import at.fhtw.models.User;
import at.fhtw.models.dtos.UserCredentials;
import at.fhtw.persistence.UserRepository;
import at.fhtw.presentation.http.ContentType;
import at.fhtw.presentation.http.HttpStatus;
import at.fhtw.presentation.models.Response;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.inject.Inject;

import java.util.Date;
import java.util.UUID;

public class AuthService {
    private final UserRepository userRepository;
    private final Algorithm algorithm;
    private final JWTVerifier verifier;
    private final String issuer = "MRP";

    @Inject
    public AuthService(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
        this.algorithm = Algorithm.HMAC256("MRP-HMAC-SHA256");
        this.verifier = JWT.require(algorithm)
                .withIssuer(issuer)
                .build();
    }

    public Response login(UserCredentials userCredentials) {
        Response error = new Response(
                HttpStatus.UNAUTHORIZED,
                ContentType.PLAIN_TEXT,
                "Login failed"
        );
        User user = userCredentials.toUser().hashPassword();
        User savedUser = userRepository.getUserByName(user.getUsername());
        if (savedUser == null) {
            return error;
        }
        if (!user.getPassword().equals(savedUser.getPassword())) {
            return error;
        }
        String token = JWT.create()
                .withIssuer(issuer)
                .withSubject(user.getUsername())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .withJWTId(UUID.randomUUID().toString())
                .withNotBefore(new Date(System.currentTimeMillis() + 1000))
                .sign(algorithm);
        JsonConverter<String> jsonConverter = new JsonConverter<>(String.class);
        return new Response(
                HttpStatus.OK,
                ContentType.JSON,
                jsonConverter.serialize(token)
        );
    }

    public Response register(UserCredentials userCredentials) {
        Response error = new Response(
                HttpStatus.CONFLICT,
                ContentType.PLAIN_TEXT,
                "User already exists"
        );
        if (userRepository.getUserByName(userCredentials.getUsername()) != null) {
            return error;
        }
        User user = userCredentials.toUser().hashPassword();
        user.setId(userRepository.getUsers().size());
        if (!userRepository.createUser(user)) {
            return error;
        }
        return new Response(
                HttpStatus.CREATED,
                ContentType.PLAIN_TEXT,
                "User registered"
        );
    }

    public boolean validateToken(String token) {
        try {
            DecodedJWT decodedJWT = verifier.verify(token);
            if (decodedJWT.getExpiresAt().before(new Date())) {
                return false;
            }
            if (decodedJWT.getNotBefore().after(new Date())) {
                return false;
            }
            if (!decodedJWT.getIssuer().equals(issuer)) {
                return false;
            }
            String userName = decodedJWT.getSubject();
            return userRepository.getUserByName(userName) != null;
        } catch (Exception e) {
            return false;
        }
    }

    public User getUserByToken(String token) {
        DecodedJWT decodedJWT = verifier.verify(token);
        String userName = decodedJWT.getSubject();
        return userRepository.getUserByName(userName);
    }

}
