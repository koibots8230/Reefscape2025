package frc.robot.subsystems;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.Inches;
import static edu.wpi.first.units.Units.Meters;
import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.MetersPerSecondPerSecond;
import static edu.wpi.first.units.Units.Millimeters;
import static edu.wpi.first.units.Units.Seconds;
import static edu.wpi.first.units.Units.Volts;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.math.controller.ElevatorFeedforward;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.units.measure.LinearVelocity;
import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ElevatorConstants;
import frc.robot.Constants.RobotConstants;

@Logged
public class Elevator extends SubsystemBase {

  private final SparkMax leftMotor;
  private final SparkMax rightMotor;
  private final SparkMaxConfig motorConfig;
  private final TrapezoidProfile profile;
  private final RelativeEncoder leftMotorEncoder;
  private final RelativeEncoder rightMotorEncoder;
  private final ElevatorFeedforward feedforward;
  private final DigitalInput HallEffectsSensor;
  private TrapezoidProfile.State goal;
  private TrapezoidProfile.State motorSetpoint;
  private Distance setpoint;
  private Distance leftPosition;
  private Distance rightPosition;
  private LinearVelocity leftVelocity;
  private LinearVelocity rightVelocity;
  private Voltage leftVoltage;
  private Voltage rightVoltage;
  private Current leftCurrent;
  private Current rightCurrent;

  public Elevator() {
    profile =
        new TrapezoidProfile(
            new TrapezoidProfile.Constraints(
                ElevatorConstants.MAX_VELOCITY.in(MetersPerSecond),
                ElevatorConstants.MAX_ACCELRATION.in(MetersPerSecondPerSecond)));

    leftMotor = new SparkMax(ElevatorConstants.LEFT_MOTOR_ID, MotorType.kBrushless);
    rightMotor = new SparkMax(ElevatorConstants.RIGHT_MOTOR_ID, MotorType.kBrushless);
    motorConfig = new SparkMaxConfig();
    motorConfig.idleMode(IdleMode.kBrake);
    motorConfig
        .closedLoop
        .feedbackSensor(FeedbackSensor.kAlternateOrExternalEncoder)
        .pid(ElevatorConstants.PID.kp, ElevatorConstants.PID.ki, ElevatorConstants.PID.kd);
    motorConfig.smartCurrentLimit((int) ElevatorConstants.CURRENT_LIMIT.in(Amps));
    motorConfig.alternateEncoder.positionConversionFactor(ElevatorConstants.CONVERSION_FACTOR.in(Meters));
    motorConfig.alternateEncoder.velocityConversionFactor(ElevatorConstants.CONVERSION_FACTOR.in(Meters));
    leftMotor.configure(motorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    rightMotor.configure(motorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    leftMotorEncoder = leftMotor.getAlternateEncoder();
    rightMotorEncoder = rightMotor.getAlternateEncoder();

    feedforward =
        new ElevatorFeedforward(
            ElevatorConstants.FEEDFORWARD.ks,
            ElevatorConstants.FEEDFORWARD.kg,
            ElevatorConstants.FEEDFORWARD.kv,
            ElevatorConstants.FEEDFORWARD.ka);

    HallEffectsSensor = new DigitalInput(0);

    goal = new TrapezoidProfile.State();
    setpoint = ElevatorConstants.START_SETPOINT;
    motorSetpoint = new TrapezoidProfile.State();

    leftPosition = Distance.ofBaseUnits(0, Meters);
    rightPosition = Distance.ofBaseUnits(0, Meters);
    leftMotorEncoder.setPosition(0);
    rightMotorEncoder.setPosition(0);
    leftVelocity = LinearVelocity.ofBaseUnits(leftMotorEncoder.getVelocity(), MetersPerSecond);
    rightVelocity = LinearVelocity.ofBaseUnits(rightMotorEncoder.getVelocity(), MetersPerSecond);
    leftVoltage = Voltage.ofBaseUnits(leftMotor.getBusVoltage() * leftMotor.getAppliedOutput(), Volts);
    rightVoltage = Voltage.ofBaseUnits(rightMotor.getBusVoltage() * rightMotor.getAppliedOutput(), Volts);
    leftCurrent = Current.ofBaseUnits(leftMotor.getOutputCurrent(), Amps);
    rightCurrent = Current.ofBaseUnits(rightMotor.getOutputCurrent(), Amps);
  }

  @Override
  public void periodic() {
    goal =
        new TrapezoidProfile.State(
            setpoint.in(Meters), 0);

    motorSetpoint = profile.calculate(RobotConstants.ROBOT_CLOCK_SPEED.in(Seconds), motorSetpoint, goal);

    leftMotor
        .getClosedLoopController()
        .setReference(
            motorSetpoint.position,
            ControlType.kPosition,
            ClosedLoopSlot.kSlot0,
            feedforward.calculate(motorSetpoint.velocity));
    
    rightMotor
        .getClosedLoopController()
        .setReference(
            motorSetpoint.position,
            ControlType.kPosition,
            ClosedLoopSlot.kSlot0,
            feedforward.calculate(motorSetpoint.velocity));

    leftPosition = Distance.ofBaseUnits(leftMotorEncoder.getPosition(), Meters);
    rightPosition = Distance.ofBaseUnits(rightMotorEncoder.getPosition(), Meters);
    leftVelocity = LinearVelocity.ofBaseUnits(leftMotorEncoder.getVelocity(), MetersPerSecond);
    rightVelocity = LinearVelocity.ofBaseUnits(rightMotorEncoder.getVelocity(), MetersPerSecond);
    leftVoltage = Voltage.ofBaseUnits(leftMotor.getBusVoltage() * leftMotor.getAppliedOutput(), Volts);
    rightVoltage = Voltage.ofBaseUnits(rightMotor.getBusVoltage() * rightMotor.getAppliedOutput(), Volts);
    leftCurrent = Current.ofBaseUnits(leftMotor.getOutputCurrent(), Amps);
    rightCurrent = Current.ofBaseUnits(rightMotor.getOutputCurrent(), Amps);
  }

  @Override
  public void simulationPeriodic() {
    leftPosition = setpoint;
    rightPosition = setpoint;
  }

  private void setPosition(Distance position) {
    setpoint = position;
  }

  public Command setPositionCommand(Distance position) {
    return Commands.runOnce(() -> this.setPosition(position), this);
  }

  public Command setZeroPositionCommand() {
    return Commands.sequence(
      Commands.race(
        Commands.run(() -> setPosition(Distance.ofBaseUnits(setpoint.in(Millimeters) - 5, Millimeters)), this),
        Commands.waitUntil(() -> HallEffectsSensor.get() == true)
      ),
      Commands.runOnce(() -> leftMotorEncoder.setPosition(0), this),
      Commands.runOnce(() -> rightMotorEncoder.setPosition(0), this)
    );
  }
}
