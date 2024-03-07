package cycling;

import java.time.LocalDateTime;
import java.util.*;


public class CyclingStage {
    
    private int raceId;
    private int stageId;
    private String stageName;
    private String description;
    private double length;
    private LocalDateTime startTime;
    private StageType type;
    private Map<Integer, Checkpoint> checkpoints;
    private StageState stageState;
    private Map<Integer, CyclingResult> results;

    public CyclingStage(int raceId, int stageId, String stageName, String description, double length, 
    LocalDateTime startTime, StageType type) {
        
        this.raceId = raceId;
        this.stageId = stageId;
        this.stageName = stageName;
        this.description = description;
        this.length = length;
        this.startTime = startTime;
        this.type = type;
    }

    public CyclingStage(int stageId, String stageName, String description, double length, 
    LocalDateTime startTime, StageType type, Map<Integer, Checkpoint> checkpoints, StageState stageState) {
        
        this.stageId = stageId;
        this.stageName = stageName;
        this.description = description;
        this.length = length;
        this.startTime = startTime;
        this.type = type;
        this.checkpoints = new ArrayList<>();
        this.stageState = stageState;
    }

    public int getRaceId() {
        return this.raceId;
    }

    public int getStageId() {
        return this.stageId;
    }

    public String getName() {
        return this.stageName;
    }

    public String getDescription() {
        return this.description;
    }

    public double getLength() {
        return this.length;
    }

    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    public StageType getType() {
        return this.type;
    }

    public  int[] getCheckpointIds() {
        Map<Integer, Checkpoint> map = this.checkpoints;
    
        // Get the keys (IDs) as an Integer array
        Integer[] keyArray = map.keySet().toArray(new Integer[0]);
    
        // Convert Integer[] to int[]
        int[] ids = new int[keyArray.length];
        for (int i = 0; i < keyArray.length; i++) {
            ids[i] = keyArray[i]; // Autounboxing converts Integer to int
        }
    
        return ids;
    }

    public StageState getStageState() {
        return this.stageState;
    }

    public void setStageState(StageState stageState) {
        this.stageState = stageState;
    }

    public int addCheckpoint(Checkpoint checkpoint) {
        
        double checkpointLocation = checkpoint.getLocation();

        int index = 0;
        while (index < checkpoints.size() && checkpoints.get(index).getLocation() < checkpointLocation) {
            index++;
        }
        
        checkpoints.put(index, checkpoint);

        int checkpointId = generateUniqueCheckpointId();

        return checkpointId;
    }

    public int generateUniqueCheckpointId() {
        // Creates unique checkpoint ID
        return checkpoints.size() + 1;
    }

    public void removeCheckpointFromMap(int checkpointId) {
        // Removes checkpoint from the map chekpoints
        checkpoints.remove(checkpointId);
    }

    public boolean isValidCheckpointId(int checkpointId) {
        if (checkpoints.containsKey(checkpointId)) {
            return true;
        }

        return false;
    }

    public int getNumberOfCheckpoints() {
        //Returns the number of checkpoints
        return getCheckpoints().size();
    }

    @Override
    public String toString() {
        return "CyclingStage{" +
                "stageId=" + stageId +
                ", name=" + stageName +
                ", description=" + description +
                ", length=" + length +
                ", startTime=" + startTime +
                ", type=" + type +
                ", checkpoint=" + checkpoints +
                '}';
    }
}
