package controllers;

import play.mvc.With;
import models.Gallery;
import models.Picture;

@With(Secure.class)
@CRUD.For(Gallery.class)
public class Galleries extends CRUD {

	public static void deletePicture(Long galleryId, Long pictureId) throws Exception {
		System.out.println("Delete " + galleryId + " / " + pictureId);
    	Gallery gallery = Gallery.findById(Long.valueOf(galleryId));
    	notFoundIfNull(gallery);
    	
    	Picture picture = Picture.findById(pictureId);
    	notFoundIfNull(picture);
    	gallery.remove(picture);
    	gallery.save();

    	picture.delete(galleryId);
    	
    	Galleries.show(galleryId.toString());
	}
	
}
