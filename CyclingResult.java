package cycling;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CyclingResult {
    
    private int riderId;
    private int stageId;
    private LocalTime[] checkpointTimes = new LocalTime[0];
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
    private LocalTime adjustedElapsedTime;
    // Map to store results for each stage
    private Map<Integer, LocalTime[]> stageResults = new HashMap<>();


    public CyclingResult(int riderId, int stageId, LocalTime... checkpointTimes) {
        this.riderId = riderId;
        stageResults.put(stageId, checkpointTimes);
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

 
    public LocalTime calculateTotalElapsedTime(int stageId) {
        // Retrieve the results for this stage
        LocalTime[] checkpointTimes = stageResults.get(stageId);

        // Finds the duration between first and last checkpoint
        Duration duration = Duration.between(checkpointTimes[0], checkpointTimes[checkpointTimes.length - 1]);

        // Creates a LocalTime variable which uses duration
        LocalTime elapsedTime = LocalTime.ofNanoOfDay(duration.toNanos());
        
        // Return the LocalTime Variable whihc represents the elapsed time of a rider in a stage
        return elapsedTime;      
    }

    public LocalTime calculateAdjustedElapsedTime(LocalTime[] totalElapsedTime) {

        LocalTime adjustedTime = totalElapsedTime[0];

        for (int i = 0; i < totalElapsedTime.length; i++) {

            Duration timeDifference = Duration.between(totalElapsedTime[i - 1], totalElapsedTime[i]);

            if(timeDifference.getSeconds() < 1) {
                adjustedTime = adjustedTime.plusSeconds(timeDifference.getSeconds());
            } else {
                adjustedTime = totalElapsedTime[i];
            }
        }

        return adjustedTime;
    }

    public LocalTime[] getStageCheckpointTimes(int stageId){
        return stageResults.get(stageId);
    }

    public void deleteStageResults(int stageId) {
        stageResults.remove(stageId);
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
