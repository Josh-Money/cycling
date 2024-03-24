package cycling;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

public class FunctionalityTest {

    private CyclingPortalImpl portal;
    
    public void setUp() {
        portal = new CyclingPortalImpl();
    }

    public void testGetRaceIds() {
        int[] raceIds = portal.getRaceIds();
        assertNotNull(raceIds);
        assertEquals(0, raceIds.length);
    }

    public void testCreateRace() throws IllegalNameException, InvalidNameException {
        String name = "Tour de Exeter";
        String description = "The hilliest place on earth";
        try {
            int raceId = portal.createRace(name, description);
            assertTrue(raceId > 0);
        } catch(IllegalNameException | InvalidNameException e) {
            e.getMessage();
        }

    }

    public void testViewRaceDetails() throws IDNotRecognisedException {
        String name = "Tour de Exeter";
        String description = "The hilliest place on earth";
        try {
            int raceId = portal.createRace(name, description);

            String details = portal.viewRaceDetails(raceId);
            assertNotNull(details);
        } catch (IllegalNameException | InvalidNameException | IDNotRecognisedException e) {
            e.getMessage();
        }
    }

    public void testRemoveRaceById() throws IDNotRecognisedException {
        String name = "Tour de Exeter";
        String description = "The hilliest place on earth";
        try {
            int raceId = portal.createRace(name, description);

            portal.removeRaceById(raceId);

            int[] raceIds = portal.getRaceIds();
            assertEquals(0, raceIds.length);
        } catch (IllegalNameException | InvalidNameException | IDNotRecognisedException e) {
            e.getMessage();
        }
    }

    public void testGetNumberOfStages() throws IDNotRecognisedException{
        String name = "Tour de Exeter";
        String description = "The hilliest place on earth";
       
        try {
            int raceId = portal.createRace(name, description);
            
            String stageName = "Cardiac";
            String stageDescription = "Death";
            Double length = 2.3;
            LocalDateTime startTime = LocalDateTime.of(2024, 8, 1, 9, 0);
            StageType type = StageType.HIGH_MOUNTAIN;
            
            int stageId = portal.addStageToRace(raceId, stageName, stageDescription, length, startTime, type);

            int numberOfStages = portal.getNumberOfStages(raceId);
            assertEquals(1, numberOfStages);
        } catch (IllegalNameException | InvalidNameException | IDNotRecognisedException e) {
            e.getMessage();
        }
    }

    public void testAddStageToRace() throws IDNotRecognisedException, IllegalNameException, InvalidNameException, InvalidLengthException {
        String raceName = "Tour de Exeter";
        String raceDescription = "The hilliest place on earth";

        try {
            int raceId = portal.createRace(raceName, raceDescription);

            String stageName = "Cardiac";
            String stageDescription = "Death";
            Double length = 2.3;
            LocalDateTime startTime = LocalDateTime.of(2024, 8, 1, 9, 0);
            StageType type = StageType.HIGH_MOUNTAIN;
            
            int stageId = portal.addStageToRace(raceId, stageName, stageDescription, length, startTime, type);
            assertTrue(stageId > 0);
        } catch (IDNotRecognisedException | IllegalNameException | InvalidNameException | InvalidLengthException e) {
            e.getMessage();
        }
    }

    public void testGetRaceStages() throws IDNotRecognisedException {
        String raceName = "Tour de Exeter";
        String raceDescription = "The hilliest place on earth";

        try {
            int raceId = portal.createRace(raceName, raceDescription);

            String stageName = "Cardiac";
            String stageDescription = "Death";
            Double length = 2.3;
            LocalDateTime startTime = LocalDateTime.of(2024, 8, 1, 9, 0);
            StageType type = StageType.HIGH_MOUNTAIN;
            
            int stageId = portal.addStageToRace(raceId, stageName, stageDescription, length, startTime, type);

            int[] stageIds = portal.getRaceStages(raceId);
            assertNotNull(stageIds);
            assertEquals(1, stageIds.length);
        } catch (IDNotRecognisedException | IllegalNameException | InvalidNameException | InvalidLengthException e) {
            e.getMessage();
        }
    }
    
