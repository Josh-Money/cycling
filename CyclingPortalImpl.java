package cycling;

import java.io.*;
import java.time.*;
import java.util.*;

public class CyclingPortalImpl implements CyclingPortal {

	private Map<Integer, CyclingRace> races = new HashMap<>();
	private Map<Integer, CyclingStage> stages = new HashMap<>();
	private Map<Integer, CyclingTeam> teams = new HashMap<>();
	private Map<Integer, CyclingRider> riders = new HashMap<>();
    private Map<Integer, CyclingResult> riderResults = new HashMap<>();
	private Map<Integer, Map<LocalTime, Integer>> stageElapsedTimes = new HashMap<>();
	private int raceCounter = 0;
	private int stageCounter = 0;
	private int teamCounter = 0;
	private int riderCounter = 0;

	public CyclingStage getStageById(int stageId) throws IDNotRecognisedException {
    	if (!stages.containsKey(stageId)) {
        	throw new IDNotRecognisedException("Stage ID not recognised: " + stageId);
    	}
    	return stages.get(stageId);
	}

	public CyclingRace getRaceById(int raceId) throws IDNotRecognisedException {
    	if (!stages.containsKey(raceId)) {
        	throw new IDNotRecognisedException("Stage ID not recognised: " + raceId);
    	}
    	return races.get(raceId);
	}

	public CyclingTeam getTeamById(int teamId) throws IDNotRecognisedException {
    	if (!teams.containsKey(teamId)) {
        	throw new IDNotRecognisedException("Stage ID not recognised: " + teamId);
    	}
    	return teams.get(teamId);
	}

	public CyclingRider getRiderById(int riderId) throws IDNotRecognisedException {
    	if (!riders.containsKey(riderId)) {
        	throw new IDNotRecognisedException("Stage ID not recognised: " + riderId);
    	}
    	return riders.get(riderId);
	}

    @Override
	public int[] getRaceIds() {
		
		Set<Integer> keySet = races.keySet();

		Integer[] keyArray = keySet.toArray(new Integer[0]);

		int[] raceIds = new int[keyArray.length];
		for (int i = 0; i < keyArray.length; i++) {
			raceIds[i] = keyArray[i];
		}
		return raceIds;	
	}

