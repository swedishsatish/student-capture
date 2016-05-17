package assignment;

import org.junit.Before;
import org.junit.Test;
import studentcapture.assignment.VideoIntervall;

import static org.junit.Assert.assertEquals;

/**
 * Created by David Björkstrand on 5/17/16.
 */
public class VideoIntervallTest {

    private VideoIntervall videoIntervall;

    @Before
    public void setUp() {
        videoIntervall = new VideoIntervall();
    }

    @Test
    public void shouldHaveMinTime120() {
        videoIntervall.setMinTimeSeconds(120);
        assertEquals(120, videoIntervall.getMinTimeSeconds());
    }

    @Test
    public void shouldHaveMaxTime320() {
        videoIntervall.setMaxTimeSeconds(320);
        assertEquals(320, videoIntervall.getMaxTimeSeconds());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenMinTimeIsLargerThanMaxTime() {
        videoIntervall.setMinTimeSeconds(320);
        videoIntervall.setMaxTimeSeconds(120);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenMinTimeIsLessThan0() {
        videoIntervall.setMinTimeSeconds(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenMaxTimeIs0() {
        videoIntervall.setMaxTimeSeconds(0);
    }
}
