package org.firstinspires.ftc.teamcode.freeWifi.Robot

import com.qualcomm.robotcore.hardware.Gamepad
import com.outoftheboxrobotics.photoncore.PhotonCore;

class Initialize(private val robot: Robot) {
    fun init() {
        PhotonCore.enable();
        robot.halfTimeRumble = Gamepad.RumbleEffect.Builder().addStep(1.0, 1.0, 200).addStep(0.0, 0.0, 200).addStep(1.0, 1.0, 200).build()
        robot.endGameRumble = Gamepad.RumbleEffect.Builder().addStep(1.0, 0.0, 200).addStep(0.0, 1.0, 200).addStep(1.0, 0.0, 200).addStep(0.0, 1.0, 200).addStep(1.0, 0.0, 200).build()

        //robot.motors = hashMapOf(
            //Motors.LeftFront to robot.hardwareMap.get(DcMotor::class.java, "LeftFront"),
            //Motors.RightFront to robot.hardwareMap.get(DcMotor::class.java, "RightFront"),
            //Motors.LeftBack to robot.hardwareMap.get(DcMotor::class.java, "LeftBack"),
            //Motors.RightBack to robot.hardwareMap.get(DcMotor::class.java, "RightBack")
        //)
        robot.telemetry.addLine("[ROBOT]: Initialized all Motors, Servos, and Sensors")

        robot.currentState = "Initialized";
    }
}