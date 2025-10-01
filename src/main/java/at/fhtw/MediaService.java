package at.fhtw;


import at.fhtw.models.Media;

public class MediaService {
    // Business Logic
    public boolean checkMedia(Media media) {
       return true;
    }

    // Service Logic
    public Media[] getAllMedia() {
        return new Media[0];
    }

    // Persistence
    public Media getMedia(int id) {
        return null;
    }

    public void saveMedia(Media media) {
        System.out.println("Media saved");
    }
}