    public void testGetStageLength() throws IDNotRecognisedException {
        String raceName = "Tour de Exeter";
        String raceDescription = "The hilliest place on earth";
        String stageName = "Cardiac";
        String stageDescription = "Death";
        Double length = 2.3;
        LocalDateTime startTime = LocalDateTime.of(2024, 8, 1, 9, 0);
        StageType type = StageType.HIGH_MOUNTAIN;
        try {
            int raceId = portal.createRace(raceName, raceDescription);
            int stageId = portal.addStageToRace(raceId, stageName, stageDescription, length, startTime, type);

            double retrievedLength = portal.getStageLength(stageId);
            assertEquals(length, retrievedLength, 0.01); //Allow for small delta for comparison
        } catch (IDNotRecognisedException | IllegalNameException | InvalidNameException | InvalidLengthException e) {
            e.getMessage(); 
        }
    }

    public void testRemoveStageById() throws IDNotRecognisedException {
        String raceName = "Tour de Exeter";
        String raceDescription = "The hilliest place on earth";
        String stageName = "Cardiac";
        String stageDescription = "Death";
        Double length = 2.3;
        LocalDateTime startTime = LocalDateTime.of(2024, 8, 1, 9, 0);
        StageType type = StageType.HIGH_MOUNTAIN;
        try {
            int raceId = portal.createRace(raceName, raceDescription);
            int stageId = portal.addStageToRace(raceId, stageName, stageDescription, length, startTime, type);

            portal.removeStageById(stageId);

            int[] stageIds = portal.getRaceStages(raceId);
            assertEquals(0, stageIds.length); // Assuming only 1 stage was added to race
        } catch (IDNotRecognisedException | IllegalNameException | InvalidNameException | InvalidLengthException e) {
            e.getMessage();
        }
    }

    public void testAddCatergorisedClimbToStage()throws IDNotRecognisedException, InvalidLocationException, 
            InvalidStageStateException, InvalidStageTypeException {

        String raceName = "Tour de Exeter";
        String raceDescription = "The hilliest place on earth";
        String stageName = "Cardiac";
        String stageDescription = "Death";
        Double length = 2.3;
        LocalDateTime startTime = LocalDateTime.of(2024, 8, 1, 9, 0);
        StageType type = StageType.HIGH_MOUNTAIN;
        try {
            int raceId = portal.createRace(raceName, raceDescription);
            int stageId = portal.addStageToRace(raceId, stageName, stageDescription, length, startTime, type);

            Double location = 100.0;
            CheckpointType checkpointType = CheckpointType.HC;
            Double averageGradient = 8.5;
            DOuble climbLength = 10.0;
            int checkpointId =portal.addCategorizedClimbToStage(location, checkpointType, averageGradient, climbLength);
            assertTrue(checkpointId > 0);
            
        } catch (IDNotRecognisedException | InvalidLengthException | InvalidLocationException | InvalidStageStateException | InvalidStageTypeException e) {
            e.getMessage();
        }
    }

    public void testAddIntermediateSprintToStage() throws IDNotRecognisedException, InvalidLocationException,
            InvalidStageStateException, InvalidStageTypeException {

        String raceName = "Tour de Exeter";
        String raceDescription = "The hilliest place on earth";
        String stageName = "Cardiac";
        String stageDescription = "Death";
        Double length = 2.3;
        LocalDateTime startTime = LocalDateTime.of(2024, 8, 1, 9, 0);
        StageType type = StageType.HIGH_MOUNTAIN;
        try {
            int raceId = portal.createRace(raceName, raceDescription);
            int stageId = portal.addStageToRace(raceId, stageName, stageDescription, length, startTime, type);

            double location = 2.3;
            int checkpointId = portal.addIntermediateSprintToStage(stageId, location);
            assertTrue(checkpointId > 0);
        } catch (IDNotRecognisedException | InvalidLocationException | InvalidStageStateException | InvalidStageTypeException e) {
            e.getMessage();
        }
    }

