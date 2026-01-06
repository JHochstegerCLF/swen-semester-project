package at.fhtw.presentation.handlers;

import at.fhtw.converter.JsonConverter;
import at.fhtw.models.dtos.UserCredentialsDTO;
import at.fhtw.models.dtos.UserDTO;
import at.fhtw.presentation.annotations.Auth;
import at.fhtw.presentation.annotations.GET;
import at.fhtw.presentation.annotations.POST;
import at.fhtw.presentation.annotations.PUT;
import at.fhtw.presentation.http.ContentType;
import at.fhtw.presentation.http.HttpStatus;
import at.fhtw.presentation.models.Context;
import at.fhtw.presentation.models.Response;
import at.fhtw.services.AuthService;
import at.fhtw.services.FavoriteService;
import at.fhtw.services.RatingService;
import at.fhtw.services.UserService;
import com.google.inject.Inject;

public class UserHandler extends BaseHandler {
    private final UserService userService;
    private final AuthService authService;
    private final FavoriteService favoriteService;
    private final RatingService ratingService;


    @Inject
    public UserHandler(
            UserService userService,
            AuthService authService,
            FavoriteService favoriteService,
            RatingService ratingService
    ) {
        super("/api/users", authService);
        this.userService = userService;
        this.authService = authService;
        this.favoriteService = favoriteService;
        this.ratingService = ratingService;
    }

    @Auth
    @GET(path = "/{userId}/favorites")
    protected void favorites(Context context) {
        int userId = Integer.parseInt(context.getPathParams().get("userId"));
        favoriteService.getFavorites(userId).send(context.getHttpExchange());
    }

    @Auth
    @GET(path = "/{userId}/ratings")
    protected void ratings(Context context) {
        int userId = Integer.parseInt(context.getPathParams().get("userId"));
        ratingService.getRatings(userId).send(context.getHttpExchange());
    }

    @Auth
    @GET(path = "/{userId}/profile")
    protected void getProfile(Context context) {
        int userId = Integer.parseInt(context.getPathParams().get("userId"));
        userService.getUser(userId).send(context.getHttpExchange());
    }

    @Auth
    @PUT(path = "/{userId}/profile")
    protected void updateProfile(Context context) {
        JsonConverter<UserDTO> jsonConverter = new JsonConverter<>(UserDTO.class);
        UserDTO user = jsonConverter.deserialize(context.getBody());
        user.setId(Integer.parseInt(context.getPathParams().get("userId")));
        int userId = authService.getUserByToken(context.getToken()).getId();
        userService.updateUser(userId, user).send(context.getHttpExchange());
    }

    @Auth
    @GET(path = "/{userId}/recommendations")
    protected void recommendations(Context context) {
        System.out.println("Recommendations");
        new Response(
                HttpStatus.NOT_IMPLEMENTED,
                ContentType.PLAIN_TEXT,
                "Not implemented"
        ).send(context.getHttpExchange());
    }

    public void getLeaderboard(Context context) {
        userService.getLeaderBoard().send(context.getHttpExchange());
    }

    //Authentication

    @POST(path = "/login")
    protected void login(Context context) {
        JsonConverter<UserCredentialsDTO> converter = new JsonConverter<>(UserCredentialsDTO.class);
        UserCredentialsDTO userCredentialsDTO = converter.deserialize(context.getBody());
        authService.login(userCredentialsDTO).send(context.getHttpExchange());
    }

    @POST(path = "/register")
    protected void register(Context context) {
        JsonConverter<UserCredentialsDTO> converter = new JsonConverter<>(UserCredentialsDTO.class);
        UserCredentialsDTO userCredentialsDTO = converter.deserialize(context.getBody());
        authService.register(userCredentialsDTO).send(context.getHttpExchange());
    }
}
