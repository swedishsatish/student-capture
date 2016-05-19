package studentcapture.mail;

import studentcapture.assignment.AssignmentModel;

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

    private boolean checkTime(AssignmentModel assignment){

        return true;
    }

    private ArrayList<AssignmentModel> getAssignments(){
        ArrayList<AssignmentModel> assignmentList = new ArrayList<AssignmentModel>();

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
