package org.firstinspires.ftc.teamcode.freeWifi.Autonomous

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.freeWifi.RR.SampleMecanumDrive
import org.firstinspires.ftc.teamcode.freeWifi.RR.trajectorysequence.TrajectorySequence

@Autonomous(group = "Left")
class LeftAuto : LinearOpMode() {
    override fun runOpMode() {
        val drive = SampleMecanumDrive(hardwareMap);

        val start = Pose2d(-38.0, 61.0, Math.toRadians(-90.0))

        val trajectory = drive.trajectorySequenceBuilder(start)
            .forward(52.0)
            .turn(Math.toRadians(90.0))
            .forward(52.0)
            .turn(Math.toRadians(180.0))
            .strafeRight(49.0)
            .back(45.0)
            .build()


        waitForStart();
        while (opModeIsActive()) {
            drive.followTrajectorySequence(trajectory)
            telemetry.addLine("AUTO HAS NOT BEEN IMPLEMENTED YET");
            telemetry.update()
        }
    }
}