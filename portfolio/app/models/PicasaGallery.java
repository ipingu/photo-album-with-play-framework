package models;

import javax.persistence.Entity;

import play.data.validation.Required;
import play.data.validation.URL;
import play.db.jpa.Model;

@Entity
public class PicasaGallery extends Model {
	@Required public String name;
	@Required @URL public String url;
	
	@Override
	public String toString() {
		return name;
	}
	
	
}
