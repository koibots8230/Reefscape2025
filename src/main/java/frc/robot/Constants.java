package frc.robot;

import edu.wpi.first.units.Units;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularAcceleration;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.units.measure.LinearVelocity;
import edu.wpi.first.units.measure.Time;

public class Constants {

  public static class RobotConstants {}

  public static class SwerveConstants {
    public static final LinearVelocity MAX_SPEED =
        LinearVelocity.ofBaseUnits(4.25, Units.MetersPerSecond);
    public static final AngularVelocity MAX_ANGULAR_VELOCITY =
        AngularVelocity.ofBaseUnits(0.75, Units.RotationsPerSecond);
    public static final Time SWERVE_UPDATE_PERIOD = Time.ofBaseUnits(20, Units.Millisecond);
  }

  public static class ElevatorConstants {
    public static final Distance START_SETPOINT = Distance.ofBaseUnits(0, Units.Meters);
    public static final Distance L1_SETPOINT = Distance.ofBaseUnits(1, Units.Meters);
    public static final Distance L2_SETPOINT = Distance.ofBaseUnits(2, Units.Meters);
    public static final Distance L3_SETPOINT = Distance.ofBaseUnits(3, Units.Meters);
    public static final Distance L4_SETPOINT = Distance.ofBaseUnits(4, Units.Meters);

    public static final PIDGains PID = new PIDGains.Builder().kp(0).build();
    public static final FeedforwardGains FEEDFORWARD =
        new FeedforwardGains.Builder().kv(0).kg(0).build();

    public static final AngularVelocity MAX_VELOCITY =
        AngularVelocity.ofBaseUnits(0, Units.RotationsPerSecond);
    public static final AngularAcceleration MAX_ACCELRATION =
        AngularAcceleration.ofBaseUnits(0, Units.RotationsPerSecondPerSecond);

    public static final double CONVERSION_FACTOR = 1;

    public static final Current CURRENT_LIMIT = Current.ofBaseUnits(60, Units.Amps);

    public static final int MOTOR_ID = 0;
  }

  public static class IntakePivotConstants {
    public static final int PIVOT_MOTOR_ID = 9998;
    public static final Angle START_SETPOINT = Angle.ofBaseUnits(0, Units.Degrees);

  }

  //the purpose of this comment is solely to iritate jake >:D

  public static class IntakeConstants {
    public static final int INTAKE_MOTOR_ID = 9998;
  
  
  }

}
