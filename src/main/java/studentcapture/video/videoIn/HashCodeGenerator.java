package studentcapture.video.videoIn;

/**
 * Created by c13vfm on 2016-04-27.
 * @deprecated Should not be used.
 */
public class HashCodeGenerator {

    // TODO: Make more complex
    protected static String generateHash(String userID){
        return Integer.toString(userID.hashCode());
    }
}
