package models;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import controllers.Application;

import play.data.validation.Required;
import play.db.jpa.Model;
import play.libs.Files;
import play.mvc.Util;
import play.templates.Template;
import play.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;;

@Entity
public class Gallery extends Model {

	@Required public String name;
	
	@Override
	public String toString() {
		return name;
	}

	public static Gallery create(String name) {
		Gallery gallery = new Gallery();
		gallery.name = name;
		gallery.save();

		gallery.getGalleryFolder().mkdir();
	
		File thumbnailsPath = new File(gallery.getGalleryFolder(), Application.THUMBNAILS);
		thumbnailsPath.mkdir();

		File resizedsPath = new File(gallery.getGalleryFolder(), Application.RESIZED);
		resizedsPath.mkdir();
		
		return gallery;
	}
	
	private File getGalleryFolder() {
		return new File(Application.FILE_FOLDER, this.id.toString());
	}
	
	@Override
	public void _delete() {
		File thumbnailsPath = new File(this.getGalleryFolder(), Application.THUMBNAILS);
		thumbnailsPath.delete();

		File resizedsPath = new File(this.getGalleryFolder(), Application.RESIZED);
		resizedsPath.delete();

		this.getGalleryFolder().delete();

		for (Picture picture : getPictures())
			picture.delete();
		
		super._delete();
	}

	public List<Picture> getPictures() {
		return Picture.find("byGallery", this).fetch();
	}
}
