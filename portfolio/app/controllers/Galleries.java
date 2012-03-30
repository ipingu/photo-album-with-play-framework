package controllers;

import play.mvc.With;
import models.Gallery;
import models.Picture;

@With(Secure.class)
@CRUD.For(Gallery.class)
public class Galleries extends CRUD {

	public static void deletePicture(Long galleryId, Long pictureId) {
    	Picture picture = Picture.findById(pictureId);
    	notFoundIfNull(picture);

    	picture.deleteFiles();
    	
    	Gallery gallery = Gallery.findById(Long.valueOf(galleryId));
    	notFoundIfNull(gallery);
    	
    	gallery.remove(picture);
	}
	
}
