package cycling;

public class CyclingRider {
    
    private int riderId;
    private int teamID;
    private String name;
    private int yearOfBirth;

    public CyclingRider(int riderId, int teamID, String name, int yearOfBirth) {
        this.riderId = riderId;
        this.teamID = teamID;
        this.name = name;
        this.yearOfBirth = yearOfBirth;
    }

    public int getRiderId() {
        return riderId;
    }

    public int getTeamID() {
        return teamID;
    }

    public String getName() {
        return name;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    @Override
    public String toString() {
        return "CyclingRider{" +
                "riderId=" + riderId +
                ", teamID=" + teamID +
                ", name=" + name +
                ", yearOfBirth=" + yearOfBirth +
                '}';
    }
}
