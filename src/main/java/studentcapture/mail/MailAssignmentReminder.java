package studentcapture.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Class for sending out reminder emails about upcoming assignments.
 * This class is meant to be used by the MailScheduler class.
 *
 * @author Isak Hjelt, Emil Vannebäck
 *         cs-user: dv14iht, c13evk
 *         Date:        5/12/16
 */
@Repository
public class MailAssignmentReminder{

    @Autowired
    private MailDAO mailDAO;

    @Autowired
    MailClient mailClient;

    public void sendReminderEmail(String assignmentID) {
        String courseID;
        List<String> emailAddresses;
        Optional<List<String>> tempOptList;
        Optional<String> tempOptString;

        tempOptString = mailDAO.getCourseIDFromAssignment(assignmentID);

        if (tempOptString.isPresent()) {
            courseID = tempOptString.get();
            tempOptList = mailDAO.getPraticipantsEmails(courseID);

            if (tempOptList.isPresent()) {
                emailAddresses = tempOptList.get();

                for (String emailAddress : emailAddresses) {
                    Optional<Date> assignmentStartDate = mailDAO.getStartDateFromAssignment(assignmentID);

                    if (assignmentStartDate.isPresent()) {
                        mailClient.send(emailAddress, "studentcapture@cs.umu.se",
                                "Assignment Reminder", "Upcoming assignment starting: "
                                        + assignmentStartDate.get() +
                                        " in course: " + courseID + " ends: " + ". Good luck, have fun!");
                    }
                }
            }
        }
    }

    /*Add @EnableScheduling in the spring config to make this run. Also there is no function to remove already
    * reminded assignments*/
    @Scheduled(fixedRate=500)
    public void scheduledSendToAll(){
        List<String> AssignmentIdList;
        Optional <List<String>>optional;

        optional = mailDAO.getNotificationList();
        if(optional.isPresent()){
            AssignmentIdList = optional.get();

            for (String assId : AssignmentIdList) {
                sendReminderEmail(assId);
            }
        }

    }
}

