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

	private static final File FILE_FOLDER = Play.getFile("pictures");
	
	private static final String THUMBNAILS = "thumbnail";
	private static final String FULLSIZE = "fullsize";
	private static final String RESIZED = "resized";

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
    		Galleries.create(); // can generate error if the galleryId http parameter has not been passed
    	}
    	uploadPicture(file, galleryId, file.getName());
    	
    	Picture picture = new Picture();
    	picture.filename = file.getName();
    	picture.title = title == null ? "" : title;
    	picture.save();
    	
    	Gallery gallery = Gallery.findById(Long.valueOf(galleryId));
    	gallery.addPicture(picture);
    	gallery.save();
    	
    	System.out.println("Gallery = " + gallery.pictures.size());
    	
    	Galleries.show(galleryId);
    }
    
    @Util
    private static void uploadPicture(File file, String galleryId, String destination) {
    	File galleryFolder = new File(FILE_FOLDER, galleryId.toString());
    	
    	// Mini
    	File thumbnail = new File(galleryFolder, THUMBNAILS + File.separatorChar + destination);
    	Images.resize(file, thumbnail, 200, 150, true);

    	// Fixed size
    	File fixedSize = new File(galleryFolder, RESIZED + File.separatorChar + destination);
    	Images.resize(file, fixedSize, 940, 450, true);
    	
    	// Full size
    	File fullSize = new File(galleryFolder, FULLSIZE + File.separatorChar + destination);
    	Files.copy(file, fullSize);
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
    public static void thumbnail(String galleryId, String filename) {
     	File picture = new File(FILE_FOLDER, galleryId + File.separatorChar + THUMBNAILS + File.separatorChar + filename);
     	renderBinary(picture);
    }
    
    /**
     * Render a resized picture for a specific gallery.
     */
    public static void resized(String galleryId, String filename) {
     	File picture = new File(FILE_FOLDER, galleryId + File.separatorChar + RESIZED + File.separatorChar + filename);
     	renderBinary(picture);
    }
    
    /**
     * Render a fullsized picture for a specific gallery.
     */
    public static void fullsize(String galleryId, String filename) {
     	File picture = new File(FILE_FOLDER, galleryId + File.separatorChar + FULLSIZE + File.separatorChar + filename);
     	renderBinary(picture);
    }
}