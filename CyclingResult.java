package cycling;

import java.time.LocalTime;
import java.util.Arrays;

public class CyclingResult {
    
    private int riderId;
    private int stageId;
    private LocalTime[] checkpointTimes;

    public CyclingResult(int riderId, int stageId, LocalTime[] checkpointTimes) {
        this.riderId = riderId;
        this.stageId = stageId;

        this.checkpointTimes = new LocalTime[checkpointTimes.length];
        for (int i =0; i < checkpointTimes.length; i++) {
            this.checkpointTimes[i] = checkpointTimes[i];
        }
    }

    public int getRiderId() {
        return riderId;
    }

    public int getStageId() {
        return stageId;
    }

    public LocalTime[] getCheckpointTimes() {
        return Arrays.copyOf(checkpointTimes, checkpointTimes.length);
    }

    @Override
    public String toString() {
        return "CyclingResult{" +
                "riderId=" + riderId +
                ", stageId=" + stageId +
                ", checkpointTimes=" + Arrays.toString(checkpointTimes) +
                '}';
    }
}
