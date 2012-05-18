package models;

import java.io.File;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import controllers.Application;

import play.data.validation.Required;
import play.db.jpa.Model;
import play.libs.Files;
import play.libs.Images;
import play.mvc.Util;

@Entity
public class Picture extends Model {

	@OneToOne public Gallery gallery;
	public String title;
	
	@Override
	public void _delete() {
		File mini = getFile(gallery.id, id, Application.THUMBNAILS);
		mini.delete();
		
		File resized = getFile(gallery.id, id, Application.RESIZED);
		resized.delete();
		
		super._delete();
	}
	
	public void upload(File file) {
    	File galleryFolder = new File(Application.FILE_FOLDER, gallery.id.toString());
    	
    	// Mini
    	File thumbnail = new File(galleryFolder, Application.THUMBNAILS + File.separatorChar + this.id);
    	Images.resize(file, thumbnail, 200, 150, true);

    	// Fixed size
    	File fixedSize = new File(galleryFolder, Application.RESIZED + File.separatorChar + this.id);
    	Images.resize(file, fixedSize, 940, 450, true);
	}
	
	public static File getFile(Long galleryId, Long pictureId, String pictureType) {
		File file = new File(Application.FILE_FOLDER, galleryId.toString() + File.separatorChar + pictureType + File.separatorChar + pictureId.toString());
		return file;
	}
	
	public ImageView toImageView() {
		ImageView image = new ImageView();
		image.thumbnail = "/public/pictures/" + gallery.id.toString() + "/" + Application.THUMBNAILS + "/" + id.toString();
		image.url = "/public/pictures/" + gallery.id.toString() + "/" + Application.RESIZED + "/" + id.toString();
		image.title = title;
		return image;
	}
	
}
