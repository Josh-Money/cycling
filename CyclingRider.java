package cycling;

public class CyclingRider {
    
    private int riderId;
    private int teamId;
    private String name;
    private int yearOfBirth;

    public CyclingRider(int riderId, int teamId, String name, int yearOfBirth) {
        this.riderId = riderId;
        this.teamId = teamId;
        this.name = name;
        this.yearOfBirth = yearOfBirth;
    }

    public int getRiderId() {
        return riderId;
    }

    public int getTeamId() {
        return teamId;
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
                ", teamId=" + teamId +
                ", name=" + name +
                ", yearOfBirth=" + yearOfBirth +
                '}';
    }
}
