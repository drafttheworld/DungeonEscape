/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.mine;

/**
 *
 * @author Andrew
 */
public class TargetBoundary {

    private int minDistanceFromCenter;
    private int maxDistanceFromCenter;
    private double targetPercentage;

    public TargetBoundary(int minDistanceFromCenter, int maxDistanceFromCenter, double targetPercentage) {
        if (targetPercentage > 1) {
            throw new IllegalArgumentException("Target percentage must be from 0 and 1, found: " + targetPercentage);
        }

        this.minDistanceFromCenter = minDistanceFromCenter;
        this.maxDistanceFromCenter = maxDistanceFromCenter;
        this.targetPercentage = targetPercentage;
    }

    public int getMinDistanceFromCenter() {
        return minDistanceFromCenter;
    }

    public void setMinDistanceFromCenter(int minDistanceFromCenter) {
        this.minDistanceFromCenter = minDistanceFromCenter;
    }

    public int getMaxDistanceFromCenter() {
        return maxDistanceFromCenter;
    }

    public void setMaxDistanceFromCenter(int maxDistanceFromCenter) {
        this.maxDistanceFromCenter = maxDistanceFromCenter;
    }

    public double getTargetPercentage() {
        return targetPercentage;
    }

    public void setTargetPercentage(double targetPercentage) {
        this.targetPercentage = targetPercentage;
    }

}
