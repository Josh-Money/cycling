package cycling;

import java.io.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import javax.naming.spi.DirStateFactory.Result;

public class CyclingPortalImpl implements CyclingPortal {

	private Map<Integer, CyclingRace> races;
	private Map<Integer, CyclingStage> stages;
	private Map<Integer, CyclingTeam> teams;
	private Map<Integer, CyclingRider> riders;
    private Map<Integer, CyclingResult> riderResults;

    @Override
	public int[] getRaceIds() {
		
		// Creates an integer list of raceIds
		int[] raceIds = new int[races.size()];

		// Iterates through the list of raceIds
		for (int i = 0; i < races.size(); i++) {
			raceIds[i] = races.get(i).getRaceId();
		}
		return raceIds;	
	}

	private int getNextRaceID() {
		
		// Increments the size of the list to create next raceID
		return races.size() + 1;
	}

	@Override
	public int createRace(String name, String description) throws IllegalNameException, InvalidNameException {
		
		// Generates unique race id 
		int raceId = getNextRaceID();
		
		// Constructor to create new race
		CyclingRace newRace = new CyclingRace(raceId, name, description);
		
		// Adds new race to the list 
		races.put(raceId, newRace);
		
		// Return the raceId
		return raceId;
	}

	@Override
	public String viewRaceDetails(int raceId) throws IDNotRecognisedException {
		
		// Gets raceId from list of races
		CyclingRace race = races.get(raceId);

		// Check if raceID is recognised
		if (race == null) {
			throw new IDNotRecognisedException("Race ID not recognised: " + raceId);
		}

		// Returns the details of the race
		return race.toString();
	}

	@Override
	public void removeRaceById(int raceId) throws IDNotRecognisedException {

		// Gets raceId from list of races
		CyclingRace race = races.get(raceId);

		// Check if raceID is recognised
		if (race == null) {
			throw new IDNotRecognisedException("Race ID not recognised: " + raceId);
		}		
		// Removes the race
		races.remove(raceId);
	}

	@Override
	public int getNumberOfStages(int raceId) throws IDNotRecognisedException {
		
		// Gets raceId from list of races
		CyclingRace race = races.get(raceId);

		// Check if raceID is recognised
		if (race == null) {
			throw new IDNotRecognisedException("Race ID not recognised: " + raceId);
		}
		
		// Returns the size of the stage list for that race .i.e. number of stages
		return race.getStages().size();
	}

	private int getNextStageId() {
		
		// Increments stageId to create a unique ID
		return stages.size() + 1;
	}

	@Override
	public int addStageToRace(int raceId, String stageName, String description, double length, LocalDateTime startTime,
			StageType type)
			throws IDNotRecognisedException, IllegalNameException, InvalidNameException, InvalidLengthException {
		
		// Gets raceId from list of races 
		CyclingRace race = races.get(raceId);

		// Generates stage id
		int stageId = getNextStageId();

		// Creates new stage using constructor
		CyclingStage newStage = new CyclingStage(raceId, stageId, stageName, description, length, startTime, type);

		// Check if raceId is not recognised
		if (race == null) {
			throw new IDNotRecognisedException("Race ID not recognised: " + raceId);
		}
		// Check if race name is already in use in the system
		for (String name : race.getNameOfStages()) {
			if (stageName.equals(name)) {
			throw new IllegalNameException("Name already in use");
			}
		}
		// Check if stage name is valid
		if (stageName == null || stageName.trim().isEmpty() || stageName.length() > 30 || stageName.contains(" ")){
			throw new InvalidNameException("Invalid stage name: " + stageName);
		}
		// Check if stage length is valid
		if (length < 5) {
			throw new InvalidLengthException("Invalid stage length: " + length);
		}

		

		// Adds stage to race
		race.addStage(newStage); //two parameters used when there is only one parameter in the method cycling.CyclingRace.AddStage()

		return stageId;
	}

	@Override
	public int[] getRaceStages(int raceId) throws IDNotRecognisedException {
		
		// intilaise race object 
		CyclingRace race = races.get(raceId);

		// Throw exception if raceId doesnt exist
		if (race == null) {
			throw new IDNotRecognisedException("Race Id not recognised: " + raceId);
		}

		// initialise list of stages in race
		List<CyclingStage> listOfStages = race.getStages();

		// get size of list 
		int size = listOfStages.size();

		// create integer array with size of stage list
		int[] stageIDArrayList = new int[size];

		// cycles through the list of stage and adds stageid for each stage to the integer array
		for (int i = 0; i < size; i++) {
			CyclingStage stage = listOfStages.get(i);
			int stageId = stage.getStageId();
			stageIDArrayList[i] = stageId;
		}

		return stageIDArrayList;
	}

