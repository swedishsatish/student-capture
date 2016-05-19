package studentcapture.assignment;

import javax.validation.Valid;

/**
 * Created by David Björkstrand on 5/17/16.
 */
public class AssignmentVideoIntervall {

    private int minTimeSeconds;
    private boolean minTimeIsSet;
    private int maxTimeSeconds;
    private boolean maxTimeIsSet;

    public AssignmentVideoIntervall() {
        minTimeIsSet = false;
        maxTimeIsSet = false;
    }

    public void setMinTimeSeconds(int minTimeSeconds) throws  IllegalArgumentException {
        this.minTimeIsSet = true;
        this.minTimeSeconds = minTimeSeconds;
        this.validate();
    }

    public int getMinTimeSeconds() {
        return minTimeSeconds;
    }

    public void setMaxTimeSeconds(int maxTimeSeconds) throws IllegalArgumentException {
        this.maxTimeIsSet = true;
        this.maxTimeSeconds = maxTimeSeconds;
        this.validate();
    }

    public int getMaxTimeSeconds() {
        return maxTimeSeconds;
    }

    private void validate() throws IllegalArgumentException{
        if (minTimeIsSet && maxTimeIsSet) {
            if (minTimeSeconds >= maxTimeSeconds) {
                throw new IllegalArgumentException("Minimum video length must be less than " +
                        "max video length");
            }
        } else if (minTimeIsSet) {
            if (minTimeSeconds < 0) {
                throw new IllegalArgumentException("Minimum video length must be greater or " +
                        "equal to 0");
            }
        } else if (maxTimeIsSet) {
            if (maxTimeSeconds <= 0) {
                throw new IllegalArgumentException("Max video length must be greater " +
                        "than 0");
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AssignmentVideoIntervall that = (AssignmentVideoIntervall) o;

        if (minTimeSeconds != that.minTimeSeconds) return false;
        if (minTimeIsSet != that.minTimeIsSet) return false;
        if (maxTimeSeconds != that.maxTimeSeconds) return false;
        return maxTimeIsSet == that.maxTimeIsSet;

    }

    @Override
    public int hashCode() {
        int result = minTimeSeconds;
        result = 31 * result + (minTimeIsSet ? 1 : 0);
        result = 31 * result + maxTimeSeconds;
        result = 31 * result + (maxTimeIsSet ? 1 : 0);
        return result;
    }

}
