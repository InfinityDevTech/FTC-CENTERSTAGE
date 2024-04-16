package org.firstinspires.ftc.teamcode.freeWifi.Autonomous

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.freeWifi.RR.SampleMecanumDrive
import org.firstinspires.ftc.teamcode.freeWifi.Robot.Arm
import org.firstinspires.ftc.teamcode.freeWifi.Robot.ElementDetector
import org.firstinspires.ftc.teamcode.freeWifi.Robot.Robot
import kotlin.properties.Delegates

@Autonomous(group = "Blue")
class BlueAuto : LinearOpMode() {

    override fun runOpMode() {
        val robot = Robot(this).init()
        val drive = SampleMecanumDrive(robot)
        val arm = Arm(robot)
        val locator = ElementDetector(robot)

        locator.setAlliancePipe("blue")

        drive.poseEstimate = Pose2d(-38.0, 61.0, Math.toRadians(90.0))
        var zone: Int by Delegates.notNull<Int>();
        while(!isStarted) {
            zone = locator.get_element_zone()
            telemetry.addLine("Zone: $zone")
            telemetry.update()
        }
        locator.close();

        val path = drive.trajectorySequenceBuilder(drive.poseEstimate);

            when (zone) {
                3 -> {
                    path
                    .lineTo(Vector2d(-38.0, 35.0))
                    .turn(Math.toRadians(-90.0))
                        .forward(9.0)
                        .addSpatialMarker(Vector2d(-29.0, 35.0)) {
                            arm.drop_pos()
                        }
                        .back(12.0)
                        .splineToSplineHeading(Pose2d(-52.0, 15.0, Math.toRadians(90.0)), 25.0)
                    .splineToSplineHeading(Pose2d(38.0, 52.0, Math.toRadians(-180.0)), Math.toRadians(75.0))
                }
                2 -> {
                    path.lineTo(Vector2d(-37.0, 22.0))
                        .addSpatialMarker(Vector2d(-34.0, 30.0)) {
                            arm.drop_pos()
                        }
                        .splineToSplineHeading(Pose2d(38.0, 40.0, Math.toRadians(-180.0)), Math.toRadians(75.0))
                }
                1 -> {
                    path.lineTo(Vector2d(-52.0, 50.0))
                        .addSpatialMarker(Vector2d(-52.0, 40.0)) {
                            arm.drop_pos()
                        }
                    .lineTo(Vector2d(-52.0, 25.0))
                    .splineToSplineHeading(Pose2d(38.0, 31.0, Math.toRadians(-180.0)), Math.toRadians(75.0))
                }
                else -> {}
            }

        val built_path = path
            .addTemporalMarker(9.0) {
                arm.placement_pos()
            }
            .back(5.5)
            .addTemporalMarker {
                arm.dropItLikeItsHot()
            }
            .forward(10.0)
            .addTemporalMarker {
                arm.zero_pos()
            }
            .lineTo(Vector2d(38.0, 12.0))
            .back(10.0)
            .build()

        arm.grabItLikeItsCold()
        locator.start_april_pipe()
        drive.followTrajectorySequence(built_path)

        while (opModeIsActive()) {
            locator.april_telemetry()
            telemetry.update()
        }
    }
}