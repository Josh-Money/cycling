package cycling;

import java.time.LocalDateTime;

public class CyclingStage {
    
    private int stageId;
    private String stageName;
    private String description;
    private double length;
    private LocalDateTime startTime;
    private StageType type;

    public CyclingStage(int stageId, String stageName, String description, 
    double length, LocalDateTime startTime, StageType type) {
        
        this.stageId = stageId;
        this.stageName = stageName;
        this.description = description;
        this.length = length;
        this.startTime = startTime;
        this.type = type;
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

    @Override
    public String toString() {
        return "CyclingStage{" +
                "stageId=" + stageId +
                ", name=" + stageName +
                ", description=" + description +
                ", length=" + length +
                ", startTime=" + startTime +
                ", type=" + type +
                '}';
    }
}
