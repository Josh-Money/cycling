package cycling;

public class Checkpoint {
    private double location;
    private CheckpointType type;

    public Checkpoint(double location, CheckpointType type) {
        this.location = location;
        this.type = type;
    }

    public Double getLocation() {
        return location;
    }

    public CheckpointType getType() {
        return type;
    }
}
