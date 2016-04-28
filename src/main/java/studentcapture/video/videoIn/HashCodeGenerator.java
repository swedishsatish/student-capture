package studentcapture.video.videoIn;

/**
 * Created by c13vfm on 2016-04-27.
 */
public class HashCodeGenerator {

    // TODO: Make more complex
    protected static String generateHash(String userID){
        int hashCode = 13;
        hashCode = 31 * hashCode + (int)userID.charAt(1);
        hashCode = 31 * hashCode + (int)userID.charAt(2);
        hashCode = 31 * hashCode + (int)userID.charAt(3);
        hashCode = 31 * hashCode + (int)userID.charAt(0);
        hashCode = 31 * hashCode + (int)userID.charAt(3);
        hashCode = 31 * hashCode + (int)userID.charAt(2);
        hashCode = 31 * hashCode + (int)userID.charAt(1);

        String temp = Integer.toString(hashCode);

        return temp;
    }
}