	private int getNextRaceID() {
		
		// Increments the size of the list to create next raceID
		return raceCounter += 1;
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

		for (CyclingStage stage : race.getStages()) {
			int stageId = stage.getStageId();
			stageElapsedTimes.remove(stageId);
			race.deleteStage(stageId);
			for(int riderId : riderResults.keySet()) {
				CyclingResult result = riderResults.get(riderId);
				if(result.getStageId() == stageId) {
					riderResults.remove(riderId);
				}
			}
		}
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
		return stageCounter += 1;
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
		stages.put(stageId, newStage);

		// Adds stage to race
		race.addStage(newStage); 

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

		if (!stages.containsKey(stageId)) {
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
		double stageLength = stages.get(stageId).getLength();

		// Verfies location is valid
		if (location > stageLength) {
			throw new InvalidLocationException("Location is longer than length of stage");
		}

		// Verifies stage is in the right state
		if (stage.getStageState() == StageState.WAITING_FOR_RESULTS) {
			throw new InvalidStageStateException("Stage is waiting for results.");
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
		Checkpoint newSprintCheckpoint = new Checkpoint(location, CheckpointType.SPRINT);

		// Adds checkpoint to stage and gives it unique ID
		int checkpointId = stage.addCheckpoint(newSprintCheckpoint);

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
		if (stage.getStageState() == StageState.WAITING_FOR_RESULTS) {
			throw new InvalidStageStateException("The stage is already waiting for results.");
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
		return teamCounter += 1;
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

		newTeam.addTeamName();

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
		return riderCounter += 1;
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

		// Removes rider form results map in stage class
		for(CyclingStage stage : stages.values()) {
			stage.removeRiderFromResultsMap(riderId);
		}

		// Verfies rider Id 
		if (!riders.containsKey(riderId)) {
			throw new IDNotRecognisedException("rider Id not recognised: " + riderId);		
		}

		// Removes the rider from riders hashmap in impl
		riders.remove(riderId);

		// Removes the rider object
		rider.deleteRiderObject();
		
		// Updates riders team without the rider
		int teamId = rider.getTeamId();
		CyclingTeam team = teams.get(teamId);
		team.removeRider(riderId);

		// Removes riders results in impl
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
			CyclingResult result = riderResults.get(riderId);
			if(result.getStageId() == stageId) {
            	throw new DuplicatedResultException("Rider already has a result for this stage.");
			}
        }
		
		// Gets stage object from stage Id
		CyclingStage stage = stages.get(stageId);

		stage.setStageState(StageState.WAITING_FOR_RESULTS);

		// Checks if there are the right amount of checkpoint times
        if (checkpoints.length != stage.getNumberOfCheckpoints() + 2) {
            throw new InvalidCheckpointTimesException("Invalid number of checkpoint times.");
        }

		// Checks stage is valid state
		if (stage.getStageState() != StageState.WAITING_FOR_RESULTS) {
			throw new InvalidStageStateException("Stage results have already been finalised.");
		}

		// Gets result object from rider Id
		if (riderResults.containsKey(riderId)) {
			CyclingResult riderResult = riderResults.get(riderId);
			riderResult.updateResults(stageId, checkpoints);

			// Add results to results hashmap in stage class
			stage.addResults(riderId, riderResult);

		} else {
			CyclingResult riderResult = new CyclingResult(riderId, stageId, checkpoints);
			//Maps the rider id to his results
			riderResults.put(riderId, riderResult);

			// Add results to results hashmap in stage class
			stage.addResults(riderId, riderResult);
		}
		// Update the stage state to "results recorded"
		stage.setStageState(StageState.NOT_WAITING_FOR_RESULTS);
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

		CyclingStage stage = stages.get(stageId);

		ArrayList<Integer> riderIds = stage.getRiderIdsWithResults();
		
		// Gets the results object for the specific rider
		CyclingResult riderResult  = riderResults.get(riderId);

		if (riderResult == null) {
			// If rider has no result then return null
			return null; 
		}

		ArrayList<LocalTime> stageElapsedTimes = new ArrayList<>();
		for (int eachRiderId : riderIds) {
			CyclingResult eachRiderResult = riderResults.get(eachRiderId);
			if(eachRiderResult == null) {
				continue;
			} 
			LocalTime totalElapsedTime = eachRiderResult.calculateTotalElapsedTime(stageId);
			stageElapsedTimes.add(totalElapsedTime);			
		}
		
		// Calculates the total elapsed time of the rider in that stage
		LocalTime elapsedTime = riderResult.calculateTotalElapsedTime(stageId);

		Collections.sort(stageElapsedTimes);
		int index = 0;

		for (int i = 0; i < stageElapsedTimes.size(); i++) {
			if (stageElapsedTimes.get(i).equals(elapsedTime)) {
				index = i;
			}
		}

		if (index == 0) {
			return elapsedTime;
		} else {
			LocalTime otherElapsedTime = stageElapsedTimes.get(index - 1);
			Duration duration = Duration.between(otherElapsedTime, elapsedTime);

			if (duration.getSeconds() < 1) {
				return otherElapsedTime;
			} else {
				return elapsedTime;
			}
		}
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

		// Get the adjusted elapsed time for all riders in the stage
		Map<Integer, LocalTime> riderAdjustedTimes = new HashMap<>();
		for (Map.Entry<Integer, CyclingResult> entry : riderResults.entrySet()) {
			int riderId = entry.getKey();
			riderAdjustedTimes.put(riderId, getRiderAdjustedElapsedTimeInStage(stageId, riderId));
		}

		// Sort results from fastest to slowest based on their adjusted elapsed time
		ArrayList<Map.Entry<Integer, LocalTime>> sortedRiders = new ArrayList<>(riderAdjustedTimes.entrySet());
		sortedRiders.sort(Map.Entry.comparingByValue());
		
		// Determine rank of each rider
		int[] riderRanks = new int[sortedRiders.size()];

		// List of all riderIds
		ArrayList<Integer> riderIdList = new ArrayList<>();

		for (Map.Entry<Integer, LocalTime> entry : sortedRiders) {
			riderIdList.add(entry.getKey());
		}

		// Adds sorted riders into int[] by their riderId
		for(int i = 0; i < sortedRiders.size(); i++) {
			riderRanks[i] = riderIdList.get(i);
		}

		return riderRanks; 
	}

	@Override
	public LocalTime[] getRankedAdjustedElapsedTimesInStage(int stageId) throws IDNotRecognisedException {

		// Check stageId exists
		if (!stages.containsKey(stageId)) {
			throw new IDNotRecognisedException("STage Id not recognised: " + stageId);
		}

		CyclingStage stage = stages.get(stageId);

		Map<LocalTime, Integer> ridersElapsedTimeMap = new HashMap<>();

		// Gets an array of all riderIds that have a result for this stage
		ArrayList<Integer> riderIds = stage.getRiderIdsWithResults();

		for(int riderId : riderIds) {
			LocalTime adjustedElapsedTime = getRiderAdjustedElapsedTimeInStage(stageId, riderId);
			ridersElapsedTimeMap.put(adjustedElapsedTime, riderId);
		}

		stageElapsedTimes.put(stageId, ridersElapsedTimeMap);

		LocalTime[] rankedAdjustedTimesArray = ridersElapsedTimeMap.keySet().toArray(new LocalTime[0]);

		Arrays.sort(rankedAdjustedTimesArray);

		return rankedAdjustedTimesArray;
	}

	@Override
	public int[] getRidersPointsInStage(int stageId) throws IDNotRecognisedException {
		
		// Check if stage Id exists
		if (!stages.containsKey(stageId)) {
			throw new IDNotRecognisedException("Stage Id not recognised: " + stageId);
		}

		// Get stage object 
		CyclingStage stage = stages.get(stageId);

		// Get array of rider Ids that have results for this stage
		ArrayList<Integer> riderIdArray = stage.getRiderIdsWithResults();

		int[] riderIds = new int[riderIdArray.size()];

		for(int i = 0; i < riderIdArray.size(); i++) {
			riderIds[i] = riderIdArray.get(i);
		}
		
		ArrayList<Integer> sprintCheckpointArray = stage.getSprintCheckpoints();

		ArrayList<Map<Integer, Duration>> sprintCheckpointsWithTimes = stage.calculateSprintCheckpointTimes(sprintCheckpointArray);

		// Create an int array to store sprint points
		int[] sprintPointsArray = new int[riderIds.length];

		// Stores finishing points
		int[] finishingPointsArray = new int[riderIds.length];

		// Store total points for rider
		int[] totalPointsArray = new int[riderIds.length];

		// Calculate points for each rider based on their result, AND GET RESULTS FOR SPRINT CHECKPOINTS
		for (Map<Integer, Duration> sprintCheckpointTimes : sprintCheckpointsWithTimes) {
			
			//Creates ArrayList of checkpoint times for each rider for specific sprint checkpoint
			ArrayList<Duration> checkpointTimes = new ArrayList<>(sprintCheckpointTimes.values());
			
			// Sorts the riders times for this checkpoint
			Collections.sort(checkpointTimes);

			// Maps riderId to their rank/position
			Map<Integer,Integer> checkpointRank = new HashMap<Integer, Integer>();
			
			// Iterates through checkpointTimes and finds the rank for each rider
			for (int i = 0; i < checkpointTimes.size(); i++) {
				for(Map.Entry<Integer, Duration> entry : sprintCheckpointTimes.entrySet()) {
					if (entry.getValue().equals(checkpointTimes.get(i))) {
						checkpointRank.remove(entry.getKey());
						checkpointRank.put(entry.getKey(), i + 1);
					}
				}
			}

			// Finds the points for the riders sprint and finishing position
			for (int x = 0; x < riderIds.length; x++) {
				int riderId = riderIds[x];
				int rank = checkpointRank.get(riderId);
				int sprintPoints = stage.getSprintPoints(rank);
				sprintPointsArray[x] = sprintPoints;
				int finishingPoints = stage.getRiderPoints(rank);
				finishingPointsArray[x] = finishingPoints;
				// Finds total points for each rider in the point classification
				totalPointsArray[x] += finishingPointsArray[x] + sprintPointsArray[x]; 
			}
		}
		return totalPointsArray;
	}

	@Override
	public int[] getRidersMountainPointsInStage(int stageId) throws IDNotRecognisedException {
		
		// Check if stage Id exists
		if (!stages.containsKey(stageId)) {
			throw new IDNotRecognisedException("Stage Id not recognised: " + stageId);
		}

		// Get stage object 
		CyclingStage stage = stages.get(stageId);

		ArrayList<Integer> riderIdArray = stage.getRiderIdsWithResults();

		int[] riderIds = new int[riderIdArray.size()];

		for(int i = 0; i < riderIdArray.size(); i++) {
			riderIds[i] = riderIdArray.get(i);
		}

		ArrayList<Checkpoint> mountainCheckpointObjects = stage.getMountainCheckpointObjects();
		
		ArrayList<Integer> mountainCheckpointArray = stage.getMountainCheckpoints();

		ArrayList<Map<Integer, Duration>> mountainCheckpointsWithTimes = stage.calculateMountainCheckpointTimes(mountainCheckpointArray);

		// Create an int array to store sprint points
		int[] mountainPointsArray = new int[riderIds.length];

		int y = 0;

		// Calculate points for each rider based on their result
		for (Map<Integer, Duration> mountainCheckpointTimes : mountainCheckpointsWithTimes) {
			
			//Creates ArrayList of checkpoint times for each rider for specific mountain checkpoint
			ArrayList<Duration> checkpointTimes = new ArrayList<>(mountainCheckpointTimes.values());
			
			// Sorts the riders times for this checkpoint
			Collections.sort(checkpointTimes);

			// Maps riderId to their rank/position
			Map<Integer,Integer> checkpointRank = new HashMap<Integer, Integer>();

			for (int riderId : riderIds) {
				checkpointRank.put(riderId, 0);
			}
			
			// Iterates through checkpointTimes and finds the rank for each rider
			for (int i = 0; i < checkpointTimes.size(); i++) {
				for(Map.Entry<Integer, Duration> entry : mountainCheckpointTimes.entrySet()) {
					if (entry.getValue().equals(checkpointTimes.get(i))) {
						checkpointRank.put(entry.getKey(), i + 1);
					}
				}
			}

			// Finds the points for the riders mountain checkpoints
			for (int x = 0; x < riderIds.length; x++) {
				int riderId = riderIds[x];
				int rank = checkpointRank.get(riderId);
				CheckpointType type = mountainCheckpointObjects.get(y).getType();
				int points = stage.getMountainPointsForRider(rank, type);
				mountainPointsArray[x] += points;
			}
			y++;
		}
		return mountainPointsArray;
	}

	@Override
	public void eraseCyclingPortal() {
		races.clear();
		stages.clear();
		teams.clear();
		riders.clear();
		riderResults.clear();
		stageElapsedTimes.clear();

		riderCounter = 0;
		raceCounter = 0;
		teamCounter = 0;
		stageCounter = 0;

	}

	@Override
	public void saveCyclingPortal(String filename) throws IOException {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
			// Saves races, stages, teams, riders and results
			oos.writeObject(this);
		} catch (IOException e) {
			throw e;
		}
	}

	@Override
	public void loadCyclingPortal(String filename) throws IOException, ClassNotFoundException {
		Object obj;
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
			// Loads all objects
			obj = ois.readObject();
		} catch (IOException | ClassNotFoundException e){
			throw e;
		}
		if(!(obj instanceof CyclingPortalImpl)) {
			throw new ClassNotFoundException("The file did not contain CyclingPortalImpl object.");
		}

		CyclingPortalImpl loadedPortal = (CyclingPortalImpl) obj;
		this.teams = loadedPortal.teams;
		this.stages = loadedPortal.stages;
		this.teams = loadedPortal.teams;
		this.riders = loadedPortal.riders;
		this.riderResults = loadedPortal.riderResults;
		this.stageElapsedTimes = loadedPortal.stageElapsedTimes;
		this.raceCounter = loadedPortal.raceCounter;
		this.stageCounter = loadedPortal.stageCounter;
		this.teamCounter = loadedPortal.teamCounter;
		this.riderCounter = loadedPortal.riderCounter;
	}

