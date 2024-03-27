package cycling;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class CyclingStage {
    
    private int raceId;
    private int stageId;
    private String stageName;
    private String description;
    private double length;
    private LocalDateTime startTime;
    private StageType type;
    private Map<Integer, Checkpoint> checkpoints = new HashMap<>();
    private StageState stageState;
    private Map<Integer, CyclingResult> results = new HashMap<>();

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
        this.checkpoints = checkpoints;
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
    
        // Get the keys (IDs) as an Integer array
        Integer[] keyArray = checkpoints.keySet().toArray(new Integer[checkpoints.size()]);
    
        // Convert Integer[] to int[]
        int[] ids = new int[keyArray.length];
        for (int i = 0; i < keyArray.length; i++) {
            ids[i] = keyArray[i]; // Autounboxing converts Integer to int
        }
    
        return ids;
    }

    public void deleteStageObj() {

    }

    public StageState getStageState() {
        return this.stageState;
    }

    public void setStageState(StageState stageState) {
        this.stageState = stageState;
    }

    public int addCheckpoint(int checkpointId, Checkpoint checkpoint) {

        checkpoints.put(checkpointId, checkpoint);

        return checkpointId;
    }

    public void removeCheckpointFromMap(int checkpointId) {
        // Removes checkpoint from the map checkpoints
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
        return getCheckpointIds().length;
    }

    public void addResults(int riderId, CyclingResult riderResult) {
        results.put(riderId, riderResult);
    }

    public void removeRiderFromResultsMap(int riderId) {
        results.remove(riderId);
    }

    public ArrayList<Integer> getRiderIdsWithResults() {
        ArrayList<Integer> riderIdList = new ArrayList<>(results.keySet());

        return riderIdList;
    }

    public ArrayList<Integer> getMountainCheckpoints() {
        ArrayList<Integer> climbCheckpointList = new ArrayList<>();
        for (Checkpoint checkpoint : checkpoints.values()) {
            double location = checkpoint.getLocation();
            int position = (int) location;
            if (checkpoint.getType() == CheckpointType.HC || checkpoint.getType() == CheckpointType.C1 
                || checkpoint.getType() == CheckpointType.C2 || checkpoint.getType() == CheckpointType.C3 
                || checkpoint.getType() == CheckpointType.C4) {
                climbCheckpointList.add(position);
            } 
        }
        return climbCheckpointList;
    }

    public ArrayList<Checkpoint> getMountainCheckpointObjects() {
        ArrayList<Checkpoint> climbCheckpointList = new ArrayList<>();
        for (Checkpoint checkpoint : checkpoints.values()) {
            if (checkpoint.getType() == CheckpointType.HC || checkpoint.getType() == CheckpointType.C1 
                || checkpoint.getType() == CheckpointType.C2 || checkpoint.getType() == CheckpointType.C3 
                || checkpoint.getType() == CheckpointType.C4) {
                climbCheckpointList.add(checkpoint);
            } 
        }
        return climbCheckpointList;
    }

    public ArrayList<Map<Integer, Duration>> calculateMountainCheckpointTimes(ArrayList<Integer> mountainCheckpoints) {

        ArrayList<Map<Integer, Duration>> mountainList = new ArrayList<>();

        for(int index : mountainCheckpoints) {
            Map<Integer, Duration> mountainCheckpointElapsedTimes = new HashMap<>();
            for (Map.Entry<Integer, CyclingResult> entry : results.entrySet()) {
                LocalTime[] checkpointTimes = entry.getValue().getCheckpointTimes();
                Duration duration = Duration.between(checkpointTimes[index - 2], checkpointTimes[index - 1]);
                mountainCheckpointElapsedTimes.put(entry.getKey(), duration);           
            }
            mountainList.add(mountainCheckpointElapsedTimes);
        }
        return mountainList;
    }

    public ArrayList<Integer> getSprintCheckpoints() {
        ArrayList<Integer> sprintCheckpoints = new ArrayList<>();

        for (Checkpoint checkpoint : checkpoints.values()) {
            double location = checkpoint.getLocation();
            int position = (int) location;
            if(checkpoint.getType() == CheckpointType.SPRINT) {
                sprintCheckpoints.add(position);
            }
        }
        return sprintCheckpoints;
    }

    public ArrayList<Map<Integer,Duration>> calculateSprintCheckpointTimes(ArrayList<Integer> sprintcheckpoints) {

        ArrayList<Map<Integer, Duration>> sprintList = new ArrayList<>();

        for (int index : sprintcheckpoints) {
            Map<Integer, Duration> sprintCheckpointElapsedTimes = new HashMap<>();
            for(Map.Entry<Integer, CyclingResult> entry : results.entrySet()) {
                LocalTime[] checkpointTimes = entry.getValue().getCheckpointTimes();
                Duration duration = Duration.between(checkpointTimes[index - 2], checkpointTimes[index - 1]);
                sprintCheckpointElapsedTimes.put(entry.getKey(), duration);
            }
            sprintList.add(sprintCheckpointElapsedTimes);
        }
        return sprintList;
    } 

    public int getMountainPointsForRider(int position, CheckpointType type) {
        if (type == CheckpointType.C4) {
            switch (position) {
                case 1:
                    return 1;
                default:
                    return 0;
            }
        } else if (type == CheckpointType.C3) {
            switch (position) {
                case 1:
                    return 2;
                case 2:
                    return 1;
                default:
                    return 0;
            }
        } else if (type == CheckpointType.C2) {
            switch (position) {
                case 1:
                    return 5;
                case 2:
                    return 3;
                case 3:
                    return 2;
                case 4: 
                    return 1;
                default:
                    return 0;
            }
        } else if (type == CheckpointType.C1) {
            switch (position) {
                case 1:
                    return 10;
                case 2:
                    return 8;
                case 3:
                    return 6;
                case 4: 
                    return 4;
                case 5: 
                    return 2;
                case 6:
                    return 1;
                default:
                    return 0;
            }
        } else if (type == CheckpointType.HC) {
            switch (position) {
                case 1:
                    return 20;
                case 2:
                    return 15;
                case 3:
                    return 12;
                case 4: 
                    return 10;
                case 5: 
                    return 8;
                case 6:
                    return 6;
                case 7:
                    return 4;
                case 8: 
                    return 2;
                default:
                    return 0;
            }
        }
        return position;
    }

    public int getSprintPoints(int position) {
        switch (position) {
            case 1:
                return 20;
            case 2:
                return 17;
            case 3: 
                return 15;
            case 4: 
                return 13;
            case 5: 
                return 11;
            case 6:
                return 10;
            case 7:
                return 9;
            case 8:
                return 8;
            case 9: 
                return 7;
            case 10: 
                return 6;
            case 11:
                return 5;
            case 12:
                return 4;
            case 13:
                return 3;
            case 14:
                return 2;
            case 15:
                return 1;
            default:
                return 0;
        }
    }

    public int getRiderPoints(int position) {
        if (this.type == StageType.FLAT) {
            switch (position) {
                case 1:
                    return 50;
                case 2:
                    return 30;
                case 3:
                    return 20;
                case 4:
                    return 18;
                case 5:
                    return 16;
                case 6:
                    return 14;
                case 7:
                    return 12;
                case 8:
                    return 10;
                case 9:
                    return 8;
                case 10:
                    return 7;
                case 11:
                    return 6;
                case 12:
                    return 5;
                case 13:
                    return 4;
                case 14:
                    return 3;
                case 15:
                    return 2;
                default:
                    return 0;
            }
        } else if (this.type == StageType.HIGH_MOUNTAIN) {
            switch (position) {
                case 1:
                    return 20;
                case 2:
                    return 17;
                case 3: 
                    return 15;
                case 4: 
                    return 13;
                case 5: 
                    return 11;
                case 6:
                    return 10;
                case 7:
                    return 9;
                case 8:
                    return 8;
                case 9: 
                    return 7;
                case 10: 
                    return 6;
                case 11:
                    return 5;
                case 12:
                    return 4;
                case 13:
                    return 3;
                case 14:
                    return 2;
                case 15:
                    return 1;
                default:
                    return 0;
            }
        } else if (this.type == StageType.TT) {
            switch (position) {
                case 1:
                    return 20;
                case 2:
                    return 17;
                case 3: 
                    return 15;
                case 4: 
                    return 13;
                case 5: 
                    return 11;
                case 6:
                    return 10;
                case 7:
                    return 9;
                case 8:
                    return 8;
                case 9: 
                    return 7;
                case 10: 
                    return 6;
                case 11:
                    return 5;
                case 12:
                    return 4;
                case 13:
                    return 3;
                case 14:
                    return 2;
                case 15:
                    return 1;
                default:
                    return 0;
            }
        } else if (this.type == StageType.MEDIUM_MOUNTAIN) {
            switch (position) {
                case 1:
                    return 30;
                case 2:
                    return 25;
                case 3: 
                    return 22;
                case 4: 
                    return 19;
                case 5: 
                    return 17;
                case 6:
                    return 15;
                case 7: 
                    return 13;
                case 8: 
                    return 11;
                case 9: 
                    return 9;
                case 10:
                    return 7;
                case 11:
                    return 6;
                case 12:
                    return 5;
                case 13:
                    return 4;
                case 14:
                    return 3;
                case 15:
                    return 2;
                default:
                    return 0;
            }
        }
        return position;
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
