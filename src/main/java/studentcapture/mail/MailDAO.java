package studentcapture.mail;

import studentcapture.model.Assignment;

import java.util.ArrayList;

/**
 * Author C13evk
 */
public class MailDAO {
    private MailClient mailClient;
    private String from;
    private String subject;
    private String message;

    public void MailClient(){
        mailClient = new MailClient();
        from = "StudentCapture";
        subject = "Assignment Reminder";
        message = createMessage();
    }

    private boolean checkTime(Assignment assignment){

        return true;
    }

    private ArrayList<Assignment> getAssignments(){
        ArrayList<Assignment> assignmentList = new ArrayList<Assignment>();

        return assignmentList;
    }

    private ArrayList<String> getPraticipantsEmails(){
        ArrayList<String> emailList = new ArrayList<String>();

        return emailList;
    }

    private String createMessage(){
        return "Test";
    }

}
