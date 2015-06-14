package logistic;

public class Instance {
    private int y;
    private double[] x;

    public Instance(double[] x, int label) {
        this.y = label;
        this.x = x;
    }

    public int getLabel() {
        return y;
    }

    public double[] getX() {
        return x;
    }
}
