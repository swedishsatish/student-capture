package studentcapture.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Class for sending out reminder emails about upcoming assignments.
 *
 * @author Isak Hjelt, Emil Vannebäck
 * cs-user: dv14iht, c13evk
 * Date:        5/12/16
 */
@Repository
public class MailAssignmentReminder {
    @Autowired
    private MailDAO mailDAO;

    @Autowired
    MailClient mailClient;

    public void sendReminderEmail(String assignmentID) {
        String courseID;
        List<String> emailAddresses;
        Optional<List<String>> tempOptList;
        Optional <String> tempOptString;

        tempOptString = mailDAO.getCourseIDFromAssignment(assignmentID);

        if (tempOptString.isPresent()) {
            courseID = tempOptString.get();
            tempOptList = mailDAO.getPraticipantsEmails(courseID);

            if (tempOptList.isPresent()) {
                emailAddresses = tempOptList.get();

                for (String emailAddress : emailAddresses) {
                    mailClient.send(emailAddress, "studentcapture@cs.umu.se",
                            "Assignment Reminder", "Upcoming assignment starting: "
                                    + mailDAO.getStartDateFromAssignment(assignmentID).get() +
                                    " in course: " + courseID + " ends: " + ". Good luck, have fun!");
                }
            }
        }
    }
}

