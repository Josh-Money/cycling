package cycling;

import java.io.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class CyclingPortalImpl implements CyclingPortal {

	private Map<Integer, CyclingRace> races;
	private Map<Integer, CyclingStage> stages;
	private Map<Integer, CyclingTeam> teams;
	private Map<Integer, CyclingRider> riders;

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

	@Override
	public int createRace(String name, String description) throws IllegalNameException, InvalidNameException {
		
		// Constructor to create new race
		CyclingRace newRace = new CyclingRace(name, description);

		// Generates unique race id 
		int raceId = getNextRaceID();
		
		// Adds new race to the list 
		races.put(raceId, newRace);
		
		// Return the raceId
		return raceId;
	}

	private int getNextRaceID() {
		
		// Increments the size of the list to create next raceID
		return races.size() + 1;
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
		//needs work, needs to remove all contents of the race not just race Id

		// Gets raceId from list of races
		CyclingRace race = races.get(raceId);

		// Check if raceID is recognised
		if (race == null) {
			throw new IDNotRecognisedException("Race ID not recognised: " + raceId);
		}		
		// Removes the race
		races.remove(race);      // think this might be race.getraceId() 
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

	@Override
	public int addStageToRace(int raceId, String stageName, String description, double length, LocalDateTime startTime,
			StageType type)
			throws IDNotRecognisedException, IllegalNameException, InvalidNameException, InvalidLengthException {
		
		// Gets raceId from list of races 
		CyclingRace race = races.get(raceId);

		// Check if raceId is not recognised
		if (race == null) {
			throw new IDNotRecognisedException("Race ID not recognised: " + raceId);
		}
		// Check if race name is already in use in the system
		if (races.contains(race.getName())) {
			throw new IllegalNameException("Name already in use");
		}
		// Check if stage name is valid
		if (stageName == null || stageName.trim().isEmpty() || stageName.length() > 30 || stageName.contains(" ")){
			throw new InvalidNameException("Invalid stage name: " + stageName);
		}
		// Check if stage length is valid
		if (length < 5) {
			throw new InvalidLengthException("Invalid stage length: " + length);
		}

		// Creates new stage using constructor
		CyclingStage newStage = new CyclingStage(stageName, description, length, startTime, type);

		// Generates stage id
		int stageId = getNextStageId();

		// Adds stage to race
		race.addStage(stageId, newStage); //two parameters used when there is only one parameter in the method cycling.CyclingRace.AddStage()
	}

	public int getNextStageId() {
		
		// Increments stageId to create a unique ID
		return stages.size() + 1;
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
		
		// Gets stageId from list of races
		CyclingRace stage = stages.get(stageId);

		// Check if stageID is recognised
		if (stage == null) {
			throw new IDNotRecognisedException("Stage ID not recognised: " + stageId);
		}
		
		// Removes the stage
		stages.remove(stage);

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
			throw new InvalidStageStateException("");
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
		
		// Verify checkpoint Id
		if (!isValidCheckpointId(checkpointId)) {
			throw new IDNotRecognisedException("Invalid checkpoint ID: " + checkpointId);
		}

		// Verifies stage is in right state
		if (stage.getStageState() != StageState.WAITING_FOR_RESULTS) {
			throw new InvalidStageStateException("The stage is not in 'waiting for results' state.");
		}

		// Removes the checkpoint
		removeCheckpointFromList(checkpointId);
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

		// Verfies stage is in right state
		if (stage.getStageState() != StageState.WAITING_FOR_RESULTS) {
			throw new InvalidStageStateException("The stage is not in 'Waiting for results' state.");
		}

		// Retrieves the list of checkpoints which are in order from first to last
		stage.getCheckpoints();
	}

	@Override
	public int createTeam(String name, String description) throws IllegalNameException, InvalidNameException {
		
		// Creates a new team 
		CyclingTeam newTeam = new CyclingTeam(name, description);

		// Verifies if team name is already in use
		if (teams.contains(name)) {
			throw new IllegalNameException("Name is already in use.");
		}

		// Verifies that name is valid
		if (name == null || name.trim().isEmpty() || name.length() > 30 || name.contains(" ")){
			throw new InvalidNameException("Invalid team name: " + name);
		}

		// Generates new team ID
		int teamId = getUniqueTeamId();

		// Adds to the teams hashmap
		teams.put(teamId, newTeam);
	}

	public int getUniqueTeamId() {
		return teams.size() + 1;
	}

	@Override
	public void removeTeam(int teamId) throws IDNotRecognisedException {
		
		// Creates a new team
		CyclingTeam team = teams.get(teamId);

		// Verifies TeamId 
		if (!teams.containsKey(teamId)) {
			throw new IDNotRecognisedException("TeamId not recognised: " + teamId);
		}

		// Removes the team
		teams.remove(team);

	}

	@Override
	public int[] getTeams() {
		
		// Creates a new list to display all teamIDs
		List<Integer> teamIds = new ArrayList<>();

		// Loops through the teams hashmap adding the teamId to the list
		for (CyclingTeam team : teams.values()) {
			teamIds.add(team.getTeamId());
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
		return team.getRidersInTeam();
	}

	@Override
	public int createRider(int teamID, String name, int yearOfBirth)
			throws IDNotRecognisedException, IllegalArgumentException {
		
		// Creates new rider
		CyclingRider newRider = new CyclingRider(yearOfBirth, teamID, name, yearOfBirth);

		// Verfies teamId
		if(!teams.containsKey(teamID)) {
			throw new IDNotRecognisedException("TeamId not recognised: " + teamID);
		}

		// Validate rider name and year of birth
		if (name == null || name.trim().isEmpty() || yearOfBirth < 1900) {
			throw new IllegalArgumentException("Invalid rider name or year of birth");
		}

		// Generates unique rider Id
		int riderId = getNextRiderId();

		// Adds rider Id and details to riders hash map
		riders.put(riderId, newRider);

		return riderId;
	}

	public int getNextRiderId() {
		// Generates unique rider Id
		return riders.size() + 1;
	}

	@Override
	public void removeRider(int riderId) throws IDNotRecognisedException {
		
		// Finds rider using rider Id
		CyclingRider rider = riders.get(riderId);

		// Verfies rider Id 
		if (!riders.containsKey(riderId)) {
			throw new IDNotRecognisedException("rider Id not recognised: " + riderId);		
		}

		// Removes the rider
		riders.remove(riderId);
	}

	@Override
	public void registerRiderResultsInStage(int stageId, int riderId, LocalTime... checkpoints)
			throws IDNotRecognisedException, DuplicatedResultException, InvalidCheckpointTimesException,
			InvalidStageStateException {
		
		// Checks if stage Id exists 
		if (!stages.containsKey(stageId)) {
			throw new IDNotRecognisedException("Stage ID not recognised: " + stageId);
		}

		// Checks if rider id exists
		if (!riders.containsKey(riderId)) {
			throw new IDNotRecognisedException("RiderId not recognised: " + riderId);
		}

		// Get stage and rider 
		CyclingStage stage = stages.get(stageId);
		CyclingRider rider = riders.get(riderId);

		// Check if stage state is in the "waiting for results" state
		if (stage.getStageState() != StageState.WAITING_FOR_RESULTS) {
			throw new InvalidStageStateException("Stage is not in 'waiting for results' state.");
		}

		// Check if the rider already has results for the stage 
		if (stage.hasRiderResult(riderId)) {
			throw new DuplicatedResultException("Rider already ahs a result.");
		}

		// Check if number of checkpoint time is valid
		int expectedCheckpointCount = stage.getNumberOfCheckpoints() + 2; // for start and finish
		if (checkpoints.length != expectedCheckpointCount) {
			throw new InvalidCheckpointTimesException("Invalid number of checkpoint times.");
		}
		
		// Create a new CyclingResults object
		CyclingResult results = new CyclingResult(riderId, stageId, checkpoints);
		
		// Add results to stage and rider 
		// NEED TO MAKE addResults FUNCTION
		stage.addResults(results);
		rider.addResults(results);
		
		// Update the stage state to "results recorded"
		stage.setStageState(StageState.RESULTS_FINALISED);

	}

	@Override
	public LocalTime[] getRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LocalTime getRiderAdjustedElapsedTimeInStage(int stageId, int riderId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub

	}

	@Override
	public int[] getRidersRankInStage(int stageId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
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