    public void testRemoveCheckpoint() throws IDNotRecognisedException, InvalidStageStateException {
        String raceName = "Tour de Exeter";
        String raceDescription = "The hilliest place on earth";
        String stageName = "Cardiac";
        String stageDescription = "Death";
        Double length = 2.3;
        LocalDateTime startTime = LocalDateTime.of(2024, 8, 1, 9, 0);
        StageType type = StageType.HIGH_MOUNTAIN;
        try {
            int raceId = portal.createRace(raceName, raceDescription);
            int stageId = portal.addStageToRace(raceId, stageName, stageDescription, length, startTime, type);

            double location = 2.3;
            int checkpointId = portal.addIntermediateSprintToStage(stageId, location);

            portal.removeCheckpoint(checkpointId);

            int[] checkpointIds = portal.getStageCheckpoints(stageId);
            assertEquals(0, checkpointIds.length);
        } catch (IDNotRecognisedException | IllegalNameException | InvalidNameException | InvalidLengthException | InvalidLocationException | InvalidStageStateException | InvalidStageTypeException e) {
            e.getMessage();
        }
    }

    public void testConcludeStagePrepartion() throws IDNotRecognisedException, InvalidStageStateException {
        String raceName = "Tour de Exeter";
        String raceDescription = "The hilliest place on earth";
        String stageName = "Cardiac";
        String stageDescription = "Death";
        Double length = 2.3;
        LocalDateTime startTime = LocalDateTime.of(2024, 8, 1, 9, 0);
        StageType type = StageType.HIGH_MOUNTAIN;
        try {
            int raceId = portal.createRace(raceName, raceDescription);
            int stageId = portal.addStageToRace(raceId, stageName, stageDescription, length, startTime, type);

            portal.concludeStagePreparation(stageId);

            CyclingStage stage = portal.getStageById(stageId);
            assertEquals(StageState.WAITING_FOR_RESULTS, stage.getStageState());
        } catch (IDNotRecognisedException | IllegalNameException | InvalidNameException | InvalidLengthException | InvalidStageStateException e) {
            e.getMessage();
        }
    }

    public void testGetStageCheckpoints() throws IDNotRecognisedException {
        String raceName = "Tour de Exeter";
        String raceDescription = "The hilliest place on earth";
        String stageName = "Cardiac";
        String stageDescription = "Death";
        Double length = 2.3;
        LocalDateTime startTime = LocalDateTime.of(2024, 8, 1, 9, 0);
        StageType type = StageType.HIGH_MOUNTAIN;
        try {
            int raceId = portal.createRace(raceName, raceDescription);
            int stageId = portal.addStageToRace(raceId, stageName, stageDescription, length, startTime, type);

            double location = 5.0;
            double secLocation = 4.5;

            portal.addIntermediateSprintToStage(location);
            portal.addIntermediateSprintToStage(secLocation);

            int[] checkpointIds = portal.getStageCheckpoints(stageId);
            assertEquals(2, checkpointIds.length); 
        } catch (IDNotRecognisedException | IllegalNameException | InvalidNameException | InvalidLengthException e) {
            e.getMessage();
        }
    }

    public void testCreateTeam() throws IllegalNameException, InvalidNameException {
        String teamName = "Team A";
        String teamDescription = "Consists of 5 riders";
    
        try{
            int teamId = portal.createTeam(teamName, teamDescription);
            assertTrue(teamId > 0);
        } catch (IllegalNameException | InvalidNameException e) {
            e.getMessage();
        }
    }

    public void testRemoveTeam() throws IDNotRecognisedException {
        String teamName = "Team A";
        String teamDescription = "Consists of 5 riders";
        try {
            int teamId = portal.createTeam(teamName, teamDescription);
            portal.removeTeam(teamId);
            int[] teams = portal.getTeams();
            assertTrue(0, teams.length);
            assertFalse(Arrays.stream(teams).anyMatch(id -> id == teamId));    
        } catch (IllegalNameException | InvalidNameException | IDNotRecognisedException e) {
            e.getMessage();
        }
    }

