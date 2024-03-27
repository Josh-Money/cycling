package cycling;

import java.io.*;
import java.time.*;
import java.util.*;

public class CyclingPortalImpl implements CyclingPortal {

	// Maps raceID to race object
	private Map<Integer, CyclingRace> races = new HashMap<>();
	// Maps stageID to stage object
	private Map<Integer, CyclingStage> stages = new HashMap<>();
	// Maps teamID to team object
	private Map<Integer, CyclingTeam> teams = new HashMap<>();
	// Maps riderID to rider object
	private Map<Integer, CyclingRider> riders = new HashMap<>();
	// Maps riderID to results object
    private Map<Integer, CyclingResult> riderResults = new HashMap<>();
	// A Map which maps stageID to a map of adjusted elapsed times to riderID
	private Map<Integer, Map<LocalTime, Integer>> stageElapsedTimes = new HashMap<>();
	
	// Counters to create unique IDs
	private int raceCounter = 0;
	private int stageCounter = 0;
	private int teamCounter = 0;
	private int riderCounter = 0;
	private int checkpointCounter = 0;

    @Override
	public int[] getRaceIds() {

		//Retrieves the keys of the races hashmap and puts it in a Set<Integer>
		Set<Integer> keySet = races.keySet();

		// Convert the set to Integer[]
		Integer[] keyArray = keySet.toArray(new Integer[0]);

		// Convert Integer[] to int[]
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

		// Iterates through the stages in the specified race and removes all relevant information
		for (CyclingStage stage : race.getStages()) {
			int stageId = stage.getStageId();
			stageElapsedTimes.remove(stageId);
			race.deleteStageObject(stageId);
			race.deleteStageName(stageId);
			stages.remove(stageId);
	
			// Create an iterator to safely remove elements
			Iterator<Map.Entry<Integer, CyclingResult>> iterator = riderResults.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<Integer, CyclingResult> entry = iterator.next();
				CyclingResult result = entry.getValue();
				if (result.getStageId() == stageId) {
					iterator.remove(); // Safe removal using iterator
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

		// create int[] with size of stage list
		int[] stageIDArrayList = new int[size];

		// cycles through the list of stage and adds stageid for each stage to the int[]
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

		// Deletes stage from ArrayList<CyclingStage> listOfstages and ArrayList<String> namesOfStages in the race class
		race.deleteStageObject(stageId);
		race.deleteStageName(stageId);
		
		// Removes the stage from stage hashmap
		stages.remove(stageId);
	}

	public int generateUniqueCheckpointId() {
        // Creates unique checkpoint ID
        return  checkpointCounter += 1;
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
		int checkpointId = generateUniqueCheckpointId();

		stage.addCheckpoint(checkpointId, newClimb);

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
		int checkpointId = generateUniqueCheckpointId();

		// Adds checkpoint object to the stage class hashmap
		stage.addCheckpoint(checkpointId, newSprintCheckpoint);

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
		
		//Removes the checkpoint from its hashmap in the stage class
		stage.removeCheckpointFromMap(checkpointId);
	}

	
	private CyclingStage findStageByCheckpointId(int checkpointId) { 
		
		// Intialise CyclingStage object
		CyclingStage theStage = null;

		// Iterates through all the stages to find the stage that contains the checkpointId
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
		// Return the stage
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

		// Adds name of team to ArrayList<String> nameOfTeams
		newTeam.addTeamName();

		// Adds to the hashmap in teams class
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

		// Creates new ArrayList to store all the total elapsed times for thi stage
		ArrayList<LocalTime> stageElapsedTimes = new ArrayList<>();

		// Iterates through the rider IDs and checks whether they have a result
		for (int eachRiderId : riderIds) {
			CyclingResult eachRiderResult = riderResults.get(eachRiderId);
			if(eachRiderResult == null) {
				continue;
			}

			// Calculate the riders total elapsed time
			LocalTime totalElapsedTime = eachRiderResult.calculateTotalElapsedTime(stageId);

			// Adds the riders total elapsed time to the ArrayList
			stageElapsedTimes.add(totalElapsedTime);			
		}
		
		// Calculates the total elapsed time of the rider in that stage
		LocalTime elapsedTime = riderResult.calculateTotalElapsedTime(stageId);

		// Sorts the total elapsed times in ascending order
		Collections.sort(stageElapsedTimes);

		// Finds whether the riders total elapsed needs adjusting
		int index = 0;

		for (int i = 0; i < stageElapsedTimes.size(); i++) {
			if (stageElapsedTimes.get(i).equals(elapsedTime)) {
				index = i;
			}
		}

		// If index stays at 0, then this menas the riders total elapsed time is the fastest and therefore should be returned unchanged
		if (index == 0) {
			return elapsedTime;
		} else {
			// Otherwise compares to time of rider infront
			LocalTime otherElapsedTime = stageElapsedTimes.get(index - 1);
			Duration duration = Duration.between(otherElapsedTime, elapsedTime);

			// If the time between the two riders is less than a second, the inital rider's total elapsed time changes to the same as the rider infront, else remains the same
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

		// Creates new hashmap to store adjusted elapsed time to the rider ID
		Map<LocalTime, Integer> ridersElapsedTimeMap = new HashMap<>();

		// Gets an array of all riderIds that have a result for this stage
		ArrayList<Integer> riderIds = stage.getRiderIdsWithResults();

		// Iterates through the riders and adds their adjusted elpased time and their ID to the above hashmap
		for(int riderId : riderIds) {
			LocalTime adjustedElapsedTime = getRiderAdjustedElapsedTimeInStage(stageId, riderId);
			ridersElapsedTimeMap.put(adjustedElapsedTime, riderId);
		}

		// Adds the stage Id mapping to the above hashmap to the stageElapsedTimes map whihc stores all the adjusted elapsed times for each rider for each stage
		stageElapsedTimes.put(stageId, ridersElapsedTimeMap);

		// Creates a LocalTime[] of the adjusted elapsed times
		LocalTime[] rankedAdjustedTimesArray = ridersElapsedTimeMap.keySet().toArray(new LocalTime[0]);

		// SOrt the array is ascending order (fastest first)
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

		// Create int[] to be returned
		int[] riderIds = new int[riderIdArray.size()];

		for(int i = 0; i < riderIdArray.size(); i++) {
			riderIds[i] = riderIdArray.get(i);
		}
		
		// ArrayList<Integer> of the checkpoint's location of type SPRINT
		ArrayList<Integer> sprintCheckpointArray = stage.getSprintCheckpoints();

		// ArrayList which each element stores the rider ID and their time for one sprint checkpoint
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

		// Gets an ArrayList of all riderIDs that have results
		ArrayList<Integer> riderIdArray = stage.getRiderIdsWithResults();

		// Create int[] to be returned
		int[] riderIds = new int[riderIdArray.size()];

		for(int i = 0; i < riderIdArray.size(); i++) {
			riderIds[i] = riderIdArray.get(i);
		}

		// ArrayList<Checkpoint> that contains all the mountain checkpoint objects
		ArrayList<Checkpoint> mountainCheckpointObjects = stage.getMountainCheckpointObjects();

		System.out.println(mountainCheckpointObjects);
		
		// ArrayList<Integer> that contains all the mountain checkpoint locations
		ArrayList<Integer> mountainCheckpointArray = stage.getMountainCheckpoints();

		System.out.println(mountainCheckpointArray);

		// ArrayList where each element is a map of the rider ID and their checkpoint time for each checkpoint
		ArrayList<Map<Integer, Duration>> mountainCheckpointsWithTimes = stage.calculateMountainCheckpointTimes(mountainCheckpointArray);

		// Create an int array to store sprint points
		int[] mountainPointsArray = new int[riderIds.length];

		// Used to access the right mountain checkpoint object
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
		// Resets all collections
		races.clear();
		stages.clear();
		teams.clear();
		riders.clear();
		riderResults.clear();
		stageElapsedTimes.clear();

		// Resets all ID counters to their intial values
		riderCounter = 0;
		raceCounter = 0;
		teamCounter = 0;
		stageCounter = 0;
		checkpointCounter = 0;
	}

	@Override
	public void saveCyclingPortal(String filename) throws IOException {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
			// Saves all collections and counter values
			oos.writeObject(this);
		} catch (IOException e) {
			throw e;
		}
	}

	@Override
	public void loadCyclingPortal(String filename) throws IOException, ClassNotFoundException {
		Object obj;
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
			// Loads all collections and counter values =
			obj = ois.readObject();
		} catch (IOException | ClassNotFoundException e){
			throw e;
		}
		if(!(obj instanceof CyclingPortalImpl)) {
			throw new ClassNotFoundException("The file did not contain CyclingPortalImpl object.");
		}

		// Safely downcase as we have checked that it is the correct object type
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
		this.checkpointCounter = loadedPortal.checkpointCounter;
	}

	@Override
	public void removeRaceByName(String name) throws NameNotRecognisedException {
		
		// Intialises CyclingRace object
		CyclingRace race;

		// Iterates through the races hashmap and compares each race's name with parameter
		for (CyclingRace cyclingRace : races.values()) {
			if (cyclingRace.getName() == name) {
				race = cyclingRace; // sets above race object to the right race
				races.remove(race.getRaceId()); // removes race from race hashmap

				// Iterates through all the stages in the race object
				for (CyclingStage stage : race.getStages()) {
					int stageId = stage.getStageId();
					stageElapsedTimes.remove(stageId); // Removes stage from the elapsed time map
					race.deleteStageObject(stageId); // Deletes the stage object in the race class
					race.deleteStageName(stageId); // Deletes the stage name from the nameOfStages ArrayList

					// Iterates through rider Id in the results map and checks whether their result is for the stage to be removed
					for(int riderId : riderResults.keySet()) {
						CyclingResult result = riderResults.get(riderId);
						if(result.getStageId() == stageId) {
							riderResults.remove(riderId); // Removes rider result from map
						}
					}
				}		
			} else {
				throw new NameNotRecognisedException("Race name is not recognisedL: " + name);
			}
		}
	}

	@Override
	public LocalTime[] getGeneralClassificationTimesInRace(int raceId) throws IDNotRecognisedException {
		if (!races.containsKey(raceId)) {
			throw new IDNotRecognisedException("Race ID not recognised: " + raceId);
		}

		CyclingRace race = races.get(raceId);

		// List of all stages in race
		List<CyclingStage> stageList = race.getStages();

		// ArrayList of riderIDs that have results for the stages
		ArrayList<Integer> riderIds = stageList.get(0).getRiderIdsWithResults(); 

		// Creates new hashmap of rider ID to their total elapsed time for the whole race
		Map<Integer, LocalTime> totalElapsedTimes = new HashMap<>();

		// Iterates through the rider IDs and sets their initial time to 0
		for (Integer riderId : riderIds) {
			LocalTime time = LocalTime.of(0, 0);
			totalElapsedTimes.put(riderId, time);
		}

		// Iterates through stage list
		for (CyclingStage stage : stageList) {
			int stageId = stage.getStageId();
			// Gets the elapsed time for each Rider Id for this stage
			Map<LocalTime, Integer> riderElapsedTimeInStage = stageElapsedTimes.get(stageId);

			for (Map.Entry<LocalTime, Integer> entry : riderElapsedTimeInStage.entrySet()) {
				int riderId = entry.getValue();
				// Gets riders old time
				LocalTime elapsedTime = totalElapsedTimes.get(riderId);
				// Finds riders new time by adding the elapsed time for each stage
				LocalTime newTime = elapsedTime.plusHours(entry.getKey().getHour())
												.plusMinutes(entry.getKey().getMinute())
												.plusSeconds(entry.getKey().getSecond());							
				// Updates the hashmap with increased total elapsed time
				totalElapsedTimes.put(riderId, newTime);
			}
		}

		// Coverst values from the hashmap into ArrayList<LocalTime>
		ArrayList<LocalTime> totalElapsedTimeList = new ArrayList<>(totalElapsedTimes.values());

		// Sorts ArrayList in ascending order
		Collections.sort(totalElapsedTimeList);

		// Converts ArrayList to LocalTime[]
		LocalTime[] generalClassificationTimes = totalElapsedTimeList.toArray(new LocalTime[0]);

		return generalClassificationTimes;
	}

	@Override
	public int[] getRidersPointsInRace(int raceId) throws IDNotRecognisedException {
		
		if (!races.containsKey(raceId)) {
			throw new IDNotRecognisedException("RaceId not recognised: " + raceId);
		}

		CyclingRace race = races.get(raceId);

		// Creates ArrayList where each element contains a Hashmap of all the riders Ids and riders points in each stage
		ArrayList<Map<Integer, Integer>> finishingTimesForEachStage = new ArrayList<>();

		// List of all stages in race 
		List<CyclingStage> stageList = race.getStages();

		// ArrayList of all rider Ids with results for the stages
		ArrayList<Integer> riderIds = stageList.get(0).getRiderIdsWithResults();

		// HashMap of riders ID to riders points
		Map<Integer, Integer> ridersPointsInRace = new HashMap<>();

		// Iterates and assigns each rider Id an initial value of 0 points
		for(int riderId : riderIds) {
			ridersPointsInRace.put(riderId, 0);
		}

		// Iterates through stage list
		for(CyclingStage stage : stageList) {
			int stageId = stage.getStageId();
			// Gets int[] of the riders points in stage
			int[] ridersPointsInStage = getRidersPointsInStage(stageId);
			// Gets int[] of the riders rank in stage
			int[] ridersRankInStage = getRidersRankInStage(stageId);
			// Hashmap which contains rank to points
			Map<Integer, Integer> rankAndPointsInStage = new HashMap<>();
			for(int i = 0; i < ridersPointsInStage.length; i++) {
				// Fills out the hashmap with the rider Ids and points 
				rankAndPointsInStage.put(ridersRankInStage[i], ridersPointsInStage[i]);
			}
			// Adds hashmap to ArrayList
			finishingTimesForEachStage.add(rankAndPointsInStage);
		}

		// Iterates through each map contains all the riders rank and points of each stage
		for(Map<Integer, Integer> stageMap : finishingTimesForEachStage) {
			// Iterates through the rider Ids in the hashmap 
			for(int riderId : stageMap.keySet()) {
				int points = ridersPointsInRace.get(riderId); // Gets points before
				int newPoints = stageMap.get(riderId); // Gets points after 
				ridersPointsInRace.put(riderId, points + newPoints); // Adds the new points to old points
			}
		}

		// Hashmap of Rider ID to Adjusted Elapsed time
		Map<Integer, LocalTime> totalElapsedTimes = new HashMap<>();

		// Sets intial time to 0
		for (Integer riderId : riderIds) {
			LocalTime time = LocalTime.of(0, 0);
			totalElapsedTimes.put(riderId, time);
		}

		// Iterates through stage list
		for (CyclingStage stage : stageList) {
			int stageId = stage.getStageId();
			Map<LocalTime, Integer> riderElapsedTimeInStage = stageElapsedTimes.get(stageId);

			for (Map.Entry<LocalTime, Integer> entry : riderElapsedTimeInStage.entrySet()) {
				int riderId = entry.getValue();
				// Gets riders old time
				LocalTime elapsedTime = totalElapsedTimes.get(riderId);
				LocalTime newTime = elapsedTime.plusHours(entry.getKey().getHour())
												.plusMinutes(entry.getKey().getMinute())
												.plusSeconds(entry.getKey().getSecond());
				// Updates hashmap with new time
				totalElapsedTimes.put(riderId, newTime);
			}
		}

		// Creates and sorts ArrayList of the LocalTime values into ascending order
		ArrayList<LocalTime> ridersTotalTime = new ArrayList<>(totalElapsedTimes.values());

		Collections.sort(ridersTotalTime);

		// Sorts the riders points by their elapsed time
		ArrayList<Integer> sortedIds = new ArrayList<>();

		for(LocalTime time : ridersTotalTime) {
			for(Map.Entry<Integer, LocalTime> entry : totalElapsedTimes.entrySet()) {
				if(entry.getValue().equals(time)) {
					sortedIds.add(entry.getKey());
				}
			}
		}

		// Creates int[] to return
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

		// contains rider id and their respective mountain points for each stage
		ArrayList<Map<Integer, Integer>> mountainPointsForEachStage = new ArrayList<>();

		// List of stages in the race
		List<CyclingStage> stageList = race.getStages();

		// rider Ids that have results for this race
		ArrayList<Integer> riderIds = stageList.get(0).getRiderIdsWithResults();

		// Maps rider id to their mountain points
		Map<Integer, Integer> ridersMountainPointsInRace = new HashMap<>();

		// Intialises mountain points to 0
		for(int riderId : riderIds) {
			ridersMountainPointsInRace.put(riderId, 0);
		}

		for(CyclingStage stage : stageList) {
			int stageId = stage.getStageId();
			// Gets mountain points sorted in an int[]
			int[] ridersMountainPointsInStage = getRidersMountainPointsInStage(stageId);
			// Gets rider Ids sorted by their elapsed time
			int[] ridersRankInStage = getRidersRankInStage(stageId);
			// Maps sorted rider id to their points
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
				// Updates mountain points for each stage
				ridersMountainPointsInRace.put(riderId, points + newPoints);
			}
		}

		// Maps rider Id to elapsed time 
		Map<Integer, LocalTime> totalElapsedTimes = new HashMap<>();

		// Initalises elapsed time to 0
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
				// Updates hashmap with new elapsed time
				totalElapsedTimes.put(riderId, newTime);
			}
		}

		// creates and sorts the elapsed times in ascending order ArrayList
		ArrayList<LocalTime> ridersTotalTime = new ArrayList<>(totalElapsedTimes.values());
		Collections.sort(ridersTotalTime);

		// Sorts riders mountain points by their elapsed time
		ArrayList<Integer> sortedIds = new ArrayList<>();

		for(LocalTime time : ridersTotalTime) {
			for(Map.Entry<Integer, LocalTime> entry : totalElapsedTimes.entrySet()) {
				if(entry.getValue().equals(time)) {
					sortedIds.add(entry.getKey());
				}
			}
		}

		// Creates int[] to return
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

		// List of all stages in race
		List<CyclingStage> stageList = race.getStages();

		// Rider Ids that ahve a result in the race
		ArrayList<Integer> riderIds = stageList.get(0).getRiderIdsWithResults(); 

		// Maps rider ID to total elapsed time
		Map<Integer, LocalTime> totalElapsedTimes = new HashMap<>();

		// Intialise total elapsed time to 0
		for (Integer riderId : riderIds) {
			LocalTime time = LocalTime.of(0, 0);
			totalElapsedTimes.put(riderId, time);
		}
		// Iterate through stages in race 
		for (CyclingStage stage : stageList) {
			int stageId = stage.getStageId();
			// Gets the elapsed times of all riders for stage
			Map<LocalTime, Integer> riderElapsedTimeInStage = stageElapsedTimes.get(stageId);
			for (Map.Entry<LocalTime, Integer> entry : riderElapsedTimeInStage.entrySet()) {
				int riderId = entry.getValue();
				LocalTime elapsedTime = totalElapsedTimes.get(riderId);
				LocalTime newTime = elapsedTime.plusSeconds(entry.getKey().toSecondOfDay());
				// Updates each rider Id with a new elapsed time
				totalElapsedTimes.put(riderId, newTime);
			}
		}

		// Creates and sorts the riders total elapsed times in an ascending order ArrayList 
		ArrayList<LocalTime> totalElapsedTimeList = new ArrayList<>(totalElapsedTimes.values());
		Collections.sort(totalElapsedTimeList);
		
		// New ArrayList to store rider IDs by ranked elapsed time
		ArrayList<Integer> riderSortedRank = new ArrayList<>();

		for(LocalTime elapsedTime : totalElapsedTimeList){
			for(Map.Entry<Integer, LocalTime> entry : totalElapsedTimes.entrySet()) {
				if(entry.getValue().equals(elapsedTime)) {
					riderSortedRank.add(entry.getKey());
				}
			}
		}


		// Create int[] to return
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

		// Creates ArrayList where each element contains a Hashmap of all the riders Ids and riders points in each stage
		ArrayList<Map<Integer, Integer>> finishingTimesForEachStage = new ArrayList<>();

		// List of all stages in race 
		List<CyclingStage> stageList = race.getStages();

		// ArrayList of all rider Ids with results for the stages
		ArrayList<Integer> riderIds = stageList.get(0).getRiderIdsWithResults();

		// HashMap of riders ID to riders points
		Map<Integer, Integer> ridersPointsInRace = new HashMap<>();

		// Iterates and assigns each rider Id an initial value of 0 points
		for(int riderId : riderIds) {
			ridersPointsInRace.put(riderId, 0);
		}

		// Iterates through stage list
		for(CyclingStage stage : stageList) {
			int stageId = stage.getStageId();
			// Gets int[] of the riders points in stage
			int[] ridersPointsInStage = getRidersPointsInStage(stageId);
			// Gets int[] of the riders rank in stage
			int[] ridersRankInStage = getRidersRankInStage(stageId);
			// Hashmap which contains rank to points
			Map<Integer, Integer> rankAndPointsInStage = new HashMap<>();
			for(int i = 0; i < ridersPointsInStage.length; i++) {
				// Fills out the hashmap with the rider Ids and points 
				rankAndPointsInStage.put(ridersRankInStage[i], ridersPointsInStage[i]);
			}
			// Adds hashmap to ArrayList
			finishingTimesForEachStage.add(rankAndPointsInStage);
		}

		// Iterates through each map contains all the riders rank and points of each stage
		for(Map<Integer, Integer> stageMap : finishingTimesForEachStage) {
			// Iterates through the rider Ids in the hashmap 
			for(int riderId : stageMap.keySet()) {
				int points = ridersPointsInRace.get(riderId); // Gets points before
				int newPoints = stageMap.get(riderId); // Gets points after 
				ridersPointsInRace.put(riderId, points + newPoints); // Adds the new points to old points
			}
		}

		// Creates and sorts the riders sprint points in an descending order ArrayList
		ArrayList<Integer> totalPoints = new ArrayList<>(ridersPointsInRace.values());
		Collections.sort(totalPoints, (a, b) -> b.compareTo(a));

		// ArrayList to store thr riderIds in order of their total points
		ArrayList<Integer> riderSortedPointsRank = new ArrayList<>();

		for(int riderPoints : totalPoints){
			for(Map.Entry<Integer, Integer> entry : ridersPointsInRace.entrySet()) {
				if(entry.getValue().equals(riderPoints)) {
					riderSortedPointsRank.add(entry.getKey());
				}
			}
		}
		
		// Creates int[] to return 
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

		// contains rider id and their respective mountain points for each stage
		ArrayList<Map<Integer, Integer>> mountainPointsForEachStage = new ArrayList<>();

		// List of stages in the race
		List<CyclingStage> stageList = race.getStages();

		// rider Ids that have results for this race
		ArrayList<Integer> riderIds = stageList.get(0).getRiderIdsWithResults();

		// Maps rider id to their mountain points
		Map<Integer, Integer> ridersMountainPointsInRace = new HashMap<>();

		// Intialises mountain points to 0
		for(int riderId : riderIds) {
			ridersMountainPointsInRace.put(riderId, 0);
		}

		for(CyclingStage stage : stageList) {
			int stageId = stage.getStageId();
			// Gets mountain points sorted in an int[]
			int[] ridersMountainPointsInStage = getRidersMountainPointsInStage(stageId);
			// Gets rider Ids sorted by their elapsed time
			int[] ridersRankInStage = getRidersRankInStage(stageId);
			// Maps sorted rider id to their points
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
				// Updates mountain points for each stage
				ridersMountainPointsInRace.put(riderId, points + newPoints);
			}
		}

		// Creates and sorts the total mountain points in a descending order ArrayList
		ArrayList<Integer> totalMountainPoints = new ArrayList<>(ridersMountainPointsInRace.values());
		Collections.sort(totalMountainPoints, (a, b) -> b.compareTo(a));

		// ArrayList to store ranked rider IDs based on their mountain points 
		ArrayList<Integer> riderSortedMountainPointsRank = new ArrayList<>();

		for(int riderMountainPoints : totalMountainPoints){
			for(Map.Entry<Integer, Integer> entry : ridersMountainPointsInRace.entrySet()) {
				if(entry.getValue().equals(riderMountainPoints)) {
					riderSortedMountainPointsRank.add(entry.getKey());
				}
			}
		}

		// Creates int[] to return
		int[] sortedRiderIds = new int[riderSortedMountainPointsRank.size()];
		for(int i = 0; i < riderSortedMountainPointsRank.size(); i++) {
			sortedRiderIds[i] = riderSortedMountainPointsRank.get(i);
		}
		return sortedRiderIds;
	}
}
