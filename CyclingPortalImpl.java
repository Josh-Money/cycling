package cycling;

import java.io.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class CyclingPortalImpl implements CyclingPortal {

	private List<CyclingRace> races;
	private List<CyclingStage> stages;
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
		CyclingRace newRace = new CyclingRace(getNextRaceID(), name, description);
		
		// Adds new race to the list 
		races.add(newRace);
		
		// Gives the new race a unique raceId
		return newRace.getRaceId();
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
		CyclingStage newStage = new CyclingStage(getNextStageId(), stageName, description, length, startTime, type);
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
		stages.remove(stageId);

	}

	@Override
	public int addCategorizedClimbToStage(int stageId, Double location, CheckpointType type, Double averageGradient,
			Double length) throws IDNotRecognisedException, InvalidLocationException, InvalidStageStateException,
			InvalidStageTypeException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int addIntermediateSprintToStage(int stageId, double location) throws IDNotRecognisedException,
			InvalidLocationException, InvalidStageStateException, InvalidStageTypeException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void removeCheckpoint(int checkpointId) throws IDNotRecognisedException, InvalidStageStateException {
		// TODO Auto-generated method stub

	}

	@Override
	public void concludeStagePreparation(int stageId) throws IDNotRecognisedException, InvalidStageStateException {
		// TODO Auto-generated method stub

	}

	@Override
	public int[] getStageCheckpoints(int stageId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int createTeam(String name, String description) throws IllegalNameException, InvalidNameException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void removeTeam(int teamId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub

	}

	@Override
	public int[] getTeams() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getTeamRiders(int teamId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int createRider(int teamID, String name, int yearOfBirth)
			throws IDNotRecognisedException, IllegalArgumentException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void removeRider(int riderId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub

	}

	@Override
	public void registerRiderResultsInStage(int stageId, int riderId, LocalTime... checkpoints)
			throws IDNotRecognisedException, DuplicatedResultException, InvalidCheckpointTimesException,
			InvalidStageStateException {
		// TODO Auto-generated method stub

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
