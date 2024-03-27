package cycling;

public class CatergorizedClimbCheckpoint extends Checkpoint {
    private CheckpointType type;
    private double averageGradient;
    private double length;

    public CatergorizedClimbCheckpoint(double location, CheckpointType type, 
    double averageGradient, double length) {
        super(location, type);
        this.averageGradient = averageGradient;
        this.length = length;
    }

    public double getAverageGradient() {
        return averageGradient;
    }

    public double getLength() {
        return length;
    }
}
