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

    public List<CyclingStage> getStages() {
        return this.stages;
    }

    public ArrayList<String> getNameOfStages() {
        return this.namesOfStages;
    }
    
    // Finds total length of race
    public int getLength() {
        for(CyclingStage stage : stages) {
            length +=stage.getLength();
        }
        return this.length;
    }

    // Adds stage to both ArrayLists
    public int addStage(CyclingStage stage){
        stages.add(stage);
        namesOfStages.add(stage.getName());

        return stage.getStageId();
    }

    // Deletes stage from stages ArrayList
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
    
    // Deletes stage from nameOfStages ArrayList
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
