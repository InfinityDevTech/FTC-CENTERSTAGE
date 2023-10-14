package org.firstinspires.ftc.teamcode.freeWifi.DriveModes

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.teamcode.freeWifi.Constants
import org.firstinspires.ftc.teamcode.freeWifi.Robot.Motors
import org.firstinspires.ftc.teamcode.freeWifi.Robot.Robot
import kotlin.time.Duration.Companion.milliseconds

@TeleOp(name = "Mecanum", group = "TelPrimary")
class Mecanum : LinearOpMode() {

    private val robot = Robot(this);

    // INIT
    override fun runOpMode() {
        robot.init();

        telemetry.update();

        telemetry.addLine("[ROBOT]: " + robot.currentState);
        telemetry.update();

        waitForStart();

        // Pre loop variable initialization
        val gamepad1: Gamepad = this.gamepad1;
        val gamepad2: Gamepad = this.gamepad2;

        var speed: Double = 1.0;
        var lastTime: Long = System.currentTimeMillis();

        while (opModeIsActive()) {

            // Delta Time is the time between loops
            fun deltaTime(): Float {
                val t = (lastTime - System.currentTimeMillis()).toFloat()
                lastTime = System.currentTimeMillis()
                return t
            }

            // Per loop variable initialization
            val decreaseSpeed = gamepad1.left_bumper;
            val increaseSpeed = gamepad1.right_bumper;
            telemetry.addData("Touchpad 1", gamepad1.touchpad)
            telemetry.addData("Touchpad 2", gamepad1.touchpad_finger_1)
            telemetry.addData("Touchpad 3", gamepad1.touchpad_finger_1_x)
            telemetry.addData("Touchpad 3", gamepad1.touchpad_finger_1_y)

            telemetry.addData("Current speed", speed);
            telemetry.addData("Speed Change", (Constants.speed_modifier));
            telemetry.addData("Speed Change", (Constants.speed_modifier * deltaTime()));
            if (decreaseSpeed) {
                if (speed - (Constants.speed_modifier * deltaTime()) < 0.0) {
                    speed = 0.0;
                } else {
                    speed += (Constants.speed_modifier * deltaTime());
                }
            } else if (increaseSpeed) {
                if (speed + (Constants.speed_modifier * deltaTime()) > 1.0) {
                    speed = 1.0;
                } else {
                    speed -= (Constants.speed_modifier * deltaTime());
                }
            }

            // Assign movement controls
            val vertical = gamepad1.left_stick_y
            val horizontal = -gamepad1.right_stick_x
            val rotation = gamepad1.left_stick_x.toDouble()

            robot.setMotorPower(Motors.LeftFront, (-rotation + vertical + horizontal) * speed)
            robot.setMotorPower(Motors.RightFront, (rotation + vertical + -horizontal) * speed)
            robot.setMotorPower(Motors.LeftBack, (-rotation + vertical + -horizontal) * speed)
            robot.setMotorPower(Motors.RightBack, (rotation + vertical + horizontal) * speed)


            telemetry.addData("Loop Time (ms)", deltaTime())
            telemetry.update();
        }
    }
}