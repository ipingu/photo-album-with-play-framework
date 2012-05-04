package controllers;

import play.*;
import play.data.validation.Required;
import play.db.jpa.JPABase;
import play.libs.Files;
import play.libs.Images;
import play.mvc.*;
import play.utils.Utils;

import java.io.File;
import java.io.FileFilter;
import java.util.*;

import models.*;

public class Application extends Controller {

	public static final File FILE_FOLDER = Play.getFile("public/pictures/");
	
	public static final String THUMBNAILS = "thumbnail";
	public static final String FULLSIZE = "fullsize";
	public static final String RESIZED = "resized";

	@Util
	public static List<Gallery> getGalleries() {
    	return Gallery.all().fetch();
	}
	
    public static void index() {
        if (Gallery.count() > 0) {
        	Gallery defaultGallery = Gallery.all().first();
			showGallery(defaultGallery.id, defaultGallery.name);
        } else {
        	render();
        }
    }
    
    public static void showGallery(Long id, String name) {
    	notFoundIfNull(id);
    	Gallery gallery = Gallery.findById(id);
    	notFoundIfNull(gallery);
    	
    	render("Application/gallery.html", gallery);
    }
    
    public static void showContents() {
    	List<Content> contents = Content.findAll();
    	render(contents);
    }

    public static void thumbnail(Long galleryId, Long pictureId) {
     	File picture = Picture.getFile(galleryId, pictureId, Application.THUMBNAILS);
     	if (! picture.exists()) notFound();
     	renderBinary(picture);
    }
    
    public static void resized(Long galleryId, Long pictureId) {
    	File picture = Picture.getFile(galleryId, pictureId, Application.RESIZED);
       	if (! picture.exists()) notFound();
     	renderBinary(picture);
    }
}