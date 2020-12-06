package ca.novigrad;

import android.net.Uri;

public class Image {
    private Uri image;
    private String documentName;

    public  Image(Uri image, String documentName){
        this.documentName = documentName;
        this.image = image;
    }

    public Uri getImage() {
        return image;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }
}
