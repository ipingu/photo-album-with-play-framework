package models;

import java.io.File;

import javax.persistence.Entity;

import controllers.Application;

import play.data.validation.Required;
import play.db.jpa.Model;
import play.libs.Files;
import play.libs.Images;
import play.mvc.Util;

@Entity
public class Picture extends Model {

	@Required public String filename;
	public String title;
	
	public void deleteFiles() {
		
	}
	
	public static void upload(File file, String galleryId, String destination) {
    	File galleryFolder = new File(Application.FILE_FOLDER, galleryId.toString());
    	
    	// Mini
    	File thumbnail = new File(galleryFolder, Application.THUMBNAILS + File.separatorChar + destination);
    	Images.resize(file, thumbnail, 200, 150, true);

    	// Fixed size
    	File fixedSize = new File(galleryFolder, Application.RESIZED + File.separatorChar + destination);
    	Images.resize(file, fixedSize, 940, 450, true);
    	
    	// Full size
    	File fullSize = new File(galleryFolder, Application.FULLSIZE + File.separatorChar + destination);
    	Files.copy(file, fullSize);
	}
	
	public static File getFile(Long galleryId, Long pictureId, String pictureType) {
		Picture picture = Picture.findById(pictureId);
		if (picture == null) return null;
		File file = new File(Application.FILE_FOLDER, galleryId.toString() + File.separatorChar + pictureType + File.separatorChar + picture.filename);
		return file;
	}
	
}
