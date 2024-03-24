package cycling;

import java.time.LocalDateTime;
import java.util.Arrays;

public class CyclingPortalApp {
    public static void main(String[] args) {
        try{
            CyclingPortalImpl portal = new CyclingPortalImpl();

            int raceId = portal.createRace("test", "Testrace");
            System.out.println("Created Race with ID: " + raceId);

            // int stageId = portal.addStageToRace(raceId, "stage1", "brill", 7.0, LocalDateTime.now(), StageType.FLAT);
            // System.out.println("Added stage to race with ID: " + stageId);

            int[] raceIds = portal.getRaceIds();
            System.out.println("Race ids: " + Arrays.toString(raceIds));

            // int[] stageRaceIds = portal.getRaceStages(raceId);
            // System.out.println("Stage ids: " + Arrays.toString(stageRaceIds));

            // double stageLength = portal.getStageLength(stageId);
            // System.out.println("Stage length: " + stageLength);

            String raceDetails = portal.viewRaceDetails(raceId);
            System.out.println("Details for race with race ID: " + raceId + ": " + raceDetails);

            int climbCheckpointId = portal.addCategorizedClimbToStage(raceId, 2.0, CheckpointType.C3, 30.4, 1.5);
            System.out.println("Climb checkpoint added to stage: " + climbCheckpointId);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
