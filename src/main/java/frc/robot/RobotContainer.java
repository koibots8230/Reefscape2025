// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

// STEP 1: Add the epilogue @Logged annotation
public class RobotContainer {

  // STEP 2: Add a private final Swerve

  // STEP 4: Add a private final CommandXBoxController for the driver's controller

  public RobotContainer() {

    // STEP 2: Initialize the Swerve

    // STEP 4: Initialize the driver's CommandXboxController (slot 0)

    configureBindings();
  }

  private void configureBindings() {

    // STEP 4: Set the Swerve default command to Swerve's driveFieldRelative command.
    // Pass in the CommandXBoxController supplier methods
  }

  public void teleopInit() {}

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}
