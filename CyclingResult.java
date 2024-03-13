package cycling;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CyclingResult {
    
    private int riderId;
    private int stageId;
    private LocalTime[] checkpointTimes;
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
    private Map<Integer, LocalTime[]> stageResults;


    public CyclingResult(int riderId, int stageId, LocalTime... checkpointTimes) {
        this.riderId = riderId;
        stageResults.put(stageId, checkpointTimes);
    }


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
        return Arrays.copyOf(checkpointTimes, checkpointTimes.size());
    }

 
    public LocalTime calculateTotalElapsedTime(int stageId) {
        LocalTime[] checkpointTimes = stageResults.get(stageId);
        Duration duration = Duration.between(checkpointTimes[0], checkpointTimes[checkpointTimes.length - 1]);
        LocalTime elapsedTime = LocalTime.ofNanoOfDay(duration.toNanos());
        return elapsedTime;      
    }

<<<<<<< Updated upstream
    private LocalTime[] calculateAdjustedElapsedTime(LocalTime totalElapsedTime) {
=======
<<<<<<< HEAD
    public LocalTime calculateAdjustedElapsedTime(LocalTime[] totalElapsedTime) {
=======
    private LocalTime[] calculateAdjustedElapsedTime(LocalTime totalElapsedTime) {
>>>>>>> b2fe8548a4c660acfcd76e42b877b281610bdb02
>>>>>>> Stashed changes
        
        LocalTime adjustedTime = totalElapsedTime[0];

        for (int i = 1; i < totalElapsedTime.length; i++) {
            Duration timeDifference = Duration
        }


        if (adjustedTime.isAfter())
        return adjustedTime;
    }
    public LocalTime[] getStageCheckpointTimes(int stageId){
        return stageResults.get(stageId);
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
