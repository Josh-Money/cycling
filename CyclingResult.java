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
    // Map stageID to checkpointTimes
    private Map<Integer, LocalTime[]> stageResults = new HashMap<>();


    public CyclingResult(int riderId, int stageId, LocalTime... checkpointTimes) {
        this.riderId = riderId;
        this.stageId = stageId;
        stageResults.put(stageId, checkpointTimes);
        
        // Adds checkpoints in order that it is recieved
        this.checkpointTimes = new LocalTime[checkpointTimes.length];
        for (int i =0; i < checkpointTimes.length; i++) {
            this.checkpointTimes[i] = checkpointTimes[i];
        }
    }

    public void updateResults(int stageId, LocalTime... checkpoints) {
        stageResults.put(stageId, checkpoints);
    }

    public int getRiderId() {
        return riderId;
    }

    public int getStageId() {
        return stageId;
    }

    public LocalTime[] getCheckpointTimes() {
        // Converts LocalTime[] to Array
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
                '}';
    }
}