	@Override
	public void removeRaceByName(String name) throws NameNotRecognisedException {
		
		CyclingRace race;

		for (CyclingRace cyclingRace : races.values()) {
			if (cyclingRace.getName() == name) {
				race = cyclingRace;
				races.remove(race.getRaceId());
				for (CyclingStage stage : race.getStages()) {
					int stageId = stage.getStageId();
					stageElapsedTimes.remove(stageId);
					race.deleteStage(stageId);
					for(int riderId : riderResults.keySet()) {
						CyclingResult result = riderResults.get(riderId);
						if(result.getStageId() == stageId) {
							riderResults.remove(riderId);
						}
					}
				}		
			} else {
				throw new NameNotRecognisedException("Race name is not recognisedL: " + name);
			}
		}

		// remove stages checkpoints and results
	}

	@Override
	public LocalTime[] getGeneralClassificationTimesInRace(int raceId) throws IDNotRecognisedException {
		if (!races.containsKey(raceId)) {
			throw new IDNotRecognisedException("Race ID not recognised: " + raceId);
		}

		CyclingRace race = races.get(raceId);

		List<CyclingStage> stageList = race.getStages();

		ArrayList<Integer> riderIds = stageList.get(0).getRiderIdsWithResults(); // should check that all stages have same number of riders

		Map<Integer, LocalTime> totalElapsedTimes = new HashMap<>();

		for (Integer riderId : riderIds) {
			LocalTime time = LocalTime.of(0, 0);
			totalElapsedTimes.put(riderId, time);
		}

		for (CyclingStage stage : stageList) {
			int stageId = stage.getStageId();
			Map<LocalTime, Integer> riderElapsedTimeInStage = stageElapsedTimes.get(stageId);
			for (Map.Entry<LocalTime, Integer> entry : riderElapsedTimeInStage.entrySet()) {
				int riderId = entry.getValue();
				LocalTime elapsedTime = totalElapsedTimes.get(riderId);
				LocalTime newTime = elapsedTime.plusHours(entry.getKey().getHour())
												.plusMinutes(entry.getKey().getMinute())
												.plusSeconds(entry.getKey().getSecond());
				totalElapsedTimes.put(riderId, newTime);
			}
		}

		ArrayList<LocalTime> totalElapsedTimeList = new ArrayList<>(totalElapsedTimes.values());

		Collections.sort(totalElapsedTimeList);

		LocalTime[] generalClassificationTimes = totalElapsedTimeList.toArray(new LocalTime[0]);

		return generalClassificationTimes;
	}

