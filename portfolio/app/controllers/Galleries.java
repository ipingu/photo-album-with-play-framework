package controllers;

import java.io.File;

import play.Logger;
import play.Play;
import play.data.validation.Required;
import play.mvc.Util;
import play.mvc.With;
import models.Gallery;
import models.Picture;

@With(Secure.class)
@CRUD.For(Gallery.class)
public class Galleries extends CRUD {

	public static void deletePicture(Long galleryId, Long pictureId) throws Exception {
    	Gallery gallery = Gallery.findById(Long.valueOf(galleryId));
    	notFoundIfNull(gallery);
    	
    	// remove from gallery
    	Picture picture = Picture.findById(pictureId);
    	notFoundIfNull(picture);
    	gallery.remove(picture);
    	gallery.save();

    	// delete file
    	picture.delete(galleryId);
    	
    	Galleries.show(galleryId.toString());
	}

	public static void changeName(Long galleryId, @Required String name) throws Exception {
    	Gallery gallery = Gallery.findById(Long.valueOf(galleryId));
    	notFoundIfNull(gallery);
    	
    	gallery.name = name;
    	gallery.save();

    	redirect("Galleries.show", galleryId.toString());
	}
	
	public static void uploadSinglePicture(@Required File file, @Required String galleryId, String title) throws Exception {
    	if (validation.hasErrors()) {
    		params.flash();
    		validation.keep();
    		Galleries.create(); // redirect to form creation
    	}
    
    	Picture picture = addFileToGallery(file, galleryId, title);
    	Logger.info("Picture %s uploaded in gallery %s", picture.id, galleryId);
    	
    	redirect("Galleries.show", galleryId.toString());
    }

	@Util
	public static Picture addFileToGallery(File file, String galleryId, String title) {
		Logger.info("Add picture to gallery %s", galleryId);
		// instanciate picture
    	Picture picture = new Picture();
    	picture.title = title == null ? "" : title;
    	picture.save();
    
    	Picture.upload(file, galleryId, picture.id.toString());
    	
    	// add picture to gallery
    	Gallery gallery = Gallery.findById(Long.valueOf(galleryId));
    	gallery.addPicture(picture);
    	gallery.save();
		return picture;
	}
	
}
