package cycling;

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

            int climbCheckpointId = portal.addCategorizedClimbToStage(stageId, 2.0, CheckpointType.C3, 30.4, 1.5);
            System.out.println("Climb checkpoint added to stage with checkpoint Id: " + climbCheckpointId);

            int intermediateSprintId = portal.addIntermediateSprintToStage(stageId, 3.5);
            System.out.println("Intermediate sprint checkpoint added to stage with checkpoint Id: " + intermediateSprintId);

            int[] stageCheckpoints = portal.getStageCheckpoints(stageId);
            System.out.println("Checkpoints for this stageID, " + stageId + ": " + stageCheckpoints);

            int teamId = portal.createTeam("Team14", "Description of team A");
            System.out.println("New team created with ID: " + teamId);

            int[] teamIds = portal.getTeams();
            System.out.println("Team Ids: " + Arrays.toString(teamIds));

            int riderId = portal.createRider(teamId, "Rider 1", 2002);
            System.out.println("Rider created with ID: " + riderId);

            int[] riderIds = portal.getTeamRiders(teamId);
            System.out.println("Rider ids: " + Arrays.toString(riderIds));

            // LocalTime[] riderResultsInStage = portal.getRiderResultsInStage(stageId, riderId);
            // System.out.println("Checkpoint times for rider with id: " + riderId + "for stage with id: " + stageId + ": " + riderResultsInStage);

            // LocalTime riderAdjustedTime = portal.getRiderAdjustedElapsedTimeInStage(stageId, riderId);
            //System.out.println("Adjusted time for rider: " + riderId + "for stage: " + stageId + "is: " + riderAdjustedTime);
 
            //int[] rankOfRidersInStage = portal.getRidersRankInStage(stageId); // Test with multiple riders
            //System.out.println("The placement of the riders for this stage, by riderId is: " + rankOfRidersInStage);

            //LocalTime[] rankedAdjustedTime = portal.getRankedAdjustedElapsedTimesInStage(stageId);
            //System.out.println("The placement pf the riders is sorted by their elapsed time: " + rankedAdjustedTime);

            //int[] riderPointsInStage = portal.getRidersPointsInStage(stageId);
            //System.out.println("The array of intermediate sprint  and finish line points for each rider: " + riderPointsInStage);

            //int[] ridersMountainPointsInStage = portal.getRidersMountainPointsInStage(stageId);
            //System.out.println("The array of riders points from mountian summmits: " + ridersMountainPointsInStage);

            //LocalTime[] getGCTimes = portal.getGeneralClassificationTimesInRace(raceId);
            //System.out.println("The list of rider times for the general classification in race " + raceId + ": " + getGCTimes);

            int[] riderPointsInRace = portal.getRidersPointsInRace(raceId);
            System.out.println("The list of riders points from final and intermediate sprints for race " + raceId + ": " + riderPointsInRace);

            int[] riderMountainPointsInRace = portal.getRidersMountainPointsInRace(raceId);
            System.out.println("The list of rider points from mountain summits in race" + raceId + ": " + riderMountainPointsInRace);

            int[] riderGCRank = portal.getRidersGeneralClassificationRank(raceId);
            System.out.println("The list of rider Ids sorted ascending by their adjusted elapsed times for race " + raceId + ": " + riderGCRank);

            int[] riderPointsRank = portal.getRidersPointClassificationRank(raceId);
            System.out.println("The list of rider Ids sorted by their final and intermediate sprints for race " + raceId + ": " + riderPointsRank);

            int[] riderMountainRank = portal.getRidersMountainPointClassificationRank(raceId);
            System.out.println("The list of rider Ids sorted by their mountain summits for race " + raceId + ": " + riderMountainRank);






        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