	@Override
	public int[] getRidersPointsInRace(int raceId) throws IDNotRecognisedException {
		
		if (!races.containsKey(raceId)) {
			throw new IDNotRecognisedException("RaceId not recognised: " + raceId);
		}

		CyclingRace race = races.get(raceId);

		ArrayList<Map<Integer, Integer>> finishingTimesForEachStage = new ArrayList<>();

		List<CyclingStage> stageList = race.getStages();

		ArrayList<Integer> riderIds = stageList.get(0).getRiderIdsWithResults();

		Map<Integer, Integer> ridersPointsInRace = new HashMap<>();

		for(int riderId : riderIds) {
			ridersPointsInRace.put(riderId, 0);
		}

		for(CyclingStage stage : stageList) {
			int stageId = stage.getStageId();
			int[] ridersPointsInStage = getRidersPointsInStage(stageId);
			int[] ridersRankInStage = getRidersRankInStage(stageId);
			Map<Integer, Integer> rankAndPointsInStage = new HashMap<>();
			for(int i = 0; i < ridersPointsInStage.length; i++) {
				rankAndPointsInStage.put(ridersRankInStage[i], ridersPointsInStage[i]);
			}
			finishingTimesForEachStage.add(rankAndPointsInStage);
		}

		for(Map<Integer, Integer> stageMap : finishingTimesForEachStage) {
			for(int riderId : stageMap.keySet()) {
				int points = ridersPointsInRace.get(riderId);
				int newPoints = stageMap.get(riderId);
				ridersPointsInRace.remove(riderId);
				ridersPointsInRace.put(riderId, points + newPoints);
			}
		}

		Map<Integer, LocalTime> totalElapsedTimes = new HashMap<>();

		for (Integer riderId : riderIds) {
			LocalTime time = LocalTime.of(0, 0);
			totalElapsedTimes.put(riderId, time);
		}

		for (CyclingStage stage : stageList) {
			int stageId = stage.getStageId();
			Map<LocalTime, Integer> riderElapsedTimeInStage = stageElapsedTimes.get(stageId);
			for (Map.Entry<LocalTime, Integer> entry : riderElapsedTimeInStage.entrySet()) {
				int riderId = entry.getValue();
				LocalTime elapsedTime = totalElapsedTimes.get(riderId);
				LocalTime newTime = elapsedTime.plusHours(entry.getKey().getHour())
												.plusMinutes(entry.getKey().getMinute())
												.plusSeconds(entry.getKey().getSecond());
				totalElapsedTimes.remove(riderId);
				totalElapsedTimes.put(riderId, newTime);
			}
		}

		ArrayList<LocalTime> ridersTotalTime = new ArrayList<>(totalElapsedTimes.values());

		Collections.sort(ridersTotalTime);

		ArrayList<Integer> sortedIds = new ArrayList<>();

		for(LocalTime time : ridersTotalTime) {
			for(Map.Entry<Integer, LocalTime> entry : totalElapsedTimes.entrySet()) {
				if(entry.getValue().equals(time)) {
					sortedIds.add(entry.getKey());
				}
			}
		}

		int[] totalPointsInRace = new int[sortedIds.size()];

		for(int i = 0; i < sortedIds.size(); i++) {
			int riderId = sortedIds.get(i);
			totalPointsInRace[i] = ridersPointsInRace.get(riderId);
		}

		return totalPointsInRace;
	}

