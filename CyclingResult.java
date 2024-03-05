package cycling;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CyclingResult {
    
    private int riderId;
    private int stageId;
    private List<LocalTime> checkpointTimes;
    // Total time for a rider in a stage
    private LocalTime[] totalElapsedTime;
    // Finishing position
    private int position;
    // Points earned by rider in the stage
    private int points;
    // Points earned by rider in the mountain summits
    private int mountainPoints;
    // Points earned by rider for intermediate sprint checkpoints
    private int sprintPoints;
    // If riders are less than a second apart, both riders get the fastest time of the two
    private LocalTime[] adjustedElapsedTime;
    // Map to store results for each stage

    public CyclingResult(int riderId, int stageId, List<LocalTime> checkpointTimes,
    LocalTime[] totalElapsedTime, int position, int points, int mountainPoints,
    int sprintPoints, LocalTime[] adjustedElapsedTime) {
        this.riderId = riderId;
        this.stageId = stageId;
        this.position = position;
        this.points = points;
        this.mountainPoints = mountainPoints;
        this.sprintPoints = sprintPoints;
        this.adjustedElapsedTime = calculateAdjustedElapsedTime(totalElapsedTime);
        this.totalElapsedTime = calculateTotalElapsedTime(checkpointTimes);
        stageResultsMap = new HashMap<>();


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

    public int getPosition() {
        return position;
    }

    public int getPoints() {
        return points;
    }

    public int getMountainPoints() {
        return mountainPoints;
    }

    public int getSprintPoints() {
        return sprintPoints;
    }

    public LocalTime getAdjustedElapsedTime() {
        return adjustedElapsedTime;
    }

    public LocalTime[] getTotalElapsedTime() {
        return totalElapsedTime;
    }

    public LocalTime[] getCheckpointTimes() {
        return Arrays.copyOf(checkpointTimes, checkpointTimes.length);
    }

    public void addCheckpointTimes(LocalTime... times) {
        for (LocalTime time : times) {
            checkpointTimes.add(time);
        }
    }

    private LocalTime[] calculateTotalElapsedTime(LocalTime[] checkpointTimes) {

        LocalTime[] result = new LocalTime[checkpointTimes.length];
        result[0] = checkpointTimes[0];
        for (int i = 1; i < checkpointTimes.length; i++) {
            result[i] = result[i - 1].plusSeconds(checkpointTimes[i].toSecondOfDay());
        }

        return result;
    }

    private LocalTime[] calculateAdjustedElapsedTime(LocalTime[] totalElapsedTime) {
        
        LocalTime[] adjustedTimes = new LocalTime[totalElapsedTime.length];
        adjustedTimes[0] = totalElapsedTime[0]; // Start time

        for (int i = 1; i < totalElapsedTime.length; i++) {
            Duration timeDifference = Duration.between(totalElapsedTime[i - 1], totalElapsedTime[i]);
        
            if (timeDifference.getSeconds() < 1) {
                adjustedTimes[i] = adjustedTimes[i - 1];
            } else {
                adjustedTimes[i] = totalElapsedTime[i];
            }
        }

        return adjustedTimes;
    }

    @Override
    public String toString() {
        return "CyclingResult{" +
                "riderId=" + riderId +
                ", stageId=" + stageId +
                ", checkpoint times=" + Arrays.toString(checkpointTimes) +
                ", position=" + position +
                ", points=" + points +
                ", mountain points=" + mountainPoints +
                ", sprint points=" + sprintPoints +
                ", adjusted elapsed time=" + Arrays.toString(totalElapsedTime) +
                ", total elapsed time=" + Arrays.toString(totalElapsedTime) +
                '}';
    }
}
