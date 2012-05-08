package controllers;

import java.io.File;

import models.Gallery;
import models.PicasaGallery;
import models.Picture;
import play.Logger;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.db.Model;
import play.exceptions.TemplateNotFoundException;
import play.mvc.Util;
import play.mvc.With;

@With(Secure.class)
@CRUD.For(PicasaGallery.class)
public class PicasaGalleries extends CRUD {

}