	@Override
	public int[] getRidersMountainPointsInRace(int raceId) throws IDNotRecognisedException {
		
		if (!races.containsKey(raceId)) {
			throw new IDNotRecognisedException("RaceId not recognised: " + raceId);
		}

		CyclingRace race = races.get(raceId);

		ArrayList<Map<Integer, Integer>> mountainPointsForEachStage = new ArrayList<>();

		List<CyclingStage> stageList = race.getStages();

		ArrayList<Integer> riderIds = stageList.get(0).getRiderIdsWithResults();

		Map<Integer, Integer> ridersMountainPointsInRace = new HashMap<>();

		for(int riderId : riderIds) {
			ridersMountainPointsInRace.put(riderId, 0);
		}

		for(CyclingStage stage : stageList) {
			int stageId = stage.getStageId();
			int[] ridersMountainPointsInStage = getRidersMountainPointsInStage(stageId);
			int[] ridersRankInStage = getRidersRankInStage(stageId);
			Map<Integer, Integer> rankAndMountainPointsInStage = new HashMap<>();
			for(int i = 0; i < ridersMountainPointsInStage.length; i++) {
				rankAndMountainPointsInStage.put(ridersRankInStage[i], ridersMountainPointsInStage[i]);
			}
			mountainPointsForEachStage.add(rankAndMountainPointsInStage);
		}

		for(Map<Integer, Integer> stageMap : mountainPointsForEachStage) {
			for(int riderId : stageMap.keySet()) {
				int points = ridersMountainPointsInRace.get(riderId);
				int newPoints = stageMap.get(riderId);
				ridersMountainPointsInRace.remove(riderId);
				ridersMountainPointsInRace.put(riderId, points + newPoints);
			}
		}

		Map<Integer, LocalTime> totalElapsedTimes = new HashMap<>();

		for (Integer riderId : riderIds) {
			LocalTime time = LocalTime.of(0, 0);
			totalElapsedTimes.put(riderId, time);
		}

		for (CyclingStage stage : stageList) {
			int stageId = stage.getStageId();
			Map<LocalTime, Integer> riderElapsedTimeInStage = stageElapsedTimes.get(stageId);
			for (Map.Entry<LocalTime, Integer> entry : riderElapsedTimeInStage.entrySet()) {
				int riderId = entry.getValue();
				LocalTime elapsedTime = totalElapsedTimes.get(riderId);
				LocalTime newTime = elapsedTime.plusHours(entry.getKey().getHour())
												.plusMinutes(entry.getKey().getMinute())
												.plusSeconds(entry.getKey().getSecond());
				totalElapsedTimes.remove(riderId);
				totalElapsedTimes.put(riderId, newTime);
			}
		}

		ArrayList<LocalTime> ridersTotalTime = new ArrayList<>(totalElapsedTimes.values());

		Collections.sort(ridersTotalTime);

		ArrayList<Integer> sortedIds = new ArrayList<>();

		for(LocalTime time : ridersTotalTime) {
			for(Map.Entry<Integer, LocalTime> entry : totalElapsedTimes.entrySet()) {
				if(entry.getValue().equals(time)) {
					sortedIds.add(entry.getKey());
				}
			}
		}

		int[] totalMountainPointsInRace = new int[sortedIds.size()];

		for(int i = 0; i < sortedIds.size(); i++) {
			int riderId = sortedIds.get(i);
			totalMountainPointsInRace[i] = ridersMountainPointsInRace.get(riderId);
		}

		return totalMountainPointsInRace;
	}

