package at.fhtw.presentation.handlers;

import at.fhtw.converter.JsonConverter;
import at.fhtw.models.dtos.LikeDTO;
import at.fhtw.models.dtos.RatingDTO;
import at.fhtw.presentation.annotations.Auth;
import at.fhtw.presentation.annotations.DELETE;
import at.fhtw.presentation.annotations.POST;
import at.fhtw.presentation.annotations.PUT;
import at.fhtw.presentation.models.Context;
import at.fhtw.services.AuthService;
import at.fhtw.services.RatingService;
import com.google.inject.Inject;

public class RatingHandler extends BaseHandler {
    private final RatingService ratingService;
    private final AuthService authService;

    @Inject
    public RatingHandler(
            RatingService ratingService,
            AuthService authService
    ) {
        super("/api/ratings", authService);
        this.ratingService = ratingService;
        this.authService = authService;
    }

    @Auth
    @POST(path = "/{ratingId}/like")
    protected void likeRating(Context context) {
        LikeDTO like = new LikeDTO();
        like.setRating(Integer.parseInt(context.getPathParams().get("ratingId")));
        like.setUser(authService.getUserByToken(context.getToken()).getId());
        ratingService.addLike(like).send(context.getHttpExchange());
    }

    @Auth
    @PUT(path = "/{ratingId}")
    protected void updateRating(Context context) {
        JsonConverter<RatingDTO> jsonConverter = new JsonConverter<>(RatingDTO.class);
        RatingDTO rating = jsonConverter.deserialize(context.getBody());
        rating.setId(Integer.parseInt(context.getPathParams().get("ratingId")));
        rating.setCreatorId(authService.getUserByToken(context.getToken()).getId());
        ratingService.updateRating(rating, rating.getCreatorId()).send(context.getHttpExchange());
    }

    @Auth
    @DELETE(path = "/{ratingId}")
    protected void deleteRating(Context context) {
        int id = Integer.parseInt(context.getPathParams().get("ratingId"));
        int userId = authService.getUserByToken(context.getToken()).getId();
        ratingService.deleteRating(id, userId).send(context.getHttpExchange());
    }

    @Auth
    @POST(path = "/{ratingId}/confirm")
    protected void confirmRating(Context context) {
        int id = Integer.parseInt(context.getPathParams().get("ratingId"));
        int userId = authService.getUserByToken(context.getToken()).getId();
        ratingService.confirmRating(id, userId).send(context.getHttpExchange());
    }
}