	@Override
	public double getStageLength(int stageId) throws IDNotRecognisedException {
		
		CyclingStage stage = stages.get(stageId);

		if (stage == null) {
			throw new IDNotRecognisedException("Stage ID not recognised: " + stageId);
		}

		return stage.getLength();
	}

	@Override
	public void removeStageById(int stageId) throws IDNotRecognisedException {
		
		// Gets the stage object from the stage id 
		CyclingStage stage = stages.get(stageId);

		// Check if stageID is recognised
		if (stage == null) {
			throw new IDNotRecognisedException("Stage ID not recognised: " + stageId);
		}

		// gets raceId from stage
		int raceId = stage.getRaceId();

		// Finds object associated with raceid
		CyclingRace race = races.get(raceId);

		// Deletes stage from listOfstages and arrayList namesOfStages
		race.deleteStage(stageId);
		
		// Removes the stage from stage hashmap
		stages.remove(stageId);
	}

	@Override
	public int addCategorizedClimbToStage(int stageId, Double location, CheckpointType type, Double averageGradient,
			Double length) throws IDNotRecognisedException, InvalidLocationException, InvalidStageStateException,
			InvalidStageTypeException {
	
		// Finds the stage using stage Id
		CyclingStage stage = stages.get(stageId);

		// Verifies stage ID
		if (!stages.containsKey(stageId)) {
			throw new IDNotRecognisedException("Stage ID not recognised: " + stageId);
		}

		// Finds length of stage 
		length = stages.get(stageId).getLength();

		// Verfies location is valid
		if (location > length) {
			throw new InvalidLocationException("Location is longer than length of stage");
		}

		// Verifies stage is in the right state
		if (stage.getStageState() == StageState.WAITING_FOR_RESULTS) {
			throw new InvalidStageStateException("");
		}

		// Verifies that the stage is not a time-trial
		if (stage.getType() == StageType.TT) {
			throw new InvalidStageTypeException("Time-trials stages have no checkpoints.");
		}

		// Creates a new CategorisedClimbCheckpoint
		CatergorizedClimbCheckpoint newClimb = new CatergorizedClimbCheckpoint(location, type, averageGradient, length);

		// Adds the checkpoint to the stage and gives it a unique ID
		int checkpointId = stage.addCheckpoint(newClimb);

		return checkpointId;
	}

	@Override
	public int addIntermediateSprintToStage(int stageId, double location) throws IDNotRecognisedException,
			InvalidLocationException, InvalidStageStateException, InvalidStageTypeException {
		
		// Creates variable for the stage with the given stageId
		CyclingStage stage = stages.get(stageId);

		// Verifies stage ID
		if (!stages.containsKey(stageId)) {
			throw new IDNotRecognisedException("Stage ID not recognised: " + stageId);
		}

		// Verfies location is valid
		if (location > stages.get(stageId).getLength()) {
			throw new InvalidLocationException("Location is longer than length of stage");
		}

		// Verifies stage is in the right state
		if (stage.getStageState() == StageState.WAITING_FOR_RESULTS) {
			throw new InvalidStageStateException("Stage is waiting for results. ");
		}

		// Verifies that the stage is not a time-trial
		if (stage.getType() == StageType.TT) {
			throw new InvalidStageTypeException("Time-trials stages have no checkpoints.");
		}

		// Creates a new IntermediateSprintCheckpoint
		IntermediateSprintCheckpoint newSprint = new IntermediateSprintCheckpoint(location);

		// Adds checkpoint to stage and gives it unique ID
		int checkpointId = stage.addCheckpoint(newSprint);

		return checkpointId;

	}

