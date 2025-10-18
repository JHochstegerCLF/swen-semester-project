package at.fhtw.presentation.handlers;

import at.fhtw.presentation.models.Context;
import at.fhtw.presentation.annotations.GET;
import at.fhtw.presentation.annotations.POST;
import at.fhtw.presentation.annotations.PUT;

public class UserHandler extends BaseHandler {

    public UserHandler(String initialPath) {
        super(initialPath);
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
        System.out.println("Login");
    }

    @POST(path = "/register")
    protected void register(Context context) {
        System.out.println("Register");
    }
}
