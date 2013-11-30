package notifiers;

import models.Child;
import play.mvc.Mailer;

public class Mails extends Mailer {

	public static void sendMessageForChristmas(Child fromChild, Child toChild, String listOfGifts) {
		
		setSubject("MTL Christmas 2013 !");
		addRecipient(fromChild.getEmail());
		setFrom("erik.clairiot@gmail.com");
		
		send(fromChild, toChild, listOfGifts);
	}
}