	@Override
	public void removeCheckpoint(int checkpointId) throws IDNotRecognisedException, InvalidStageStateException {
		
		CyclingStage stage = findStageByCheckpointId(checkpointId);

		if (stage == null) {
			throw new IDNotRecognisedException("Checkpoint ID does not match any checkpoint in the system.");
		}
		if (stage.getStageState() == StageState.WAITING_FOR_RESULTS) {
			throw new InvalidStageStateException("Cannot modify stage that is waiting for results.");
		}
		stage.removeCheckpointFromMap(checkpointId);
	}

	
	private CyclingStage findStageByCheckpointId(int checkpointId) { 
		CyclingStage theStage = null;

		for (CyclingStage stage: stages.values()) {
			int[] checkpointArray = stage.getCheckpointIds();

			boolean containsCheckpoint = false;
			for (int i : checkpointArray) {
				if (i == checkpointId) {
					containsCheckpoint = true;
					break;
				}
			}

			if (containsCheckpoint) {
				theStage = stage;
				break;
			}
		}
		return theStage;
	}


	@Override
	public void concludeStagePreparation(int stageId) throws IDNotRecognisedException, InvalidStageStateException {

		CyclingStage stage = stages.get(stageId);

		// Verify stage Id
		if (!stages.containsKey(stageId)) {
			throw new IDNotRecognisedException("Stage Id is not recognised: " + stageId);
		}

		// Verifies stage state
		if (stage.getStageState() != StageState.WAITING_FOR_RESULTS) {
			throw new InvalidStageStateException("The stage is not in 'waiting for results' state.");
		}

		// Set stage state to 'Waiting for results'
		stage.setStageState(StageState.WAITING_FOR_RESULTS);
	}

	@Override
	public int[] getStageCheckpoints(int stageId) throws IDNotRecognisedException {
		CyclingStage stage = stages.get(stageId);

		// Verfies stage Id
		if (!stages.containsKey(stageId)) {
			throw new IDNotRecognisedException("Stage Id is not recognised: " + stageId);
		}

		// Retrieves the list of checkpoints which are in order from first to last
		return stage.getCheckpointIds();
	}

	private int getUniqueTeamId() {
		return teams.size() + 1;
	}

	@Override
	public int createTeam(String name, String description) throws IllegalNameException, InvalidNameException {
		
		// Generates new team ID
		int teamId = getUniqueTeamId();

		// Creates a new team 
		CyclingTeam newTeam = new CyclingTeam(teamId, name, description);

		// Verifies if team name is already in use
		for (String teamName : newTeam.getNamesOfTeamsArray()) {
			if (name.equals(teamName)) {
			throw new IllegalNameException("Name already in use");
			}
		}

		// Verifies that name is valid
		if (name == null || name.trim().isEmpty() || name.length() > 30 || name.contains(" ")){
			throw new InvalidNameException("Invalid team name: " + name);
		}

		// Adds to the teams hashmap
		teams.put(teamId, newTeam);

		return teamId;
	}

	@Override
	public void removeTeam(int teamId) throws IDNotRecognisedException {
		
		// Find team with the teamId
		CyclingTeam team = teams.get(teamId);
		
		// Creates an arrayList filled with riderId for the specific team
		ArrayList<CyclingRider> riderIdArray = new ArrayList<>();

		// Verifies TeamId 
		if (!teams.containsKey(teamId)) {
			throw new IDNotRecognisedException("TeamId not recognised: " + teamId);
		}

		// Removes the team from team hashmap
		teams.remove(teamId);

		// Removes the team object
		team.deleteObj();

		// Removes all rider associated with that team from the riders hashmap 
		for(Map.Entry<Integer, CyclingRider> entry : riders.entrySet()) {
			if(entry.getValue().getTeamId()==teamId){
				riders.remove(entry.getKey());
				riderIdArray.add(entry.getValue());
			}
		}
		// Also remove all rider objects associated with the team
		for(CyclingRider rider : riderIdArray) {
			rider.deleteRiderObject();
		}

	}

	@Override
	public int[] getTeams() {
		
		// Creates a new list to display all teamIDs
		List<Integer> teamIds = new ArrayList<>();

		// Loops through the teams hashmap adding the teamId to the list
		for (Integer team : teams.keySet()) {
			teamIds.add(team);
		}

		// Returns the list of teamIds
		return teamIds.stream().mapToInt(Integer::intValue).toArray();
	}

	@Override
	public int[] getTeamRiders(int teamId) throws IDNotRecognisedException {
		
		// Finds team associated with teamId
		CyclingTeam team = teams.get(teamId);

		// Verifies Id exists
		if(!teams.containsKey(teamId)) {
			throw new IDNotRecognisedException("teamId is not recognised: " + teamId);
		}

		// Returns riders in team
		return team.getRidersInTeam(teamId);
	}

