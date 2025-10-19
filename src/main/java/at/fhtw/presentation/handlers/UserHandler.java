package at.fhtw.presentation.handlers;

import at.fhtw.converter.JsonConverter;
import at.fhtw.models.dtos.UserCredentials;
import at.fhtw.presentation.annotations.GET;
import at.fhtw.presentation.annotations.POST;
import at.fhtw.presentation.annotations.PUT;
import at.fhtw.presentation.models.Context;
import at.fhtw.presentation.models.Response;
import at.fhtw.services.AuthService;
import at.fhtw.services.UserService;
import com.google.inject.Inject;

public class UserHandler extends BaseHandler {
    private final UserService userService;
    private final AuthService authService;

    @Inject
    public UserHandler(
            UserService userService,
            AuthService authService
    ) {
        super("/api/users");
        this.userService = userService;
        this.authService = authService;
    }

    @GET(path = "/{userId}/favorites")
    protected void favorites(Context context) {
        System.out.println("Favorites: ");
    }

    @GET(path = "/{userId}/ratings")
    protected void ratings(Context context) {
        System.out.println("Ratings");
    }

    @GET(path = "/{userId}/profile")
    protected void getProfile(Context context) {
        System.out.println("Profile");
    }

    @PUT(path = "/{userId}/profile")
    protected void updateProfile(Context context) {
        System.out.println("Update Profile");
    }

    @GET(path = "/{userId}/recommendations")
    protected void recommendations(Context context) {
        System.out.println("Recommendations");
    }

    //Authentication

    @POST(path = "/login")
    protected void login(Context context) {
        JsonConverter<UserCredentials> converter = new JsonConverter<>(UserCredentials.class);
        UserCredentials userCredentials = converter.deserialize(context.getBody());
        Response response = authService.login(userCredentials);
        response.send(context.getHttpExchange());
    }

    @POST(path = "/register")
    protected void register(Context context) {
        JsonConverter<UserCredentials> converter = new JsonConverter<>(UserCredentials.class);
        UserCredentials userCredentials = converter.deserialize(context.getBody());
        Response response = authService.register(userCredentials);
        response.send(context.getHttpExchange());
    }
}