	@Override
	public int[] getRidersGeneralClassificationRank(int raceId) throws IDNotRecognisedException {
		
		if (!races.containsKey(raceId)) {
			throw new IDNotRecognisedException("Race ID not recognised: " + raceId);
		}

		CyclingRace race = races.get(raceId);

		List<CyclingStage> stageList = race.getStages();

		ArrayList<Integer> riderIds = stageList.get(0).getRiderIdsWithResults(); // should check that all stages have same number of riders

		Map<Integer, LocalTime> totalElapsedTimes = new HashMap<>();

		for (Integer riderId : riderIds) {
			LocalTime time = LocalTime.of(0, 0);
			totalElapsedTimes.put(riderId, time);
		}

		for (CyclingStage stage : stageList) {
			int stageId = stage.getStageId();
			Map<LocalTime, Integer> riderElapsedTimeInStage = stageElapsedTimes.get(stageId);
			for (Map.Entry<LocalTime, Integer> entry : riderElapsedTimeInStage.entrySet()) {
				int riderId = entry.getValue();
				LocalTime elapsedTime = totalElapsedTimes.get(riderId);
				LocalTime newTime = elapsedTime.plusSeconds(entry.getKey().toSecondOfDay());
				totalElapsedTimes.remove(riderId);
				totalElapsedTimes.put(riderId, newTime);
			}
		}

		ArrayList<LocalTime> totalElapsedTimeList = new ArrayList<>(totalElapsedTimes.values());

		ArrayList<Integer> riderSortedRank = new ArrayList<>();

		Collections.sort(totalElapsedTimeList);

		for(LocalTime elapsedTime : totalElapsedTimeList){
			for(Map.Entry<Integer, LocalTime> entry : totalElapsedTimes.entrySet()) {
				if(entry.getValue().equals(elapsedTime)) {
					riderSortedRank.add(entry.getKey());
				}
			}
		}

		int[] sortedRiderIds = new int[riderSortedRank.size()];
		for (int i = 0; i < riderSortedRank.size(); i++) {
			sortedRiderIds[i] = riderSortedRank.get(i);
		}

		return sortedRiderIds;
	}

