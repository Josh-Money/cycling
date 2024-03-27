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
    // Maps checkpoint Id to Checkpoint instance
    private Map<Integer, Checkpoint> checkpoints = new HashMap<>();
    private StageState stageState;
    // Maps rider Id to RiderResult instance
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


    public int getRaceId() {
        return this.raceId;
    }

    public int getStageId() {
        return this.stageId;
    }

    public double getLength() {
        return this.length;
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

    public StageState getStageState() {
        return this.stageState;
    }

    public void setStageState(StageState stageState) {
        this.stageState = stageState;
    }

    public int addCheckpoint(int checkpointId, Checkpoint checkpoint) {

        //Updates checkpoints hashmap wiht new checkpoint
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
        // Updates results hashmap with new RiderResult instance
        results.put(riderId, riderResult);
    }

    public void removeRiderFromResultsMap(int riderId) {
        // Removes key value pair from results with associated rider Id 
        results.remove(riderId);
    }

    public ArrayList<Integer> getRiderIdsWithResults() {

        // Returns the list of riders that contain results from the results keyset
        ArrayList<Integer> riderIdList = new ArrayList<>(results.keySet());

        return riderIdList;
    }

    public ArrayList<Integer> getMountainCheckpoints() {
        // Initialises new Integer ArrayList
        ArrayList<Integer> climbCheckpointList = new ArrayList<>();
        // Iterates through each Checkpoint object in checkpoints hashmap
        for (Checkpoint checkpoint : checkpoints.values()) {
            // Gets the location of the checkpoint through Checkpoint class method
            double location = checkpoint.getLocation();
            int position = (int) location;
            // Checks if the checkpoint is any of the mountain types
            if (checkpoint.getType() == CheckpointType.HC || checkpoint.getType() == CheckpointType.C1 
                || checkpoint.getType() == CheckpointType.C2 || checkpoint.getType() == CheckpointType.C3 
                || checkpoint.getType() == CheckpointType.C4) {
                // If condition is satisfied the climbcheckpoint list is updated with the position of the mountain checkpoint
                climbCheckpointList.add(position);
            } 
        }
        // Returns list of mountain checkpoints positionss
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
        // Initialises new ArraList of hashmaps
        ArrayList<Map<Integer, Duration>> mountainList = new ArrayList<>();

        // Iterates through the mountain checkpoints positions 
        for(int index : mountainCheckpoints) {
            // Initialises new Hashmap
            Map<Integer, Duration> mountainCheckpointElapsedTimes = new HashMap<>();
            // Iterates through the key value pair in the results hashmap
            for (Map.Entry<Integer, CyclingResult> entry : results.entrySet()) {
                // Gets the checkpointTimes for each checkpoint object in the hashmap
                LocalTime[] checkpointTimes = entry.getValue().getCheckpointTimes();
                // Gets the duration between the mountain checkpoint and the next one in the checkpoint list
                Duration duration = Duration.between(checkpointTimes[index - 2], checkpointTimes[index - 1]);
                // Updates the HashMap with the rider Id mapped to elapsed time for the mountain checkpoint
                mountainCheckpointElapsedTimes.put(entry.getKey(), duration);           
            }
            // Adds the hashmap to the mountain list ArrayList
            mountainList.add(mountainCheckpointElapsedTimes);
        }
        return mountainList;
    }

    public ArrayList<Integer> getSprintCheckpoints() {

        // Initialises new ArrayList
        ArrayList<Integer> sprintCheckpoints = new ArrayList<>();
        // Iterates through the Checkpoint objects in the checkpoints hashmap
        for (Checkpoint checkpoint : checkpoints.values()) {
            // Gets the location of the checkpoint
            double location = checkpoint.getLocation();
            // Converts double variable to int 
            int position = (int) location;
            // Checks weather checkpoint is type sprint
            if(checkpoint.getType() == CheckpointType.SPRINT) {
                // If condition is met updates sprintCheckpoints ArrayList with position
                sprintCheckpoints.add(position);
            }
        }
        // Returns list of the locations of the sprint checkpoints
        return sprintCheckpoints;
    }

    public ArrayList<Map<Integer,Duration>> calculateSprintCheckpointTimes(ArrayList<Integer> sprintcheckpoints) {
        // Initialises ArrayList of hashmaps
        ArrayList<Map<Integer, Duration>> sprintList = new ArrayList<>();
        // Iterates through the positions of the sprint checkpoints
        for (int index : sprintcheckpoints) {
            // Initialises new hashmap
            Map<Integer, Duration> sprintCheckpointElapsedTimes = new HashMap<>();
            // Iterates through the results hashmap key value pair
            for(Map.Entry<Integer, CyclingResult> entry : results.entrySet()) {
                // Gets the checkpoint times for the stage
                LocalTime[] checkpointTimes = entry.getValue().getCheckpointTimes();
                // Gets the duration of the sprint checkpoint
                Duration duration = Duration.between(checkpointTimes[index - 2], checkpointTimes[index - 1]);
                // Updates the hashmap with the rider id and the duration of the sprint checkpoint
                sprintCheckpointElapsedTimes.put(entry.getKey(), duration);
            }
            // Adds the hashmap to the ArrayList
            sprintList.add(sprintCheckpointElapsedTimes);
        }
        return sprintList;
    } 

    public int getMountainPointsForRider(int position, CheckpointType type) {
        // Checks checkpoint type
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
        //Returns the points for each position based on position in checkpoint
        }
    }

    public int getRiderPoints(int position) {
        // Checks the stage type of the stage
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
        // Returns the position based on the position and stage type
        }
        return position;
    }

    @Override
    public String toString() {
        // Returns string of object instance infomation
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
