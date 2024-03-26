package cycling;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

public class CyclingPortalApp {
    public static void main(String[] args) {
        try{
            CyclingPortalImpl portal = new CyclingPortalImpl();

            int raceId = portal.createRace("test", "Testrace");
            System.out.println("Created Race with ID: " + raceId);

            int stageId = portal.addStageToRace(raceId, "stage1", "brill", 7.0, LocalDateTime.now(), StageType.FLAT);
            System.out.println("Added stage to race with ID: " + stageId);

            int[] raceIds = portal.getRaceIds();
            System.out.println("Race ids: " + Arrays.toString(raceIds));

            int[] stageRaceIds = portal.getRaceStages(raceId);
            System.out.println("Stage ids: " + Arrays.toString(stageRaceIds));

            double stageLength = portal.getStageLength(stageId);
            System.out.println("Stage length: " + stageLength);

            String raceDetails = portal.viewRaceDetails(raceId);
            System.out.println("Details for race with race ID: " + raceId + ": " + raceDetails);

            int climbCheckpointId = portal.addCategorizedClimbToStage(stageId, 2.0, CheckpointType.HC, 30.4, 1.5);
            System.out.println("Climb checkpoint added to stage with checkpoint Id: " + climbCheckpointId);

            int intermediateSprintId = portal.addIntermediateSprintToStage(stageId, 3.5);
            System.out.println("Intermediate sprint checkpoint added to stage with checkpoint Id: " + intermediateSprintId);

            int[] stageCheckpoints = portal.getStageCheckpoints(stageId);
            System.out.println("Checkpoints for this stageID, " + stageId + ": " + Arrays.toString(stageCheckpoints));

            int teamId = portal.createTeam("Team14", "Description of team A");
            System.out.println("New team created with ID: " + teamId);

            int[] teamIds = portal.getTeams();
            System.out.println("Team Ids: " + Arrays.toString(teamIds));

            int riderId = portal.createRider(teamId, "Rider 1", 2002);
            System.out.println("Rider created with ID: " + riderId);

            int riderId2 = portal.createRider(teamId, "Rider 2", 1999);
            System.out.println("Rider created with ID: " + riderId2);

            int[] riderIds = portal.getTeamRiders(teamId);
            System.out.println("Rider ids for team " + teamId + ": "  + Arrays.toString(riderIds));

            portal.registerRiderResultsInStage(stageId, riderId, LocalTime.of(0, 30, 0), LocalTime.of(1,1,1), LocalTime.of(1, 20, 2), LocalTime.of(2, 40, 8));

            portal.registerRiderResultsInStage(stageId, riderId2, LocalTime.of(0, 30, 0), LocalTime.of(2,10,1), LocalTime.of(3, 40, 2), LocalTime.of(5, 50, 8));

            LocalTime[] riderResultsInStage = portal.getRiderResultsInStage(stageId, riderId);
            System.out.println("Checkpoint times for rider " + riderId + " for stage " + stageId + ": ");
            for (LocalTime time : riderResultsInStage) {
                System.out.print(time + " ");
            }
            System.out.print("\n");

            LocalTime riderAdjustedTime = portal.getRiderAdjustedElapsedTimeInStage(stageId, riderId);
            System.out.println("Adjusted time for rider " + riderId + " ,for stage " + stageId + " is: " + riderAdjustedTime);
 
            int[] rankOfRidersInStage = portal.getRidersRankInStage(stageId); // Test with multiple riders
            System.out.println("The placement of the riders for this stage, by riderId is: " + Arrays.toString(rankOfRidersInStage));

            LocalTime[] rankedAdjustedTime = portal.getRankedAdjustedElapsedTimesInStage(stageId);
            System.out.print("The placement of the riders is sorted by their elapsed time: ");
            for (LocalTime time : rankedAdjustedTime) {
                System.out.print(time + " ");
            }
            System.out.print("\n");

            int[] riderPointsInStage = portal.getRidersPointsInStage(stageId);
            System.out.println("The array of intermediate sprint and finish line points for each rider: " + Arrays.toString(riderPointsInStage));

            int[] ridersMountainPointsInStage = portal.getRidersMountainPointsInStage(stageId);
            System.out.println("The array of riders points from mountian summmits: " + Arrays.toString(ridersMountainPointsInStage));

            LocalTime[] getGCTimes = portal.getGeneralClassificationTimesInRace(raceId);
            System.out.println("The list of rider times for the general classification in race " + raceId + ": ");
            for (LocalTime time : getGCTimes) {
                System.out.print(time + " ");
            }
            System.out.print("\n");

            int[] riderPointsInRace = portal.getRidersPointsInRace(raceId);
            System.out.println("The list of riders points from final and intermediate sprints for race " + raceId + ": " + Arrays.toString(riderPointsInRace));

            int[] riderMountainPointsInRace = portal.getRidersMountainPointsInRace(raceId);
            System.out.println("The list of rider points from mountain summits in race" + raceId + ": " + Arrays.toString(riderMountainPointsInRace));

            int[] riderGCRank = portal.getRidersGeneralClassificationRank(raceId);
            System.out.println("The list of rider Ids sorted ascending by their adjusted elapsed times for race " + raceId + ": " + Arrays.toString(riderGCRank));

            int[] riderPointsRank = portal.getRidersPointClassificationRank(raceId);
            System.out.println("The list of rider Ids sorted by their final and intermediate sprints for race " + raceId + ": " + Arrays.toString(riderPointsRank));

            int[] riderMountainRank = portal.getRidersMountainPointClassificationRank(raceId);
            System.out.println("The list of rider Ids sorted by their mountain summits for race " + raceId + ": " + Arrays.toString(riderMountainRank));
            
            String filename = "SavedCyclingPortalImpl.java";
            try {
                portal.saveCyclingPortal(filename);
                System.out.println("Cycling portal saved to " + filename);
            } catch (IOException ex) {
                System.err.println("Failed to save cycling portal data: " + ex.getMessage());
                ex.printStackTrace();
            }

            portal.eraseCyclingPortal();
            System.out.println("Cycling portal data erased.");

            int[] racesAfterErase = portal.getRaceIds();
            System.out.println("Races in system after erase: " + Arrays.toString(racesAfterErase));

            int[] teamsAfterErase = portal.getTeams();
            System.out.println("Teams in system after erase: " + Arrays.toString(teamsAfterErase));

            try {
                portal.loadCyclingPortal(filename);
                System.out.println("Cycling portal data loaded from: " + filename);

                int[] racesAfterLoad = portal.getRaceIds();
                System.out.println("Races in system after load: " + Arrays.toString(racesAfterLoad));
            } catch(IOException | ClassNotFoundException ex) {
                System.err.println("Failed to load cycling portal data: " + ex.getMessage());
            }

            System.out.println("Testing completed successfully.");






        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
