package com.example.android.news;

/**
 * Created by ana on 21/06/2017.
 */

class News {

    // thumbnail image associated with the article
    private String Image;

    // Title of the article
    private String Title;

    // Person who wrote the article
    private String Author;

    // Section of the website the article is in
    private String Section;

    // Date the article was written
    private String Date;

    // Website URL of the article
    private String Url;

    /**
     * Constructs a new {@link News} object
     * @param image is the image associated with the article
     * @param title is the title of the article
     * @param author is the person who wrote the article
     * @param section is where the article is located
     * @param date is when it was first issued
     * @param url is the website URL to find more details about the article
     */
    News(String image, String title, String author, String section, String date, String url){
        Image = image;
        Title = title;
        Author = author;
        Section = section;
        Date = date;
        Url = url;
    }

    // Getter methods to return the image, title, author, section, date, and url of the article

    String getImage(){
        return Image;
    }

    String getTitle(){
        return Title;
    }

    String getAuthor(){
        return Author;
    }

    String getSection(){
        return Section;
    }

    String getDate(){
        return Date;
    }

    String getUrl() {
        return Url;
    }
}
