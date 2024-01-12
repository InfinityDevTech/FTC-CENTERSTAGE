package org.firstinspires.ftc.teamcode.freeWifi.Robot

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotor.RunMode
import com.qualcomm.robotcore.hardware.DcMotorSimple

class Intake(var robot: Robot) : IMovementComposable {

    private var toggled: Boolean = false;
    private var toggled_frame: Boolean = false;
    private val intakeMotor: DcMotor? = robot.motors[Motors.Intake];
    override fun run_movement() {
        robot.setMotorMode(Motors.Intake, RunMode.RUN_WITHOUT_ENCODER);
        intakeMotor?.direction = DcMotorSimple.Direction.REVERSE;

        if (robot.opMode.gamepad2.b && !toggled_frame) {
            toggled = !toggled
            toggled_frame = true;
        }
        if (!robot.opMode.gamepad2.b) toggled_frame = false;


        if (toggled) {
            robot.setMotorPower(Motors.Intake, 0.5);
        } else {
            robot.setMotorPower(Motors.Intake, 0.0)
        }
    }
}