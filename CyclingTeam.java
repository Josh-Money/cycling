package cycling;

import java.util.*;

public class CyclingTeam {

    private int teamId;
    private String name;
    private String description;
    private Map<Integer, CyclingRider> riders;
    private ArrayList<String> namesOfTeams;
    
    public CyclingTeam(int teamId, String name, String description) {
        this.teamId = teamId;
        this.name = name;
        this.description = description;
        namesOfTeams.add(name);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getTeamId() {
        return teamId;
    }

    public Map<Integer, CyclingRider> getRidersInTeam() {
        return riders;
    }

    public ArrayList<String> getNamesOfTeamsArray() {
        return namesOfTeams;
    }

    @Override
    public String toString() {
        return "CyclingTeam{" +
                "name=" + name +
                ", description=" + description +
                '}';
    }
}