	@Override
	public int[] getRidersPointClassificationRank(int raceId) throws IDNotRecognisedException {
		
		if (!races.containsKey(raceId)) {
			throw new IDNotRecognisedException("RaceId not recognised: " + raceId);
		}

		CyclingRace race = races.get(raceId);

		ArrayList<Map<Integer, Integer>> finishingTimesForEachStage = new ArrayList<>();

		List<CyclingStage> stageList = race.getStages();

		ArrayList<Integer> riderIds = stageList.get(0).getRiderIdsWithResults();

		Map<Integer, Integer> ridersPointsInRace = new HashMap<>();

		for(int riderId : riderIds) {
			ridersPointsInRace.put(riderId, 0);
		}

		for(CyclingStage stage : stageList) {
			int stageId = stage.getStageId();
			int[] ridersPointsInStage = getRidersPointsInStage(stageId);
			int[] ridersRankInStage = getRidersRankInStage(stageId);
			Map<Integer, Integer> rankAndPointsInStage = new HashMap<>();
			for(int i = 0; i < ridersPointsInStage.length; i++) {
				rankAndPointsInStage.put(ridersRankInStage[i], ridersPointsInStage[i]);
			}
			finishingTimesForEachStage.add(rankAndPointsInStage);
		}

		for(Map<Integer, Integer> stageMap : finishingTimesForEachStage) {
			for(int riderId : stageMap.keySet()) {
				int points = ridersPointsInRace.get(riderId);
				int newPoints = stageMap.get(riderId);
				ridersPointsInRace.remove(riderId);
				ridersPointsInRace.put(riderId, points + newPoints);
			}
		}

		ArrayList<Integer> totalPoints = new ArrayList<>(ridersPointsInRace.values());

		Collections.sort(totalPoints, (a, b) -> b.compareTo(a));

		ArrayList<Integer> riderSortedPointsRank = new ArrayList<>();

		for(int riderPoints : totalPoints){
			for(Map.Entry<Integer, Integer> entry : ridersPointsInRace.entrySet()) {
				if(entry.getValue().equals(riderPoints)) {
					riderSortedPointsRank.add(entry.getKey());
				}
			}
		}

		int[] sortedRiderIds = new int[riderSortedPointsRank.size()];
		for(int i = 0; i < riderSortedPointsRank.size(); i++) {
			sortedRiderIds[i] = riderSortedPointsRank.get(i);
		}

		return sortedRiderIds;
	}

