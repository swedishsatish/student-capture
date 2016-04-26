package studentcapture.feedback;

/**
 * Class:       BasicSanitizer
 * <p>
 * Author:      Erik Moström
 * cs-user:     erikm
 * Date:        4/26/16
 */
public class BasicSanitizer implements Sanitizer{

    @Override
    public String sanitize(String input) {
        return input.replaceAll("[^a-zA-Z0-9]", "");
    }
}
