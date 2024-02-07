package org.firstinspires.ftc.teamcode.freeWifi.Autonomous

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.freeWifi.RR.DriveConstants
import org.firstinspires.ftc.teamcode.freeWifi.RR.SampleMecanumDrive
import org.firstinspires.ftc.teamcode.freeWifi.RR.trajectorysequence.TrajectorySequence
import org.firstinspires.ftc.teamcode.freeWifi.Robot.Arm
import org.firstinspires.ftc.teamcode.freeWifi.Robot.ElementDetector
import org.firstinspires.ftc.teamcode.freeWifi.Robot.Robot

@Autonomous(group = "Left")
class LeftAuto : LinearOpMode() {

    override fun runOpMode() {
        val robot = Robot(this).init();

        val drive = SampleMecanumDrive(robot);
        val arm = Arm(robot);

        val locator = ElementDetector(robot);

        locator.setAlliancePipe("blue");
        locator.get_element_zone();

        val start = Pose2d(-38.0, 61.0, Math.toRadians(90.0))
        drive.poseEstimate = start;

        val trajectory = drive.trajectorySequenceBuilder(start)
            .lineTo(Vector2d(-38.0, 9.0),
                SampleMecanumDrive.getVelocityConstraint(10.0, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
            )
            .turn(Math.toRadians(90.0))
            .lineTo(
                Vector2d(14.0, 9.0),
                SampleMecanumDrive.getVelocityConstraint(10.0, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
            )
            .turn(Math.toRadians(180.0))
            .lineTo(Vector2d(14.0, 58.0),
                SampleMecanumDrive.getVelocityConstraint(10.0, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
            )
            .lineTo(Vector2d(59.0, 58.0),
                SampleMecanumDrive.getVelocityConstraint(30.0, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
            )
            .build()


        waitForStart();

        //arm.auto_pos();

        while (opModeIsActive()) {
            drive.followTrajectorySequence(trajectory)
            telemetry.addLine("AUTO HAS NOT BEEN IMPLEMENTED YET");
            telemetry.update()
        }
    }
}