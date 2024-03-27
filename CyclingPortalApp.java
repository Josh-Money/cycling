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
            System.out.println(" ");

            int stageId = portal.addStageToRace(raceId, "stage1", "brill", 7.0, LocalDateTime.now(), StageType.FLAT);
            System.out.println("Added stage to race with ID: " + stageId);
            System.out.println(" ");

            int stageId2 = portal.addStageToRace(raceId, "stage2", "brill", 7.0, LocalDateTime.now(), StageType.FLAT);
            System.out.println("Added stage to race with ID: " + stageId2);
            System.out.println(" ");

            int[] raceIds = portal.getRaceIds();
            System.out.println("Race ids: " + Arrays.toString(raceIds));
            System.out.println(" ");

            int[] stageRaceIds = portal.getRaceStages(raceId);
            System.out.println("Stage ids: " + Arrays.toString(stageRaceIds));
            System.out.println(" ");

            double stageLength = portal.getStageLength(stageId);
            System.out.println("Stage length: " + stageLength);
            System.out.println(" ");

            String raceDetails = portal.viewRaceDetails(raceId);
            System.out.println("Details for race with race ID: " + raceId + ": " + raceDetails);
            System.out.println(" ");

            int climbCheckpointId = portal.addCategorizedClimbToStage(stageId, 2.0, CheckpointType.HC, 30.4, 1.0);
            System.out.println("Climb checkpoint added to stage with checkpoint Id: " + climbCheckpointId);
            System.out.println(" ");

            int intermediateSprintId = portal.addIntermediateSprintToStage(stageId, 3.0);
            System.out.println("Intermediate sprint checkpoint added to stage with checkpoint Id: " + intermediateSprintId);
            System.out.println(" ");

            int climbCheckpointId2 = portal.addCategorizedClimbToStage(stageId, 4.0, CheckpointType.C4, 17.5, 1.0);
            System.out.println("Climb checkpoint added to stage with checkpoint Id: " + climbCheckpointId2);
            System.out.println(" ");

            int intermediateSprintId2 = portal.addIntermediateSprintToStage(stageId, 5.0);
            System.out.println("Intermediate sprint checkpoint added to stage with checkpoint Id: " + intermediateSprintId2);
            System.out.println(" ");

            int climbCheckpointId3 = portal.addCategorizedClimbToStage(stageId2, 2.0, CheckpointType.HC, 30.4, 1.0);
            System.out.println("Climb checkpoint added to stage with checkpoint Id: " + climbCheckpointId3);
            System.out.println(" ");

            int intermediateSprintId3 = portal.addIntermediateSprintToStage(stageId2, 3.0);
            System.out.println("Intermediate sprint checkpoint added to stage with checkpoint Id: " + intermediateSprintId3);
            System.out.println(" ");

            int climbCheckpointId4 = portal.addCategorizedClimbToStage(stageId2, 4.0, CheckpointType.C4, 17.5, 1.0);
            System.out.println("Climb checkpoint added to stage with checkpoint Id: " + climbCheckpointId4);
            System.out.println(" ");

            int intermediateSprintId4 = portal.addIntermediateSprintToStage(stageId2, 5.0);
            System.out.println("Intermediate sprint checkpoint added to stage with checkpoint Id: " + intermediateSprintId4);
            System.out.println(" ");

            int[] stageCheckpoints = portal.getStageCheckpoints(stageId);
            System.out.println("Checkpoints for this stageID, " + stageId + ": " + Arrays.toString(stageCheckpoints));
            System.out.println(" ");

            int[] stageCheckpoints2 = portal.getStageCheckpoints(stageId2);
            System.out.println("Checkpoints for this stageID, " + stageId2 + ": " + Arrays.toString(stageCheckpoints2));
            System.out.println(" ");

            int teamId = portal.createTeam("TeamA", "Description of team A");
            System.out.println("New team created with ID: " + teamId);
            System.out.println(" ");

            int teamId2 = portal.createTeam("TeamB", "Description of Team B");
            System.out.println("New team created with ID: " + teamId2);
            System.out.println(" ");

            int teamId3 = portal.createTeam("TeamC", "Description of Team C");
            System.out.println("New team created with ID: " + teamId3);
            System.out.println(" ");

            int[] teamIds = portal.getTeams();
            System.out.println("Team Ids: " + Arrays.toString(teamIds));
            System.out.println(" ");

            int riderId = portal.createRider(teamId, "Rider 1", 2002);
            System.out.println("Rider created with ID: " + riderId);
            System.out.println(" ");

            int riderId2 = portal.createRider(teamId, "Rider 2", 1999);
            System.out.println("Rider created with ID: " + riderId2);
            System.out.println(" ");

            int riderId3 = portal.createRider(teamId2, "Rider 3", 2005);
            System.out.println("Rider created with ID: " + riderId3);
            System.out.println(" ");

            int riderId4 = portal.createRider(teamId2, "Rider 4", 1998);
            System.out.println("Rider created with ID: " + riderId4);
            System.out.println(" ");

            int riderId5 = portal.createRider(teamId3, "Rider 5", 2000);
            System.out.println("Rider created with ID: " + riderId5);
            System.out.println(" ");

            int riderId6 = portal.createRider(teamId3, "Rider 6", 1997);
            System.out.println("Rider created with ID: " + riderId6);
            System.out.println(" ");

            int[] riderIds = portal.getTeamRiders(teamId);
            System.out.println("Rider ids for team " + teamId + ": "  + Arrays.toString(riderIds));
            System.out.println(" ");

            int[] riderIds2 = portal.getTeamRiders(teamId2);
            System.out.println("Rider ids for team " + teamId2 + ": "  + Arrays.toString(riderIds2));
            System.out.println(" ");

            int[] riderIds3 = portal.getTeamRiders(teamId3);
            System.out.println("Rider ids for team " + teamId3 + ": "  + Arrays.toString(riderIds3));
            System.out.println(" ");

            portal.registerRiderResultsInStage(stageId, riderId, LocalTime.of(0, 30, 0), LocalTime.of(1,1,1), LocalTime.of(1, 20, 2), LocalTime.of(2, 40, 8), LocalTime.of(3, 40, 7), LocalTime.of(4, 40, 0));

            portal.registerRiderResultsInStage(stageId, riderId2, LocalTime.of(0, 30, 0), LocalTime.of(2,10,1), LocalTime.of(3, 40, 2), LocalTime.of(5, 50, 8), LocalTime.of(7, 20, 30), LocalTime.of(8, 40, 0));
            
            portal.registerRiderResultsInStage(stageId, riderId3, LocalTime.of(0, 30, 0), LocalTime.of(1,10,1), LocalTime.of(1, 32, 2), LocalTime.of(2, 49, 8), LocalTime.of(4, 10, 57), LocalTime.of(5, 50, 0));

            portal.registerRiderResultsInStage(stageId, riderId4, LocalTime.of(0, 30, 0), LocalTime.of(2,20,1), LocalTime.of(3, 47, 2), LocalTime.of(4, 51, 8), LocalTime.of(5, 50, 47), LocalTime.of(7, 50, 0));

            portal.registerRiderResultsInStage(stageId, riderId5, LocalTime.of(0, 30, 0), LocalTime.of(0,50,1), LocalTime.of(1, 36, 2), LocalTime.of(2, 22, 8), LocalTime.of(3, 46, 27), LocalTime.of(6, 6, 0));

            portal.registerRiderResultsInStage(stageId, riderId6, LocalTime.of(0, 30, 0), LocalTime.of(1,23,1), LocalTime.of(3, 43, 2), LocalTime.of(4, 49, 8), LocalTime.of(6, 3, 46), LocalTime.of(7, 10, 0));

            portal.registerRiderResultsInStage(stageId2, riderId, LocalTime.of(0, 30, 0), LocalTime.of(1,1,1), LocalTime.of(1, 20, 2), LocalTime.of(2, 40, 8), LocalTime.of(3, 40, 7), LocalTime.of(4, 40, 0));

            portal.registerRiderResultsInStage(stageId2, riderId2, LocalTime.of(0, 30, 0), LocalTime.of(2,10,1), LocalTime.of(3, 40, 2), LocalTime.of(5, 50, 8), LocalTime.of(7, 20, 30), LocalTime.of(8, 40, 0));
            
            portal.registerRiderResultsInStage(stageId2, riderId3, LocalTime.of(0, 30, 0), LocalTime.of(1,10,1), LocalTime.of(1, 32, 2), LocalTime.of(2, 49, 8), LocalTime.of(4, 10, 57), LocalTime.of(5, 50, 0));

            portal.registerRiderResultsInStage(stageId2, riderId4, LocalTime.of(0, 30, 0), LocalTime.of(2,20,1), LocalTime.of(3, 47, 2), LocalTime.of(4, 51, 8), LocalTime.of(5, 50, 47), LocalTime.of(7, 50, 0));

            portal.registerRiderResultsInStage(stageId2, riderId5, LocalTime.of(0, 30, 0), LocalTime.of(0,50,1), LocalTime.of(1, 36, 2), LocalTime.of(2, 22, 8), LocalTime.of(3, 46, 27), LocalTime.of(6, 6, 0));

            portal.registerRiderResultsInStage(stageId2, riderId6, LocalTime.of(0, 30, 0), LocalTime.of(1,23,1), LocalTime.of(3, 43, 2), LocalTime.of(4, 49, 8), LocalTime.of(6, 3, 46), LocalTime.of(7, 10, 0));


            portal.removeRider(riderId6);
            System.out.println("Removed rider from race: " + riderId6);
            System.out.println(" ");

            LocalTime[] riderResultsInStage = portal.getRiderResultsInStage(stageId, riderId);
            System.out.println("Checkpoint times for rider " + riderId + " for stage " + stageId + ": ");
            for (LocalTime time : riderResultsInStage) {
                System.out.print(time + " ");
            }
            System.out.print("\n");
            System.out.println(" ");

            LocalTime[] riderResultsInStage2 = portal.getRiderResultsInStage(stageId, riderId2);
            System.out.println("Checkpoint times for rider " + riderId2 + " for stage " + stageId + ": ");
            for (LocalTime time : riderResultsInStage2) {
                System.out.print(time + " ");
            }
            System.out.print("\n");
            System.out.println(" ");

            LocalTime[] riderResultsInStage3 = portal.getRiderResultsInStage(stageId, riderId3);
            System.out.println("Checkpoint times for rider " + riderId3 + " for stage " + stageId + ": ");
            for (LocalTime time : riderResultsInStage3) {
                System.out.print(time + " ");
            }
            System.out.print("\n");
            System.out.println(" ");

            LocalTime[] riderResultsInStage4 = portal.getRiderResultsInStage(stageId, riderId4);
            System.out.println("Checkpoint times for rider " + riderId4 + " for stage " + stageId + ": ");
            for (LocalTime time : riderResultsInStage4) {
                System.out.print(time + " ");
            }
            System.out.print("\n");
            System.out.println(" ");

            LocalTime[] riderResultsInStage5 = portal.getRiderResultsInStage(stageId, riderId5);
            System.out.println("Checkpoint times for rider " + riderId5 + " for stage " + stageId + ": ");
            for (LocalTime time : riderResultsInStage5) {
                System.out.print(time + " ");
            }
            System.out.print("\n");
            System.out.println(" ");

            LocalTime[] riderResultsInStage6 = portal.getRiderResultsInStage(stageId2, riderId);
            System.out.println("Checkpoint times for rider " + riderId + " for stage " + stageId2 + ": ");
            for (LocalTime time : riderResultsInStage6) {
                System.out.print(time + " ");
            }
            System.out.print("\n");
            System.out.println(" ");

            LocalTime[] riderResultsInStage7 = portal.getRiderResultsInStage(stageId2, riderId2);
            System.out.println("Checkpoint times for rider " + riderId2 + " for stage " + stageId2 + ": ");
            for (LocalTime time : riderResultsInStage7) {
                System.out.print(time + " ");
            }
            System.out.print("\n");
            System.out.println(" ");

            LocalTime[] riderResultsInStage8 = portal.getRiderResultsInStage(stageId2, riderId3);
            System.out.println("Checkpoint times for rider " + riderId3 + " for stage " + stageId2 + ": ");
            for (LocalTime time : riderResultsInStage8) {
                System.out.print(time + " ");
            }
            System.out.print("\n");
            System.out.println(" ");

            LocalTime[] riderResultsInStage9 = portal.getRiderResultsInStage(stageId2, riderId4);
            System.out.println("Checkpoint times for rider " + riderId4 + " for stage " + stageId2 + ": ");
            for (LocalTime time : riderResultsInStage9) {
                System.out.print(time + " ");
            }
            System.out.print("\n");
            System.out.println(" ");

            LocalTime[] riderResultsInStage10 = portal.getRiderResultsInStage(stageId2, riderId5);
            System.out.println("Checkpoint times for rider " + riderId5 + " for stage " + stageId2 + ": ");
            for (LocalTime time : riderResultsInStage10) {
                System.out.print(time + " ");
            }
            System.out.print("\n");
            System.out.println(" ");

            LocalTime riderAdjustedTime = portal.getRiderAdjustedElapsedTimeInStage(stageId, riderId);
            System.out.println("Adjusted time for rider " + riderId + " ,for stage " + stageId + " is: " + riderAdjustedTime);
            System.out.println(" ");

            LocalTime riderAdjustedTime2 = portal.getRiderAdjustedElapsedTimeInStage(stageId, riderId2);
            System.out.println("Adjusted time for rider " + riderId2 + " ,for stage " + stageId + " is: " + riderAdjustedTime2);
            System.out.println(" ");

            LocalTime riderAdjustedTime3 = portal.getRiderAdjustedElapsedTimeInStage(stageId, riderId3);
            System.out.println("Adjusted time for rider " + riderId3 + " ,for stage " + stageId + " is: " + riderAdjustedTime3);
            System.out.println(" ");

            LocalTime riderAdjustedTime4 = portal.getRiderAdjustedElapsedTimeInStage(stageId, riderId4);
            System.out.println("Adjusted time for rider " + riderId4 + " ,for stage " + stageId + " is: " + riderAdjustedTime4);
            System.out.println(" ");

            LocalTime riderAdjustedTime5 = portal.getRiderAdjustedElapsedTimeInStage(stageId, riderId5);
            System.out.println("Adjusted time for rider " + riderId5 + " ,for stage " + stageId + " is: " + riderAdjustedTime5);
            System.out.println(" ");

            LocalTime riderAdjustedTime6 = portal.getRiderAdjustedElapsedTimeInStage(stageId2, riderId);
            System.out.println("Adjusted time for rider " + riderId + " ,for stage " + stageId2 + " is: " + riderAdjustedTime6);
            System.out.println(" ");

            LocalTime riderAdjustedTime7 = portal.getRiderAdjustedElapsedTimeInStage(stageId2, riderId2);
            System.out.println("Adjusted time for rider " + riderId2 + " ,for stage " + stageId2 + " is: " + riderAdjustedTime7);
            System.out.println(" ");

            LocalTime riderAdjustedTime8 = portal.getRiderAdjustedElapsedTimeInStage(stageId2, riderId3);
            System.out.println("Adjusted time for rider " + riderId3 + " ,for stage " + stageId2 + " is: " + riderAdjustedTime8);
            System.out.println(" ");

            LocalTime riderAdjustedTime9 = portal.getRiderAdjustedElapsedTimeInStage(stageId2, riderId4);
            System.out.println("Adjusted time for rider " + riderId4 + " ,for stage " + stageId2 + " is: " + riderAdjustedTime9);
            System.out.println(" ");

            LocalTime riderAdjustedTime10 = portal.getRiderAdjustedElapsedTimeInStage(stageId2, riderId5);
            System.out.println("Adjusted time for rider " + riderId5 + " ,for stage " + stageId2 + " is: " + riderAdjustedTime10);
            System.out.println(" ");

            int[] rankOfRidersInStage = portal.getRidersRankInStage(stageId); // Test with multiple riders
            System.out.println("The placement of the riders for this stage," + stageId + " by riderId is: " + Arrays.toString(rankOfRidersInStage));
            System.out.println(" ");

            int[] rankOfRidersInStage2 = portal.getRidersRankInStage(stageId2); // Test with multiple riders
            System.out.println("The placement of the riders for this stage," + stageId2 + " by riderId is: " + Arrays.toString(rankOfRidersInStage2));
            System.out.println(" ");

            LocalTime[] rankedAdjustedTime = portal.getRankedAdjustedElapsedTimesInStage(stageId);
            System.out.print("The placement of the riders is sorted by their elapsed time: ");
            for (LocalTime time : rankedAdjustedTime) {
                System.out.print(time + " ");
            }
            System.out.print("\n");  
            System.out.println(" ");

            LocalTime[] rankedAdjustedTime2 = portal.getRankedAdjustedElapsedTimesInStage(stageId2);
            System.out.print("The placement of the riders is sorted by their elapsed time: ");
            for (LocalTime time : rankedAdjustedTime2) {
                System.out.print(time + " ");
            }
            System.out.print("\n"); 
            System.out.println(" ");

            int[] riderPointsInStage = portal.getRidersPointsInStage(stageId);
            System.out.println("The array of riders intermediate sprint and finish line points: " + Arrays.toString(riderPointsInStage));
            System.out.println(" ");

            int[] ridersMountainPointsInStage = portal.getRidersMountainPointsInStage(stageId);
            System.out.println("The array of riders points from mountain summits: " + Arrays.toString(ridersMountainPointsInStage)); 
            System.out.println(" ");

            int[] riderPointsInStage2 = portal.getRidersPointsInStage(stageId2);
            System.out.println("The array of riders intermediate sprint and finish line points: " + Arrays.toString(riderPointsInStage2));
            System.out.println(" ");

            int[] ridersMountainPointsInStage2 = portal.getRidersMountainPointsInStage(stageId2);
            System.out.println("The array of riders points from mountain summits: " + Arrays.toString(ridersMountainPointsInStage2)); 
            System.out.println(" ");

            LocalTime[] getGCTimes = portal.getGeneralClassificationTimesInRace(raceId);
            System.out.println("General Classification times for race " + raceId + ": ");
            for (LocalTime time : getGCTimes) {
                System.out.print(time + " ");
            }
            System.out.print("\n");
            System.out.println(" ");

            int[] riderPointsInRace = portal.getRidersPointsInRace(raceId);
            System.out.println("Total Sprint points in race " + raceId + ": " + Arrays.toString(riderPointsInRace));
            System.out.println(" ");

            int[] riderMountainPointsInRace = portal.getRidersMountainPointsInRace(raceId);
            System.out.println("Total mountain points in race" + raceId + ": " + Arrays.toString(riderMountainPointsInRace));
            System.out.println(" ");

            int[] riderGCRank = portal.getRidersGeneralClassificationRank(raceId);
            System.out.println("General Classifcation Rank for race " + raceId + ": " + Arrays.toString(riderGCRank));
            System.out.println(" ");

            int[] riderPointsRank = portal.getRidersPointClassificationRank(raceId);
            System.out.println("Sprint points rank for race " + raceId + ": " + Arrays.toString(riderPointsRank));
            System.out.println(" ");

            int[] riderMountainRank = portal.getRidersMountainPointClassificationRank(raceId);
            System.out.println("Mountain points rank for race " + raceId + ": " + Arrays.toString(riderMountainRank));
            System.out.println(" ");

            portal.removeTeam(teamId3);
            int[] teamsAfterRemoval = portal.getTeams();
            System.out.println("Teams after removal: " + Arrays.toString(teamsAfterRemoval));
            System.out.println(" ");

            portal.removeCheckpoint(climbCheckpointId4);
            int[] checkpointsAfterRemoval = portal.getStageCheckpoints(stageId2);
            System.out.println("Checkpoints for stage" + stageId2 + " after removal: " + Arrays.toString(checkpointsAfterRemoval));
            System.out.println(" ");

            portal.removeStageById(stageId);
            int[] stagesAfterRemoval = portal.getRaceStages(raceId);
            System.out.println("Stages for race" + raceId + " after removal: " + Arrays.toString(stagesAfterRemoval));
            System.out.println(" ");

            portal.removeRaceById(raceId);
            int[] raceIdsAfterRemoval = portal.getRaceIds();
            System.out.println("Race IDs after removal " + Arrays.toString(raceIdsAfterRemoval));
            System.out.println(" ");

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
