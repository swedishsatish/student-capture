package studentcapture.lti;

import studentcapture.feedback.FeedbackModel;

/**
 * Will take care of all the communication towards any LTI-related usage.
 * @version 0.1, 04/29/16
 */
public class LTICommunicator {

    /**
     * Using the feedback the grade will be set in the assignments feedback
     * grade.
     * @param feedback  The feedback that will be graded in the LMS.
     * @throws LTINullPointerException      No LTI is valid for this grading.
     * @throws LTIInvalidGradeException     The given grade in feedback was
     *                                      invalid in LMS.
     * @throws LTISignatureException        LTI communication not set up.
     */
    public static void setGrade(FeedbackModel feedback)
                throws LTINullPointerException, LTIInvalidGradeException,
                       LTISignatureException {

        //TODO: Find the LMS of the feedback.
        //TODO: Find the Student LMS token.
        //TODO: Set grade in LMS with LTI.
    }


}
