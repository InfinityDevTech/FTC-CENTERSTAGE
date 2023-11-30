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

        val dest = Pose2d(0.0, 0.0, 0.0);

        val cycle: TrajectorySequence = drive.trajectorySequenceBuilder(dest)
            .setTangent(Math.toRadians(-10.0))
            .splineToConstantHeading(Vector2d(10.0, 10.0), Math.toRadians(0.0))
            .setTangent(Math.toRadians(180.0))
            .build()
        waitForStart();
        while (opModeIsActive()) {
            drive.followTrajectorySequence(cycle)
            telemetry.addLine("AUTO HAS NOT BEEN IMPLEMENTED YET");
            telemetry.update()
        }
    }
}