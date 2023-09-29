package org.firstinspires.ftc.teamcode.freeWifi.DriveModes

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.freeWifi.Robot.Robot;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.freeWifi.Constants

@TeleOp(name = "Mecanum", group = "TelPrimary")
class Mecanum : LinearOpMode() {

    private val robot = Robot(this);

    // INIT
    override fun runOpMode() {
        robot.init();

        telemetry.update();
        waitForStart();

        var lastLoopTime: Long = System.nanoTime()
        while (opModeIsActive()) {
            if (Constants.Debug) {
                val currentTime = System.nanoTime()
                val deltaTime = currentTime-lastLoopTime
                lastLoopTime = currentTime
                telemetry.addData("Loop Time (ns)", deltaTime)
                telemetry.addData("Loop Time (ms)", deltaTime/1000000.0)
            }
            telemetry.addLine("[ROBOT]: " + robot.currentState);
            customLoop()
            telemetry.update();
        }

    }

    /*
     * This is the custom loop that will be used. It runs as fast as possible.
     */
    private fun customLoop() {
    }
}