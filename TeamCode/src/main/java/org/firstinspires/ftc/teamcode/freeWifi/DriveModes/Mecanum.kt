package org.firstinspires.ftc.teamcode.freeWifi.DriveModes

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.teamcode.freeWifi.Constants
import org.firstinspires.ftc.teamcode.freeWifi.Robot.Arm
import org.firstinspires.ftc.teamcode.freeWifi.Robot.Intake
import org.firstinspires.ftc.teamcode.freeWifi.Robot.Motors
import org.firstinspires.ftc.teamcode.freeWifi.Robot.Robot
import org.firstinspires.ftc.vision.VisionPortal
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor


@TeleOp(name = "Mecanum", group = "TelPrimary")
class Mecanum : LinearOpMode() {

    val robot = Robot(this);

    private var aprilTag: AprilTagProcessor? = null

    private var visionPortal: VisionPortal? = null

    private var fast_mode: Boolean = false;
    private var mirror: Boolean = false;

    // INIT
    override fun runOpMode() {
        robot.init();

        initAprilTag();

        telemetry.update();

        telemetry.addLine("[ROBOT]: " + robot.currentState);
        telemetry.update();

        waitForStart();

        val arm = Arm(robot);
        val intake = Intake(robot);

        // Pre loop variable initialization
        val gamepad1: Gamepad = this.gamepad1;
        val gamepad2: Gamepad = this.gamepad2;
        var lastTime: Long = System.currentTimeMillis();

        while (opModeIsActive()) {

            var speed: Double = 0.5;

            arm.run_movement();
            arm.run_logs();
            intake.run_movement();


            // Delta Time is the time between loops
            fun deltaTime(): Float {
                val t = (lastTime - System.currentTimeMillis()).toFloat()
                lastTime = System.currentTimeMillis()
                return t
            }

            // Per loop variable initialization
            if (gamepad1.left_bumper) fast_mode = !fast_mode;

            if (fast_mode) speed = 1.0;

            if (gamepad1.right_bumper) mirror = !mirror;

            var vertical: Float;
            var horizontal: Float;
            var rotation: Float;

            /*if (mirror) {
                vertical = gamepad1.right_stick_x
                horizontal = -gamepad1.left_stick_x
                rotation = gamepad1.left_stick_y
            } else {*/
            vertical = -gamepad1.right_stick_x
            horizontal = gamepad1.left_stick_x
            rotation = -gamepad1.left_stick_y
            //}


            robot.setMotorPower(Motors.LeftFront, (-rotation + vertical + horizontal) * speed)
            robot.setMotorPower(Motors.RightFront, (rotation + vertical + -horizontal) * speed)
            robot.setMotorPower(Motors.LeftBack, (-rotation + vertical + -horizontal) * speed)
            robot.setMotorPower(Motors.RightBack, (rotation + vertical + horizontal) * speed)

            val det = aprilTag?.detections;

            if (det?.size != 0) {
                telemetry.addData("Detections", det?.size);
                telemetry.addData("Detected: ", det?.first()?.id)
            }

            telemetry.addData("Loop Time (ms)", deltaTime())
            telemetry.update();
        }
    }

    private fun initAprilTag() {

        // Create the AprilTag processor the easy way.
        aprilTag = AprilTagProcessor.easyCreateWithDefaults()

        // Create the vision portal the easy way.
        if (true) {
            visionPortal = VisionPortal.easyCreateWithDefaults(
                hardwareMap[WebcamName::class.java, "Webcam 1"], aprilTag
            )
        } else {
            visionPortal = VisionPortal.easyCreateWithDefaults(
                BuiltinCameraDirection.BACK, aprilTag
            )
        }
    }
}