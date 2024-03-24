package cycling;

import java.time.LocalDateTime;
import java.util.*;

public class CyclingRace {
    
    private int raceId;
    private String name;
    private String description;
    private List<CyclingStage> stages;
    private int length;
    private ArrayList<String> namesOfStages;

    public CyclingRace(int raceId, String name, String description) {
        this.raceId = raceId;
        this.name = name;
        this.description = description;
    }

    public int getRaceId() {
        return this.raceId;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public List<CyclingStage> getStages() {
        return this.stages;
    }

    public int getLength() {
        for(CyclingStage stage : stages) {
            length +=stage.getLength();
        }
        return this.length;
    }

    public int addStage(CyclingStage stage){
        stages.add(stage);
        namesOfStages.add(stage.getName());

        return stage.getStageId();
    }

    public ArrayList<String> getNameOfStages() {
        return this.namesOfStages;
    }

    public void deleteStage(int stageId) {
        for(CyclingStage stage : stages) {
            if(stage.getStageId() == stageId) {
                stages.remove(stage);
                String stageName = stage.getName();
                for(String name : namesOfStages) {
                    if(name.equals(stageName)) {
                        namesOfStages.remove(stageName);
                    }
                }
            }
        }
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
