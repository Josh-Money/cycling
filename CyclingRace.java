package cycling;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CyclingRace {
    
    private int raceId;
    private String name;
    private String description;
    private List<CyclingStage> stages;
    private int length;

    public CyclingRace(int raceId, String name, String description) {
        this.raceId = raceId;
        this.name = name;
        this.description = description;
        this.stages = new ArrayList<>();
    }

    public int getRaceId() {
        return raceId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<CyclingStage> getStages() {
        return stages;
    }

    public int getLength() {
        return length;
    }

    public int addStage(CyclingStage stage){
        stages.add(stage);

        return stage.getStageId();
    }

    @Override
    public String toString() {
        return "CyclingRace{" +
                "raceId=" + raceId +
                ", name=" + name +
                ", description=" + description +
                ", stages=" + stages +
                ", length=" + length +
                '}';
    }
}
