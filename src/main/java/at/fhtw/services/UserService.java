package at.fhtw.services;

import at.fhtw.models.dtos.UserCredentials;
import at.fhtw.persistence.UserRepository;
import at.fhtw.presentation.http.ContentType;
import at.fhtw.presentation.http.HttpStatus;
import at.fhtw.presentation.models.Response;
import com.google.inject.Inject;
import lombok.AllArgsConstructor;

@AllArgsConstructor(onConstructor_ = @Inject)
public class UserService {
    private final UserRepository userRepository;



}
