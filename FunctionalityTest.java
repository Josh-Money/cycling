package cycling;
import java.util.Arrays;

public class FunctionalityTest {
    public int[] testRaceIdList() {
        
        MiniCyclingPortal portal = new MiniCyclingPortal();

        portal.createRace("Exeter", "Flat");
        int[] test = portal.getRaceIds();
        return test;
    }
}



