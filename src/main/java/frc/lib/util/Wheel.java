package frc.lib.util;

import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.units.Measure;

public class Wheel {
  public final Distance circumfrence;
  public final Distance radius;

  public Wheel(Distance radius) {
    this.radius = radius;
    circumfrence = radius.times(2 * Math.PI);
  }
}