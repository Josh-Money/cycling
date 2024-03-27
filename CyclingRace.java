package cycling;

import java.util.*;

public class CyclingRace {
    
    private int raceId;
    private String name;
    private String description;
    private ArrayList<CyclingStage> stages = new ArrayList<>();
    private int length;
    private ArrayList<String> namesOfStages = new ArrayList<>();

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

    public void deleteStageObject(int stageId) {
        Iterator<CyclingStage> iterator = stages.iterator();
        while (iterator.hasNext()) {
            CyclingStage stage = iterator.next();
            if (stage.getStageId() == stageId) {
                iterator.remove();
                break; 
            }
        }
    }
    

    public void deleteStageName(int stageId) {
        Iterator<String> iterator = namesOfStages.iterator();
        while (iterator.hasNext()) {
            String name = iterator.next();
            for (CyclingStage stage : stages) {
                if (name.equals(stage.getName())) {
                    iterator.remove(); 
                    break; 
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
