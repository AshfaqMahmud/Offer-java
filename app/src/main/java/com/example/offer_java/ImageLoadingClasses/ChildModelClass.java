package com.example.offer_java.ImageLoadingClasses;

public class ChildModelClass {
    String image;
    String image_desc;

    public ChildModelClass(String image, String image_desc) {
        this.image = image;
        this.image_desc = image_desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage_desc() {
        return image_desc;
    }

    public void setImage_desc(String image_desc) {
        this.image_desc = image_desc;
    }
}
