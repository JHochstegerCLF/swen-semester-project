package at.fhtw.presentation.handlers;

import at.fhtw.presentation.models.Context;
import at.fhtw.presentation.annotations.DELETE;
import at.fhtw.presentation.annotations.POST;
import at.fhtw.presentation.annotations.PUT;

public class RatingHandler extends BaseHandler {
    public RatingHandler(String initialPath) {
        super(initialPath);
    }

    @POST(path = "/{ratingId}/like")
    protected void likeRating(Context context) {
        System.out.println("Like rating");
    }

    @PUT(path = "/{ratingId}")
    protected void updateRating(Context context) {
        System.out.println("Update rating");
    }

    @DELETE(path = "/{ratingId}")
    protected void deleteRating(Context context) {
        System.out.println("Delete rating");
    }

    @POST(path = "/{ratingId}/confirm")
    protected void confirmRating(Context context) {
        System.out.println("Confirm rating");
    }
}
