package studentcapture.video.videoIn;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by c13vfm on 2016-05-04.
 */
public class HashCodeGeneratorTest {

    private String firstUser = "Arnold Schwarzenegger";
    private String secUser = "Kalle Anka";

    @Test
    public void shouldGiveAHash() {
        assertNotNull(HashCodeGenerator.generateHash(firstUser));
    }

    @Test
    public void shouldGiveSameHashOnSameString() throws Exception {
        assertEquals(HashCodeGenerator.generateHash(firstUser), HashCodeGenerator.generateHash(firstUser));
    }

    @Test
    public void shouldGiveDifferentHashOnDifferentStrings() {
        assertNotEquals(HashCodeGenerator.generateHash(firstUser), HashCodeGenerator.generateHash(secUser));
    }

}