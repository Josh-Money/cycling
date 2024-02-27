package cycling;

import java.time.LocalDateTime;

public class CyclingStage {
    
    private int stageId;
    private String stageName;
    private String description;
    private double length;
    private LocalDateTime startTime;
    private StageType type;
    private List<Checkpoint> checkpoints;
    private StageState stageState;

    public CyclingStage(int stageId, String stageName, String description, double length, 
    LocalDateTime startTime, StageType type, List<Checkpoint> checkpoints, StageState stageState) {
        
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

    public List<Checkpoint> getCheckpoints() {
        return checkpoints;
    }

    public StageState getStageState() {
        return stageState;
    }

    public int addCheckpoint(Checkpoint checkpoint) {
        int checkpointId = generateUniqueCheckpointId();

        checkpoints.add(checkpoint);

        return checkpointId;
    }

    public int generateUniqueCheckpointId() {
        // Creates unique checkpoint ID
        return checkpoints.size() + 1;
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
                ", checkpoint=" +
                '}';
    }
}
