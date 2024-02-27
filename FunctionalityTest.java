package cycling;
import cycling.CyclingPortalImpl;
import java.util.Arrays;

public class FunctionalityTest {

    public static void main(String[] args) {
        // Instantiate CyclingPortalImpl
        CyclingPortalImpl cyclingPortal = new CyclingPortalImpl();

        // Assuming you have some races added to the portal, call getRaceIds
        int[] raceIds = cyclingPortal.getRaceIds();

        // Print the raceIds
        System.out.println("Race IDs: " + Arrays.toString(raceIds));
    }
}



