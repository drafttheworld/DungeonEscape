/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.mine;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andrew
 */
public class TargetBoundaries {

    List<TargetBoundary> targetBoundaries = new ArrayList<>();

    public void addTargetBoundary(TargetBoundary targetBoundary) {
        targetBoundaries.add(targetBoundary);
    }
    
    public void purgeTargetBoundaries() {
        targetBoundaries.clear();
    }
    
    public List<TargetBoundary> getTargetBoundaries() {
        return targetBoundaries;
    }
}
