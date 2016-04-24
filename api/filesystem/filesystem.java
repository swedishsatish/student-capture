File system api:
    
    FileSystem {
        
        /**
         * stores assignment video at given parameteras
         *
         * @param courseCode the code for the course. 
         * @param courseID course id from the database
         * @param video the video to be stored at given course.
         * @return True if video was stored successfully 
         */
        public boolean storeAssignmentVideo(String courseCode, String courseID, String assignmentID, FileStream video);

        /**
         * get assignment video for a course.
         *
         * @param courseCode the code for the course. 
         * @param courseID course id from the database
         * @param assigmentId from database
         * @return a stream to the video
         */
        public FileStream getAssignmentVideo(String courseCode, String courseID, String assignmentID);
        
         /**
          * remove assignment from a course. 
          * 
          * @param courseCode the code for the course. 
          * @param courseID course id from the database
          * @param assignmentID
          */
        public boolean removeAssignment(String courseCode,String courseID, String assignmentID);

         /**
          * store the students video for an assignment at a course.
          * 
          * 
          * @param courseCode the code for the course. 
          * @param courseID course id from the database
          * @param assigmentId from database
          * @param userId from database
          * @return true if video was stored successfully
          */
        public boolean storeStudentVideo(String courseCode, String courseID, String assignmentID, String userID);

         /**
          * starts a stream to student video for a specific assignment at a course.
          * 
          * @parma courseCode the code for the course.
          * @param courseID course id from the database
          * @param userId
          * @return video or null if it doesn't exist. 
          */
        public FileStream getStudentVideo(String courseCode, String courseId, String assignmentID, String userID);
        
        /**
         * removes a student video from an assignment for a course.  
         *
         * @param courseCode the code for the course. 
         * @param courseID course id from the database
         * @param userId from database
         * @param true if video was removed succesfully else fail.
         */
        public boolean removeStudentVideo(String courseCode, String courseID, String assignmentID, String userID );

         /**
          *  store video feedback for the student for an assignment. 
          *
          * @param courseID course id from the database
          * @param assigmentId
          * @param userId
          * @parma feedback
          */
        public void storeFeedbackVideo(String courseCode, String courseID, String assignmentID,String userId, fileStream video);
        
        /**
         *  get feedback video for a student assignment.  
         *
         * @param courseCode
         * @param courseID
         * @param assignmentID
         * @param userID 
         */        
        public FileStream getFeedbackVideo(String courseCode, String courseID, String assignmentID,String userID);  

        /**
         * store feedback in text from teacher for the student, that is releated to the assignment.  
         *
         * @param courseCode the code for the course.
         * @param courseID course id from the database
         * @param assignmentID
         * @param userID
         */
        public void storeFeedbackText(String courseCode, String courseID, String assignmentID, String userID, File file);
        
         /**
          * get teachers feedback for the student from the assignment
          *
          * @param courseID course id from the database
          * @param assigmentId
          * @param userId
          * @return feedback as a string representaiton
          */
        public File getFeedbackText(String courseCode, String courseID, String assignmentID, String userId);       
    
    }


File structure:
 Course name
     Course id x
        Assigment id x 
            question.avi
            stud id x
            stud id y
                answer.avi feedback.avi feedback.xml   //feedback.xml contains a written feedback, and other informaiton related info.  
