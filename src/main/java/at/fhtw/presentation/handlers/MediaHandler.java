package at.fhtw.presentation.handlers;

import at.fhtw.converter.JsonConverter;
import at.fhtw.models.dtos.FavoriteDTO;
import at.fhtw.models.dtos.MediaDTO;
import at.fhtw.models.dtos.RatingDTO;
import at.fhtw.presentation.annotations.*;
import at.fhtw.presentation.models.Context;
import at.fhtw.services.AuthService;
import at.fhtw.services.FavoriteService;
import at.fhtw.services.MediaService;
import at.fhtw.services.RatingService;
import com.google.inject.Inject;

public class MediaHandler extends BaseHandler {
    private final MediaService mediaService;
    private final AuthService authService;
    private final RatingService ratingService;
    private final FavoriteService favoriteService;


    @Inject
    public MediaHandler(
            MediaService mediaService,
            AuthService authService,
            RatingService ratingService,
            FavoriteService favoriteService
    ) {
        // initialized super constructor with initialPath and authService (@Inject doesnt work if needed to be called manually)
        super("/api/media", authService);
        // injected services
        this.mediaService = mediaService;
        this.authService = authService;
        this.ratingService = ratingService;
        this.favoriteService = favoriteService;
    }

    @Auth
    @GET(path = "/")
    protected void getAllMedia(Context context) {
        mediaService.getAllMedia(context.getQueryParams()).send(context.getHttpExchange());
    }

    @Auth
    @POST(path = "/")
    protected void addMedia(Context context) {
        JsonConverter<MediaDTO> jsonConverter = new JsonConverter<>(MediaDTO.class);
        MediaDTO media = jsonConverter.deserialize(context.getBody());
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
        JsonConverter<MediaDTO> jsonConverter = new JsonConverter<>(MediaDTO.class);
        MediaDTO media = jsonConverter.deserialize(context.getBody());
        media.setId(Integer.parseInt(context.getPathParams().get("mediaId")));
        mediaService.updateMedia(media).send(context.getHttpExchange());
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
        JsonConverter<RatingDTO> jsonConverter = new JsonConverter<>(RatingDTO.class);
        RatingDTO rating = jsonConverter.deserialize(context.getBody());
        rating.setMediaId(Integer.parseInt(context.getPathParams().get("mediaId")));
        rating.setCreatorId(authService.getUserByToken(context.getToken()).getId());
        ratingService.addRating(rating).send(context.getHttpExchange());
    }

    @Auth
    @POST(path = "/{mediaId}/favorite")
    protected void favoriteMedia(Context context) {
        FavoriteDTO favorite = new FavoriteDTO();
        favorite.setMedia(Integer.parseInt(context.getPathParams().get("mediaId")));
        favorite.setUser(authService.getUserByToken(context.getToken()).getId());
        favoriteService.addFavorite(favorite).send(context.getHttpExchange());
    }

    @Auth
    @DELETE(path = "/{mediaId}/favorite")
    protected void unfavoriteMedia(Context context) {
        FavoriteDTO favorite = new FavoriteDTO();
        favorite.setMedia(Integer.parseInt(context.getPathParams().get("mediaId")));
        favorite.setUser(authService.getUserByToken(context.getToken()).getId());
        favoriteService.deleteFavorite(favorite).send(context.getHttpExchange());
    }
}
