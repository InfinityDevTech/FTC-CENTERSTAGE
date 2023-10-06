package org.firstinspires.ftc.teamcode.freeWifi.DriveModes

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.teamcode.freeWifi.Robot.Motors
import org.firstinspires.ftc.teamcode.freeWifi.Robot.Robot

@TeleOp(name = "Mecanum", group = "TelPrimary")
class Mecanum : LinearOpMode() {

    private val robot = Robot(this);

    private val move_speed = 1.0;

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
        var lastLoopEndTime: Long = System.nanoTime();

        while (opModeIsActive()) {

            // Delta Time is the time between loops
            val deltaTime: Long = (System.nanoTime() - lastLoopEndTime) / 1000;

            // Per loop variable initialization
            val decreaseSpeed = gamepad1.left_bumper;
            val increaseSpeed = gamepad1.right_bumper;

            // Assign movement controls
            val vertical = gamepad1.left_stick_y
            val horizontal = -gamepad1.left_stick_x
            val rotation = gamepad1.right_stick_x.toDouble()

            robot.setMotorPower(Motors.LeftFront, (-rotation + vertical + horizontal) * speed)
            robot.setMotorPower(Motors.RightFront, (rotation + vertical + -horizontal) * speed)
            robot.setMotorPower(Motors.LeftBack, (-rotation + vertical + -horizontal) * speed)
            robot.setMotorPower(Motors.RightBack, (rotation + vertical + horizontal) * speed)

            telemetry.addData("Current speed", speed);
            if (decreaseSpeed) {
                speed -= (0.0001 * deltaTime)
            } else if (increaseSpeed) {
                speed += (0.00
                01 * deltaTime)
            }

            telemetry.addData("Loop Time (ms)", deltaTime)
            lastLoopEndTime = System.nanoTime();
            telemetry.update();
        }
    }
}