	private int getNextRiderId() {
		// Generates unique rider Id
		return riders.size() + 1;
	}

	@Override
	public int createRider(int teamID, String name, int yearOfBirth)
			throws IDNotRecognisedException, IllegalArgumentException {
		
		// Generates unique rider Id
		int riderId = getNextRiderId();

		// Creates new rider
		CyclingRider newRider = new CyclingRider(riderId, teamID, name, yearOfBirth);
		// get team from teams hashmap
		CyclingTeam team = teams.get(teamID);

		// Verfies teamId
		if(!teams.containsKey(teamID)) {
			throw new IDNotRecognisedException("TeamId not recognised: " + teamID);
		}

		// Validate rider name and year of birth
		if (name == null || name.trim().isEmpty() || yearOfBirth < 1900) {
			throw new IllegalArgumentException("Invalid rider name or year of birth");
		}

		// Adds rider Id and details to riders hash map
		riders.put(riderId, newRider);

		// Adds rider to hashmap in team class called ridersInTeam
		team.addRider(riderId);

		return riderId;
	}

	@Override
	public void removeRider(int riderId) throws IDNotRecognisedException {
		
		// Finds rider using rider Id
		CyclingRider rider = riders.get(riderId);

		// Verfies rider Id 
		if (!riders.containsKey(riderId)) {
			throw new IDNotRecognisedException("rider Id not recognised: " + riderId);		
		}

		// Removes the rider from riders hashmap
		riders.remove(riderId);

		// Removes the rider object
		rider.deleteRiderObject();
		
		// Updates riders team without the rider
		int teamId = rider.getTeamId();
		CyclingTeam team = teams.get(teamId);
		team.removeRider(riderId);

		// Removes riders results
		riderResults.remove(riderId);
	}

	@Override
	public void registerRiderResultsInStage(int stageId, int riderId, LocalTime... checkpoints)
			throws IDNotRecognisedException, DuplicatedResultException, InvalidCheckpointTimesException,
			InvalidStageStateException {
		
		// Check if stageId and riderId are valid
        if (!stages.containsKey(stageId)) {
            throw new IDNotRecognisedException("Stage ID not recognised.");
        }
        if (!riders.containsKey(riderId)) {
            throw new IDNotRecognisedException("Rider ID not recognised.");
        }

        // Check for duplicated result
        if (riderResults.containsKey(riderId)) {
            throw new DuplicatedResultException("Rider already has a result for this stage.");
        }
		
		// Gets stage object from stage Id
		CyclingStage stage = stages.get(stageId);

        if (checkpoints.length != stage.getNumberOfCheckpoints() + 2) {
            throw new InvalidCheckpointTimesException("Invalid number of checkpoint times.");
        }

		// Gets result object from rider Id
		CyclingResult riderResult = new CyclingResult(riderId, stageId, checkpoints);
		
		//Maps the rider id to his results
		riderResults.put(riderId, riderResult);
		
		// Update the stage state to "results recorded"
		stage.setStageState(StageState.RESULTS_FINALISED);

	}

	@Override
	public LocalTime[] getRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
		
		// Verifies stage Id
		if (!stages.containsKey(stageId)) {
			throw new IDNotRecognisedException("Stage Id not recognised:" + stageId);
		}

		// Verifies rider Id 
		if (!riders.containsKey(riderId)) {
			throw new IDNotRecognisedException("Rider Id not recognised:" + riderId);
		}

		// Retrieve the CyclingResult object for the rider
		CyclingResult riderResult = riderResults.get(riderId);

		if (riderResult == null) {
			// If there are no results for the rider, return empty array
			return new LocalTime[0];
		} 

		// Finds the total elapsed time of the rider in that stage
		LocalTime elapsedTime  = riderResult.calculateTotalElapsedTime(stageId);

		// Gets the stage checkpoint times
		LocalTime[] checkpoints  = riderResult.getStageCheckpointTimes(stageId); 

		// Creates a new array which is the same as checkpoints but can fit the total elapsed time at the end
		LocalTime[] newCheckpoints =  Arrays.copyOf(checkpoints, checkpoints.length + 1);