    public void testGetTeams() {
        try {
            int[] teamIds = portal.getTeams();
            assertEquals(0, teamIds.length); //Initially 0 teams

            int teamId1 = portal.createTeam("Team 3", "Description");
            int teamId2 = portal.createTeam("Team 4", "Description");
            
            teamIds = portal.getTeams();
            assertEquals(2, teamIds.length);

        } catch (IllegalNameException | InvalidNameException | IDNotRecognisedException e) {
            e.getMessage();
        }
    }

    public void testGetTeamRiders() throws IDNotRecognisedException {
        try {
            int teamId = portal.createTeam("Team E", "Description of Team E");

            int[] riders = portal.getTeamRiders(teamId);
            assertEquals(0, riders.length);

            int riderId1 = portal.createRider(teamId, "Rider 1", 1990);
            int riderId2 = portal.createRider(teamId, "Rider 2", 1985);
            
            riders = portal.getTeamRiders(teamId);
            assertEquals(2, riders.length);

        } catch (IDNotRecognisedException | IllegalArgumentException e) {
            e.getMessage();
        }
    }

    public void testCreateRider() throws IDNotRecognisedException, IllegalArgumentException {
        try {
            int teamId = portal.createTeam("Team F", "Description of Team F");

            int riderId = portal.createRider(teamId, "Rider 3", 1988);
            assertTrue(riderId > 0);

        } catch (IDNotRecognisedException | IllegalArgumentException | IllegalNameException | InvalidNameException e) {
            e.getMessage();
        }
    }

    public void testRemoveRider() throws IDNotRecognisedException {
        try {
            int teamId = portal.createTeam("Team G", "Description of Team G");
            int riderId = portal.createRider(teamId, "Rider 4", 1982);

            int[] riders = portal.getTeamRiders(teamId);
            assertEquals(1, riders.length);

            portal.removeRider(riderId);
            riders = portal.getTeamRiders(teamId);
            assertEquals(0, riders.length);
            
        } catch (IDNotRecognisedException | IllegalArgumentException | IllegalNameException | InvalidNameException e) {
            e.getMessage();
        }
    }

    public void testRegisterRiderResultsInStage() throws IDNotRecognisedException, DuplicatedResultException, InvalidCheckpointTimesException, 
            InvalidStageStateException{
        
        int stageId = 1;
        int riderId = 1;
        LocalTime[] checkpoints = {LocalTime.of(12, 0), LocalTime.of(12, 30), LocalTime.of(13, 0)};

        try {
            portal.registerRiderResultsInStage(stageId, riderId, checkpoints);
        } catch(IDNotRecognisedException | DuplicatedResultException | InvalidCheckpointTimesException | InvalidStageStateException e) {
            e.getMessage();
        }
    } 

    public void testGetRiderResultsInStage() throws IDNotRecognisedException {
        int stageId = 1;
        int riderId = 1;

        try {
            LocalTime[] result = portal.getRiderResultsInStage(stageId, riderId);
            assert(result != null) : "Results are empty";
        } catch(IDNotRecognisedException e) {
            e.getMessage();
        }
    }

    public void testGetRiderAdjustedElapsedTimeInStage() throws IDNotRecognisedException {
        int stageId = 1;
        int riderId = 1;

        try {
            LocalTime result = portal.getRiderAdjustedElapsedTimeInStage(stageId, riderId);
            assert(result != null) : "Rider has no results";
        } catch (IDNotRecognisedException e) {
            e.getMessage();
        }
    }

    public void testDeleteRiderResultsInStage() {
        int stageId = 1;
        int riderId = 1;

        try {
            portal.deleteRiderResultsInStage(stageId, riderId);
        } catch (IDNotRecognisedException e) {
            e.getMessage();
        }
    }

    public void test
}




