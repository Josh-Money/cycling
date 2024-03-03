package cycling;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class CyclingStage {
    
    private int stageId;
    private String stageName;
    private String description;
    private double length;
    private LocalDateTime startTime;
    private StageType type;
    private Map<Integer, Checkpoint> checkpoints;
    private StageState stageState;
    private Map<Integer, CyclingResult> results;

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

    public int getStageId() {
        return stageId;
    }

    public String getName() {
        return stageName;
    }

    public String getDescription() {
        return description;
    }

    public double getLength() {
        return length;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public StageType getType() {
        return type;
    }

    public Map<Integer, Checkpoint> getCheckpoints() {
        return checkpoints;
    }

    public StageState getStageState() {
        return stageState;
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
        
        checkpoints.add(index, checkpoint);

        int checkpointId = generateUniqueCheckpointId();

        return checkpointId;
    }

    public int generateUniqueCheckpointId() {
        // Creates unique checkpoint ID
        return checkpoints.size() + 1;
    }

    public void removeCheckpointFromList(Checkpoint checkpoint) {
        // Removes checkpoint from the list chekpoints
        checkpoints.remove(checkpoint);
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