		// Inserts the total elapsed time at the end of the array
		newCheckpoints[newCheckpoints.length - 1] = elapsedTime;

		// Returns the array of results of the specific stage
		return newCheckpoints;
	}

	@Override
	public LocalTime getRiderAdjustedElapsedTimeInStage(int stageId, int riderId) throws IDNotRecognisedException {
		
		// Verifies stage Id
		if (!stages.containsKey(stageId)) {
			throw new IDNotRecognisedException("Stage Id not recognised:" + stageId);
		}

		// Verifies rider Id 
		if (!riders.containsKey(riderId)) {
			throw new IDNotRecognisedException("Rider Id not recognised:" + riderId);
		}
		
		// Gets the results object for the specific rider
		CyclingResult riderResult  = riderResults.get(riderId);

		if (riderResult == null) {
			// If rider has no result then return null
			return null; 
		}

		// Calculates the total elapsed time of the rider in that stage
		LocalTime elapsedTime = riderResult.calculateTotalElapsedTime(stageId);

		// Creates array of all the riders total elapsed times
		LocalTime[] totalElapsedTimes = riderResult.getTotalElapsedTime();

		// Creates a copied array which has one more element
		LocalTime[] newElapsedTimes = Arrays.copyOf(totalElapsedTimes, totalElapsedTimes.length + 1);

		// Adds the riders total elapsed time to the new array 
		newElapsedTimes[newElapsedTimes.length - 1] = elapsedTime;

		// Returns the calculated adjusted elapsed time of the rider using the array of all the riders total elapsed time
		return riderResult.calculateAdjustedElapsedTime(newElapsedTimes);
	}

	@Override
	public void deleteRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
		
		// Verifies stage Id
		if (!stages.containsKey(stageId)) {
			throw new IDNotRecognisedException("Stage Id not recognised:" + stageId);
		}

		// Verifies rider Id 
		if (!riders.containsKey(riderId)) {
			throw new IDNotRecognisedException("Rider Id not recognised:" + riderId);
		}

		// Gets the results object for the specific rider
		CyclingResult riderResult  = riderResults.get(riderId);

		// Removes the stage results in the results object for the specific rider
		riderResult.deleteStageResults(stageId);
	}

	@Override
	public int[] getRidersRankInStage(int stageId) throws IDNotRecognisedException {
		
		// Verifies stage Id
		if (!stages.containsKey(stageId)) {
			throw new IDNotRecognisedException("Stage Id not recognised:" + stageId);
		}

		// Gets the list of results object for the specific stage
		CyclingStage stage = stages.get(stageId);

		// Get the adjusted elapsed time for all riders in the stage
		Map<Integer, LocalTime> riderAdjustedTimes = new HashMap<>();
		for (Map.Entry<Integer, CyclingResult> entry : riderResults.entrySet()) {
			int riderId = entry.getKey();
			CyclingResult result = entry.getValue();
			if (result.getStageCheckpointTimes(stageId)) {
				riderAdjustedTimes.put(riderId, getRiderAdjustedElapsedTimeInStage(stageId, riderId));
			}
		}

		// Sort results from fastest to slowest based on their adjusted elapsed time
		List<Map.Entry<Integer, LocalTime>> sortedRiders = new ArrayList<>(riderAdjustedTimes.entrySet());
		sortedRiders.sort(Map.Entry.comparingByValue());
		
		// Determine rank of each rider
		Map<Integer, Integer> riderRanks = new LinkedHashMap<>();
		int rank = 1;
		for (Map.Entry<Integer, LocalTime> entry : sortedRiders) {
			riderRanks.put(entry.getKey(), rank++);
		}

		return riderRanks; 
	}

	@Override
	public LocalTime[] getRankedAdjustedElapsedTimesInStage(int stageId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getRidersPointsInStage(int stageId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getRidersMountainPointsInStage(int stageId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void eraseCyclingPortal() {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveCyclingPortal(String filename) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadCyclingPortal(String filename) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeRaceByName(String name) throws NameNotRecognisedException {
		// TODO Auto-generated method stub

	}

	@Override
	public LocalTime[] getGeneralClassificationTimesInRace(int raceId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getRidersPointsInRace(int raceId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getRidersMountainPointsInRace(int raceId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getRidersGeneralClassificationRank(int raceId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getRidersPointClassificationRank(int raceId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getRidersMountainPointClassificationRank(int raceId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

}
