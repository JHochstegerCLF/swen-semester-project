package at.fhtw.services;

import at.fhtw.converter.JsonConverter;
import at.fhtw.mapper.IUserMapper;
import at.fhtw.models.User;
import at.fhtw.models.dtos.UserCredentialsDTO;
import at.fhtw.persistence.UserRepository;
import at.fhtw.presentation.http.ContentType;
import at.fhtw.presentation.http.HttpStatus;
import at.fhtw.presentation.models.LoginResponse;
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
    private final IUserMapper userMapper;
    private final Algorithm algorithm;
    private final JWTVerifier verifier;
    private final String issuer = "MRP";

    // handles everything connected to authentication, including registration and login
    @Inject
    public AuthService(
            UserRepository userRepository,
            IUserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.algorithm = Algorithm.HMAC256("MRP-HMAC-SHA256");
        this.verifier = JWT.require(algorithm)
                .withIssuer(issuer)
                .build();
    }

    public Response login(UserCredentialsDTO userCredentialsDTO) {
        // error response created before since used in both checks
        Response error = new Response(
                HttpStatus.UNAUTHORIZED,
                ContentType.PLAIN_TEXT,
                "Login failed"
        );
        User user = userCredentialsDTO.toUser().hashPassword();
        at.fhtw.models.entities.UserEntity savedUserEntity = userRepository.findByUsername(user.getUsername());
        if (savedUserEntity == null) {
            return error;
        }
        User savedUser = userMapper.fromEntity(savedUserEntity);
        // checks if user exists
        if (savedUser == null) {
            return error;
        }
        // checks if the password is correct
        if (!user.getPassword().equals(savedUser.getPassword())) {
            return error;
        }
        // creates the jwt token with necessary information
        String token = JWT.create()
                // who created the token
                .withIssuer(issuer)
                // who does the token belong to
                .withSubject(user.getUsername())
                // when was it issued
                .withIssuedAt(new Date())
                // when will it expire
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                // unique identifier
                .withJWTId(UUID.randomUUID().toString())
                // when can it be used
                .withNotBefore(new Date(System.currentTimeMillis() + 1000))
                // sign the token with secret so only the server can verify and create it
                .sign(algorithm);
        JsonConverter<LoginResponse> jsonConverter = new JsonConverter<>(LoginResponse.class);
        LoginResponse loginResponse = new LoginResponse(savedUser.getId(), token);
        return new Response(
                HttpStatus.OK,
                ContentType.JSON,
                jsonConverter.serialize(loginResponse)
        );
    }

    public Response register(UserCredentialsDTO userCredentialsDTO) {
        Response error = new Response(
                HttpStatus.CONFLICT,
                ContentType.PLAIN_TEXT,
                "User already exists"
        );
        if (userRepository.findByUsername(userCredentialsDTO.getUsername()) != null) {
            return error;
        }
        User user = userCredentialsDTO.toUser().hashPassword();
        if (userRepository.create(userMapper.toEntity(user)) == -1) {
            return error;
        }
        return new Response(
                HttpStatus.CREATED,
                ContentType.PLAIN_TEXT,
                "User registered"
        );
    }

    // checks if token is valid
    public boolean validateToken(String token) {
        try {
            DecodedJWT decodedJWT = verifier.verify(token);
            // checks for time validity
            if (decodedJWT.getExpiresAt().before(new Date())) {
                return false;
            }
            if (decodedJWT.getNotBefore().after(new Date())) {
                return false;
            }
            // checks for issuer
            if (!decodedJWT.getIssuer().equals(issuer)) {
                return false;
            }
            // checks if the user exists
            String userName = decodedJWT.getSubject();
            return userRepository.findByUsername(userName) != null;
        } catch (Exception e) {
            return false;
        }
    }

    // helper method to get user by token
    public User getUserByToken(String token) {
        DecodedJWT decodedJWT = verifier.verify(token);
        String userName = decodedJWT.getSubject();
        return userMapper.fromEntity(userRepository.findByUsername(userName));
    }

}
