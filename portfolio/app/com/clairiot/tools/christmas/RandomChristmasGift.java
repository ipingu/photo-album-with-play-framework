package com.clairiot.tools.christmas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RandomChristmasGift {
    
    List<Child> children = new ArrayList<Child>();
    
    public RandomChristmasGift() {
        super();
    }
    
    public static void main(String[] args) {
        RandomChristmasGift santa = new RandomChristmasGift();
        santa.addChild(new Child("erik@me.com", "Erik"),
                       new Child("joe@me.com", "Manon"), 
                       new Child("jane@me.com", "Marie"),
                       new Child("rogers@me.com", "Melissa"));
        Map<Child, Child> invitations = santa.getInvitations();
        santa.sendInvitations(invitations);
    }

    public void addChild(Child... childrenList) {
        for (Child child : childrenList) {
            if (children.contains(child)) {
                throw new IllegalArgumentException("Child already registered to this Christmas party");
            }
            children.add(child);
        }
    }

    public Map<Child, Child> getInvitations() {
        Collections.shuffle(children, new Random(System.nanoTime()));
        
        Map<Child,Child> invitations = new HashMap<Child,Child>();
        
        for (int i = 0; i < children.size() - 1; i++) {
            invitations.put(children.get(i), children.get(i+1));
        }
        invitations.put(children.get(children.size() - 1), children.get(0));

        return invitations;
    }

    public void sendInvitations(Map<Child, Child> invitations) {
        StringBuilder proofOfValidity = new StringBuilder();
        for (Map.Entry<Child,Child> entry : invitations.entrySet()) {
            proofOfValidity.append(entry.getKey().name + " will offer a gift to " + entry.getValue().secretName + "\n");
        }

        for (Map.Entry<Child,Child> entry : invitations.entrySet()) {
            String sender = entry.getKey().email;
            String messageContent = "New order from Santa Claus : ";
            messageContent += "Your mission is to send a gift to " + entry.getValue().name;
            messageContent += "\n\nChristmas gifts :\n";
            messageContent += proofOfValidity.toString();
            
            sendMail(sender, messageContent, "Christmas party : you received a message from Santa Claus !");
        }
    }

    private void sendMail(String sender, String messageContent,
                          String title) {
        
        System.out.println("Send mail to " + sender + " with content " + messageContent);
    }
}

class Child {
    
    private static int childIndex = 1;
    
    String email;
    String name;
    final int secretName = childIndex++;

    public Child(String email, String name) {
        super();
        this.email = email;
        this.name = name;
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Child)) {
            return false;
        }
        final Child other = (Child)object;
        if (!(email == null ? other.email == null : email.equals(other.email))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int PRIME = 37;
        int result = 1;
        result = PRIME * result + ((email == null) ? 0 : email.hashCode());
        return result;
    }
}
