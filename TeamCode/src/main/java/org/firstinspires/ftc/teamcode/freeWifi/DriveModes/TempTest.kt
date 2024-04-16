package org.firstinspires.ftc.teamcode.freeWifi.DriveModes

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.freeWifi.Robot.ElementDetector
import org.firstinspires.ftc.teamcode.freeWifi.Robot.Motors
import org.firstinspires.ftc.teamcode.freeWifi.Robot.Robot

@TeleOp(name = "TempTest", group = "TelPrimary")
class TempTest : LinearOpMode() {
    override fun runOpMode() {
        val robot = Robot(this).init();
        val locator = ElementDetector(robot);
        locator.close();
        locator.start_april_pipe();

        waitForStart()

        while (opModeIsActive()) {
            locator.april_telemetry()
            telemetry.update()
        }
    }

}