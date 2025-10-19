package at.fhtw.presentation.handlers;

import at.fhtw.presentation.annotations.Auth;
import at.fhtw.presentation.annotations.DELETE;
import at.fhtw.presentation.annotations.POST;
import at.fhtw.presentation.annotations.PUT;
import at.fhtw.presentation.http.ContentType;
import at.fhtw.presentation.http.HttpStatus;
import at.fhtw.presentation.models.Context;
import at.fhtw.presentation.models.Response;
import at.fhtw.services.AuthService;
import com.google.inject.Inject;

public class RatingHandler extends BaseHandler {

    @Inject
    public RatingHandler(
        AuthService authService
    ) {
        super("/api/ratings", authService);
    }

    @Auth
    @POST(path = "/{ratingId}/like")
    protected void likeRating(Context context) {
        System.out.println("Like rating");
        new Response(
                HttpStatus.NOT_IMPLEMENTED,
                ContentType.PLAIN_TEXT,
                "Not implemented"
        ).send(context.getHttpExchange());
    }

    @Auth
    @PUT(path = "/{ratingId}")
    protected void updateRating(Context context) {
        System.out.println("Update rating");
        new Response(
                HttpStatus.NOT_IMPLEMENTED,
                ContentType.PLAIN_TEXT,
                "Not implemented"
        ).send(context.getHttpExchange());
    }

    @Auth
    @DELETE(path = "/{ratingId}")
    protected void deleteRating(Context context) {
        System.out.println("Delete rating");
        new Response(
                HttpStatus.NOT_IMPLEMENTED,
                ContentType.PLAIN_TEXT,
                "Not implemented"
        ).send(context.getHttpExchange());
    }

    @Auth
    @POST(path = "/{ratingId}/confirm")
    protected void confirmRating(Context context) {
        System.out.println("Confirm rating");
        new Response(
                HttpStatus.NOT_IMPLEMENTED,
                ContentType.PLAIN_TEXT,
                "Not implemented"
        ).send(context.getHttpExchange());
    }
}
