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

	public static final File FILE_FOLDER = Play.getFile("pictures");
	
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
			showGallery(defaultGallery.id);
        } else {
        	render();
        }
    }
    
    public static void showGallery(Long id) {
    	Gallery gallery = Gallery.findById(id);
    	notFoundIfNull(gallery);
    	
    	render("gallery.html", gallery);
    }
    
    public static void uploadSinglePicture(@Required File file, @Required String galleryId, String title) throws Exception {
    	if (validation.hasErrors()) {
    		params.flash();
    		validation.keep();
    		Galleries.create(); 
    	}
    	
    	Picture.upload(file, galleryId, file.getName());
    	
    	Picture picture = new Picture();
    	picture.filename = file.getName();
    	picture.title = title == null ? "" : title;
    	picture.save();
    	
    	Gallery gallery = Gallery.findById(Long.valueOf(galleryId));
    	gallery.addPicture(picture);
    	gallery.save();
    	
    	Galleries.show(galleryId);
    }
    
    @Util
    public static void prepareFoldersForGallery(Gallery gallery) {
		File galleryFolder = new File(FILE_FOLDER, gallery.id.toString());
		galleryFolder.mkdirs();
	
		new File(galleryFolder, THUMBNAILS).mkdir();
		new File(galleryFolder, RESIZED).mkdir();
		new File(galleryFolder, FULLSIZE).mkdir();
    }

    /**
     * Render a thumbnail picture for a specific gallery.
     */
    public static void thumbnail(Long galleryId, Long pictureId) {
     	File picture = Picture.getFile(galleryId, pictureId, Application.THUMBNAILS);
     	if (! picture.exists()) notFound();
     	renderBinary(picture);
    }
    
    /**
     * Render a resized picture for a specific gallery.
     */
    public static void resized(String galleryId, String filename) {
     	File picture = new File(FILE_FOLDER, galleryId + File.separatorChar + RESIZED + File.separatorChar + filename);
     	if (! picture.exists()) notFound();
     	renderBinary(picture);
    }
    
    /**
     * Render a fullsized picture for a specific gallery.
     */
    public static void fullsize(Long galleryId, Long pictureId) {
     	File picture = Picture.getFile(galleryId, pictureId, Application.FULLSIZE);
     	if (picture == null || ! picture.exists()) notFound();
     	renderBinary(picture);
    }
}