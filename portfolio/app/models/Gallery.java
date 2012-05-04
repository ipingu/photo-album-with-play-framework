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

	@OneToMany public List<Picture> pictures = new ArrayList<Picture>();
	
	@Override
	public String toString() {
		return name;
	}

	public void addPicture(Picture picture) {
		pictures.add(picture);
	}

	public void remove(Picture picture) {
		pictures.remove(picture);
	}
	
	public static Gallery create(String name) {
		Gallery gallery = new Gallery();
		gallery.name = name;
		gallery.save();
		
		File galleryFolder = new File(Application.FILE_FOLDER, gallery.id.toString());
		galleryFolder.mkdir();
	
		new File(galleryFolder, Application.THUMBNAILS).mkdir();
		new File(galleryFolder, Application.RESIZED).mkdir();
	
		return gallery;
	}
}
