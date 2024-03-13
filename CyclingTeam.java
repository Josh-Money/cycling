package cycling;

import java.util.*;

public class CyclingTeam {

    private int teamId;
    private String name;
    private String description;
    private Map<Integer, ArrayList<Integer>> riders;
    private ArrayList<String> namesOfTeams;
    
    public CyclingTeam(int teamId, String name, String description) {
        this.teamId = teamId;
        this.name = name;
        this.description = description;
        namesOfTeams.add(name);
        ArrayList<Integer> arrayList = null;
        riders.put(teamId, arrayList);
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public int getTeamId() {
        return this.teamId;
    }

    public int[] getRidersInTeam(int teamId) {
        ArrayList<Integer> arrayList = riders.get(teamId);
        int[] intArray = new int[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++) {
            intArray[i] = arrayList.get(i);
        }

        return intArray;
    }

    public ArrayList<String> getNamesOfTeamsArray() {
        return namesOfTeams;
    }

    public void deleteObj() {
        riders.remove(this.teamId);
    }

    public void addRider(int riderId) {
        ArrayList<Integer> arrayList = riders.get(this.teamId);
        riders.remove(this.teamId);
        arrayList.add(riderId);
        riders.put(this.teamId, arrayList);
    }
    public void removeRider(int riderId) {
        ArrayList<Integer> arrayList = riders.get(this.teamId);
        riders.remove(this.teamId);
        arrayList.remove(riderId);
        riders.put(this.teamId, arrayList);
    }

    @Override
    public String toString() {
        return "CyclingTeam{" +
                "name=" + name +
                ", description=" + description +
                '}';
    }
}