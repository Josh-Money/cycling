package cycling;

import java.util.HashMap;
import java.util.Map;

public class CyclingRider {
    
    private int riderId;
    private int teamId;
    private String name;
    private int yearOfBirth;
    private Map<Integer, Integer> ridersInTeam = new HashMap<>();

    public CyclingRider(int riderId, int teamId, String name, int yearOfBirth) {
        this.riderId = riderId;
        this.teamId = teamId;
        this.name = name;
        this.yearOfBirth = yearOfBirth;
        ridersInTeam.put(riderId, teamId);
    }

    public int getRiderId() {
        return this.riderId;
    }

    public int getTeamId() {
        return this.teamId;
    }

    public String getName() {
        return this.name;
    }

    public int getYearOfBirth() {
        return this.yearOfBirth;
    }

    public Map<Integer, Integer> getRidersInTeam() {
        return ridersInTeam;
    }

    public void deleteRiderObject() {
        for (int riderId : ridersInTeam.keySet()) {
            if (riderId == this.riderId) {
                ridersInTeam.remove(riderId);
            }
        }
    }

    @Override
    public String toString() {
        return "CyclingRider{" +
                "riderId=" + riderId +
                ", teamId=" + teamId +
                ", name=" + name +
                ", yearOfBirth=" + yearOfBirth +
                '}';
    }
}
