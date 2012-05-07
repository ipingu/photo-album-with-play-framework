package models;

import javax.persistence.Entity;
import javax.persistence.Lob;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Blob;
import play.db.jpa.Model;

@Entity
public class Content extends Model {

	@Required @MaxSize(200) public String title;
	@Lob @MaxSize(10000) public String html;
	public Blob image;
}
