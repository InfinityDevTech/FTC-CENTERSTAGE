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

@Autonomous(group = "Red")
class RedAuto252Edition : LinearOpMode() {

    override fun runOpMode() {
        val robot = Robot(this).init()

        val drive = SampleMecanumDrive(robot)
        val arm = Arm(robot)

        val locator = ElementDetector(robot)

        locator.setAlliancePipe("red")

        val start = Pose2d(-38.0, -61.0, Math.toRadians(-90.0))
        drive.poseEstimate = start

        var zone: Int by Delegates.notNull<Int>();

        while(!isStarted) {
            zone = locator.get_element_zone()
            telemetry.addLine("Zone: $zone")
            telemetry.update()
        }

        waitForStart()

        arm.grabItLikeItsCold()

        val trajectory = drive.trajectorySequenceBuilder(start)
                .lineTo(Vector2d(-38.0, -47.0))
                .turn(Math.toRadians(180.0))
                .lineTo(Vector2d(-34.5, -37.0))
        when (zone) {
            3 -> {
                trajectory
                        .turn(Math.toRadians(-90.0))
                        .strafeRight(2.0)
                        //.forward(4.0)
                        .addDisplacementMarker {
                            arm.dropItLikeItsHot()
                        }
                        //.back(10.0)
            }
            2 -> {
                trajectory.lineTo(Vector2d(-34.5, -32.6))
                .addDisplacementMarker {
                    arm.dropItLikeItsHot()
                }
                //.lineTo(Vector2d(-34.5, -48.0))
            }
            1 -> {
                trajectory.turn(Math.toRadians(-90.0))
                        .strafeLeft(8.0)
                        .forward(4.0)
                        .addDisplacementMarker {
                            arm.dropItLikeItsHot()
                        }
                        //.back(4.0)
                        //.lineTo(Vector2d(-34.5, -48.0))
            }
        }

        /*val built = trajectory
                .addDisplacementMarker {
                    arm.mobile_mode()
                }
                .lineTo(Vector2d(-55.0, -48.0))
                .lineTo(Vector2d(-55.0, -12.0))
                .lineTo(Vector2d(57.0, -12.0))
                .lineTo(Vector2d(57.0, -25.0))
                .addDisplacementMarker {
                    arm.takeAChillPill()
                }
        */
                val built = trajectory
                .build()

        telemetry.addLine("POS: {}" + arm.update_auto_pos())

        //arm.auto_pos();

        drive.followTrajectorySequence(built)

        while (opModeIsActive()) {
            //drive.update();
            arm.update_auto_pos()
            telemetry.update()
        }
    }
}