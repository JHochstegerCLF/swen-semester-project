package at.fhtw.presentation.handlers;

import at.fhtw.presentation.models.Context;
import at.fhtw.presentation.annotations.DELETE;
import at.fhtw.presentation.annotations.GET;
import at.fhtw.presentation.annotations.POST;
import at.fhtw.presentation.annotations.PUT;

public class MediaHandler extends BaseHandler {
    public MediaHandler(

    ) {
        super("/api/media");
    }

    @GET(path = "/")
    protected void getAllMedia(Context context) {
        System.out.println("Get all media");
    }

    @POST(path = "/")
    protected void addMedia(Context context) {
        System.out.println("Add media");
    }

    @GET(path = "/{mediaId}")
    protected void getMedia(Context context) {
        System.out.println("Get media");
    }

    @PUT(path = "/{mediaId}")
    protected void updateMedia(Context context) {
        System.out.println("Update media");
    }

    @DELETE(path = "/{mediaId}")
    protected void deleteMedia(Context context) {
        System.out.println("Delete media");
    }

    @POST(path = "/{mediaId}/rate")
    protected void rateMedia(Context context) {
        System.out.println("Rate media");
    }

    @POST(path = "/{mediaId}/favorite")
    protected void favoriteMedia(Context context) {
        System.out.println("Favorite media");
    }

    @DELETE(path = "/{mediaId}/favorite")
    protected void unfavoriteMedia(Context context) {
        System.out.println("Unfavorite media");
    }
}
