package at.fhtw.presentation.handlers;

import at.fhtw.converter.JsonConverter;
import at.fhtw.models.Media;
import at.fhtw.presentation.annotations.*;
import at.fhtw.presentation.http.ContentType;
import at.fhtw.presentation.http.HttpStatus;
import at.fhtw.presentation.models.Context;
import at.fhtw.presentation.models.Response;
import at.fhtw.services.AuthService;
import at.fhtw.services.MediaService;
import com.google.inject.Inject;

public class MediaHandler extends BaseHandler {
    MediaService mediaService;
    AuthService authService;


    @Inject
    public MediaHandler(
        MediaService mediaService,
        AuthService authService
    ) {
        super("/api/media", authService);
        this.mediaService = mediaService;
        this.authService = authService;
    }

    @Auth
    @GET(path = "/")
    protected void getAllMedia(Context context) {
        mediaService.getAllMedia(context.getQueryParams()).send(context.getHttpExchange());
    }

    @Auth
    @POST(path = "/")
    protected void addMedia(Context context) {
        JsonConverter<Media> jsonConverter = new JsonConverter<>(Media.class);
        Media media = jsonConverter.deserialize(context.getBody());
        media.setCreatorId(authService.getUserByToken(context.getToken()).getId());
        mediaService.addMedia(media).send(context.getHttpExchange());
    }

    @Auth
    @GET(path = "/{mediaId}")
    protected void getMedia(Context context) {
        int id = Integer.parseInt(context.getPathParams().get("mediaId"));
        mediaService.getMedia(id).send(context.getHttpExchange());
    }

    @Auth
    @PUT(path = "/{mediaId}")
    protected void updateMedia(Context context) {
        int id = Integer.parseInt(context.getPathParams().get("mediaId"));
        JsonConverter<Media> jsonConverter = new JsonConverter<>(Media.class);
        Media media = jsonConverter.deserialize(context.getBody());
        mediaService.updateMedia(id, media).send(context.getHttpExchange());
    }

    @Auth
    @DELETE(path = "/{mediaId}")
    protected void deleteMedia(Context context) {
        int id = Integer.parseInt(context.getPathParams().get("mediaId"));
        mediaService.deleteMedia(id).send(context.getHttpExchange());
    }

    @Auth
    @POST(path = "/{mediaId}/rate")
    protected void rateMedia(Context context) {
        System.out.println("Rate media");
        new Response(
                HttpStatus.NOT_IMPLEMENTED,
                ContentType.PLAIN_TEXT,
                "Not implemented"
        ).send(context.getHttpExchange());
    }

    @Auth
    @POST(path = "/{mediaId}/favorite")
    protected void favoriteMedia(Context context) {
        System.out.println("Favorite media");
        new Response(
                HttpStatus.NOT_IMPLEMENTED,
                ContentType.PLAIN_TEXT,
                "Not implemented"
        ).send(context.getHttpExchange());
    }

    @Auth
    @DELETE(path = "/{mediaId}/favorite")
    protected void unfavoriteMedia(Context context) {
        System.out.println("Unfavorite media");
        new Response(
                HttpStatus.NOT_IMPLEMENTED,
                ContentType.PLAIN_TEXT,
                "Not implemented"
        ).send(context.getHttpExchange());
    }
}
