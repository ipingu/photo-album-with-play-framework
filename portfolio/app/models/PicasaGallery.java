package models;

import javax.persistence.Entity;

import play.data.validation.Required;
import play.data.validation.URL;
import play.db.jpa.Model;

@Entity
public class PicasaGallery extends Model {
	
	@Required public String name;
	@Required public String user;
	@Required public String albumId;
//	https://picasaweb.google.com/data/feed/api/user/erik.clairiot/albumid/5663425536805995233?imgmax=800
	
	@Override
	public String toString() {
		return name;
	}
	
	public String getFeedUrl() {
		return "https://picasaweb.google.com/data/feed/api/user/" + user + "/albumid/" + albumId + "?imgmax=800";
	}
	
}
