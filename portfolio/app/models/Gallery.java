package models;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import play.data.validation.Required;
import play.db.jpa.Model;
import play.libs.Files;
import play.templates.Template;
import play.utils.Utils;
import java.util.List;;

@Entity
public class Gallery extends Model {

	@Required public String name;

	@OneToMany @LazyCollection(LazyCollectionOption.TRUE)
	public List<Picture> pictures;
	
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
}
