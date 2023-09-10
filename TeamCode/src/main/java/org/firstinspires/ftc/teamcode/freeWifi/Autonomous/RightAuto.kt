package org.firstinspires.ftc.teamcode.freeWifi.Autonomous

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.OpMode

@Autonomous(group = "Right")
class RightAuto : LinearOpMode() {
    override fun runOpMode() {
        telemetry.addLine("AUTO HAS NOT BEEN IMPLEMENTED YET");
        telemetry.update()
        waitForStart();
        while (opModeIsActive()) {
            telemetry.addLine("AUTO HAS NOT BEEN IMPLEMENTED YET");
            telemetry.update()
        }
    }
}