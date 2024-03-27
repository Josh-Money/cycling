package cycling;

import java.util.*;

public class CyclingTeam {

    private int teamId;
    private String name;
    private String description;
    // Maps teamID to ArrayList of riderIDs
    private Map<Integer, ArrayList<Integer>> riders = new HashMap<>();
    private ArrayList<String> namesOfTeams = new ArrayList<>();
    
    public CyclingTeam(int teamId, String name, String description) {
        this.teamId = teamId;
        this.name = name;
        this.description = description;
        ArrayList<Integer> listOfRiders = new ArrayList<>();
        riders.put(teamId, listOfRiders);
    }

    public int getTeamId() {
        return this.teamId;
    }

    // Converts ArrayList to int[] 
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

    public void addTeamName() {
        namesOfTeams.add(name);
    }

    // Deletes team object
    public void deleteObj() {
        riders.remove(this.teamId);
    }

    // Adds rider Id to ListOfRiders which then is added to the riders hashmap
    public void addRider(int riderId) {
        ArrayList<Integer> listOfRiders = riders.get(this.teamId);
        listOfRiders.add(riderId);
        riders.put(this.teamId, listOfRiders);
    }

    //Removes rider from the riders hashmap, only if the ListOfRiders is not empty
    public void removeRider(int riderId) {
        ArrayList<Integer> listOfRiders = riders.get(this.teamId);
        if (listOfRiders != null) {
            listOfRiders.remove(Integer.valueOf(riderId));
            riders.put(this.teamId, listOfRiders);
        }
    }

    @Override
    public String toString() {
        return "CyclingTeam{" +
                "name=" + name +
                ", description=" + description +
                '}';
    }
}