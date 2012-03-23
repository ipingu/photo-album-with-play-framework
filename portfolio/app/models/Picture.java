package models;

import javax.persistence.Entity;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Picture extends Model {

	@Required public String filename;
	public String title;
	
}
