package controllers;

import java.io.File;

import controllers.CRUD.ObjectType;

import play.Logger;
import play.Play;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.db.Model;
import play.exceptions.TemplateNotFoundException;
import play.mvc.Util;
import play.mvc.With;
import models.Gallery;
import models.Picture;

@With(Secure.class)
@CRUD.For(Gallery.class)
public class Galleries extends CRUD {

	public static void deleteGallery(Long id) {
		Gallery gallery = Gallery.findById(id);
		notFoundIfNull(gallery);
		
		gallery.delete();
		
		CRUD.index();
	}
	
	public static void createGallery(@Required @MinSize(1) String name) throws Exception {
		if (validation.hasErrors()) {
			params.flash();
			validation.keep();
			blank();
		}
		
		Gallery gallery = Gallery.create(name);
		
		Galleries.show(gallery.id.toString());
	}

	public static void show(String id) throws Exception {
        ObjectType type = ObjectType.get(getControllerClass());
        notFoundIfNull(type);
        Model object = type.findById(id);
        notFoundIfNull(object);
        try {
            render(type, object);
        } catch (TemplateNotFoundException e) {
            render("CRUD/show.html", type, object);
        }
    }
	
	public static void deletePicture(Long pictureId) throws Exception {
    	Picture picture = Picture.findById(pictureId);
    	notFoundIfNull(picture);

    	Long galleryId = picture.gallery.id;
    	picture.delete();
    	
    	Galleries.show(galleryId.toString());
	}

	public static void changeName(Long galleryId, @Required String name) throws Exception {
		if (validation.hasErrors()) {
			validation.keep();
			params.flash();
			Galleries.show(galleryId.toString());
		}
		
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
    		Galleries.show(galleryId);
    	}
    
    	Picture picture = addFileToGallery(file, Long.valueOf(galleryId), title);
    	Logger.info("Picture %s uploaded in gallery %s", picture.id, galleryId);
    	
    	redirect("Galleries.show", galleryId.toString());
    }

	@Util
	public static Picture addFileToGallery(File file, Long galleryId, String title) {
		Logger.info("Add picture to gallery %s", galleryId);
		Gallery gallery = Gallery.findById(Long.valueOf(galleryId));

		Picture picture = new Picture();
    	picture.title = title == null ? "" : title;
    	picture.gallery = gallery;
    	picture.save();
    
    	picture.upload(file);
    	
		return picture;
	}
	
	
	
}
