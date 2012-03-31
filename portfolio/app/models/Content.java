package models;

import javax.persistence.Entity;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Content extends Model {

	@Required @MaxSize(200) public String title;
	public String html;
	
}
