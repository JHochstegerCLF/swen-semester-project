package at.fhtw;


import at.fhtw.models.Rating;

public class RatingService {
    // Business Logic
    public boolean checkRating(Rating rating) {
       return true;
    }

    // Service Logic
    public Rating[] getAllRatings() {
        return new Rating[0];
    }

    // Persistence
    public Rating getRating(int id) {
        return null;
    }

    public void saveRating(Rating rating) {
        System.out.println("Rating saved");
    }
}