	@Override
	public int[] getRidersMountainPointClassificationRank(int raceId) throws IDNotRecognisedException {
		
		if (!races.containsKey(raceId)) {
			throw new IDNotRecognisedException("RaceId not recognised: " + raceId);
		}

		CyclingRace race = races.get(raceId);

		ArrayList<Map<Integer, Integer>> mountainPointsForEachStage = new ArrayList<>();

		List<CyclingStage> stageList = race.getStages();

		ArrayList<Integer> riderIds = stageList.get(0).getRiderIdsWithResults();

		Map<Integer, Integer> ridersMountainPointsInRace = new HashMap<>();

		for(int riderId : riderIds) {
			ridersMountainPointsInRace.put(riderId, 0);
		}

		for(CyclingStage stage : stageList) {
			int stageId = stage.getStageId();
			int[] ridersMountainPointsInStage = getRidersMountainPointsInStage(stageId);
			int[] ridersRankInStage = getRidersRankInStage(stageId);
			Map<Integer, Integer> rankAndMountainPointsInStage = new HashMap<>();
			for(int i = 0; i < ridersMountainPointsInStage.length; i++) {
				rankAndMountainPointsInStage.put(ridersRankInStage[i], ridersMountainPointsInStage[i]);
			}
			mountainPointsForEachStage.add(rankAndMountainPointsInStage);
		}

		for(Map<Integer, Integer> stageMap : mountainPointsForEachStage) {
			for(int riderId : stageMap.keySet()) {
				int points = ridersMountainPointsInRace.get(riderId);
				int newPoints = stageMap.get(riderId);
				ridersMountainPointsInRace.remove(riderId);
				ridersMountainPointsInRace.put(riderId, points + newPoints);
			}
		}

		ArrayList<Integer> totalMountainPoints = new ArrayList<>(ridersMountainPointsInRace.values());

		Collections.sort(totalMountainPoints, (a, b) -> b.compareTo(a));

		ArrayList<Integer> riderSortedMountainPointsRank = new ArrayList<>();

		for(int riderMountainPoints : totalMountainPoints){
			for(Map.Entry<Integer, Integer> entry : ridersMountainPointsInRace.entrySet()) {
				if(entry.getValue().equals(riderMountainPoints)) {
					riderSortedMountainPointsRank.add(entry.getKey());
				}
			}
		}

		int[] sortedRiderIds = new int[riderSortedMountainPointsRank.size()];
		for(int i = 0; i < riderSortedMountainPointsRank.size(); i++) {
			sortedRiderIds[i] = riderSortedMountainPointsRank.get(i);
		}

		return sortedRiderIds;
	}

}
