package com.oshu.store.HelperClasses;

public class FeaturedHelperClass {
    private int id;
    private String image;
    private String title;
    private String description;

    public FeaturedHelperClass(int id, String image, String title, String description) {
        this.id = id;
        this.image = image;
        this.title = title;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
