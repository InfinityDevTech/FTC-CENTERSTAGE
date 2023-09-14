package org.firstinspires.ftc.teamcode.freeWifi.DriveModes

import android.util.Log
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.freeWifi.Robot.Robot;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.freeWifi.Constants

import org.firstinspires.ftc.teamcode.freeWifi.Utils.RustLink;

@TeleOp(name = "Mecanum", group = "TelPrimary")
class Mecanum : LinearOpMode() {

    private val robot = Robot(this);
    private val rustLink = RustLink(this);

    // INIT
    override fun runOpMode() {
        robot.init();
        rustLink.init();

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
            telemetry.addLine("[ROBOT]: " + robot.CurrentState);
            telemetry.addLine("[RUST]: " + rustLink.CurrentState);
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