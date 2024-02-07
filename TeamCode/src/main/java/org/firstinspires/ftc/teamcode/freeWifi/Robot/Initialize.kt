package org.firstinspires.ftc.teamcode.freeWifi.Robot

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo

class Initialize(private val robot: Robot) {
    fun init() {
        robot.halfTimeRumble = Gamepad.RumbleEffect.Builder().addStep(1.0, 1.0, 200).addStep(0.0, 0.0, 200).addStep(1.0, 1.0, 200).build()
        robot.endGameRumble = Gamepad.RumbleEffect.Builder().addStep(1.0, 0.0, 200).addStep(0.0, 1.0, 200).addStep(1.0, 0.0, 200).addStep(0.0, 1.0, 200).addStep(1.0, 0.0, 200).build()

        robot.motors = hashMapOf(
            Motors.LeftFront to robot.hardwareMap.get(DcMotor::class.java, "front_left"),
            Motors.RightFront to robot.hardwareMap.get(DcMotor::class.java, "front_right"),
            Motors.LeftBack to robot.hardwareMap.get(DcMotor::class.java, "back_left"),
            Motors.RightBack to robot.hardwareMap.get(DcMotor::class.java, "back_right"),
            Motors.ArmLeft to robot.hardwareMap.get(DcMotor::class.java, "arm_left"),
            Motors.ArmRight to robot.hardwareMap.get(DcMotor::class.java, "arm_right"),
            Motors.ArmMid to robot.hardwareMap.get(DcMotor::class.java, "arm_mid")
        )

        robot.servos = hashMapOf(
            Servos.ClawRot to robot.hardwareMap.get(Servo::class.java, "claw_rot"),
            Servos.ClawGrip to robot.hardwareMap.get(Servo::class.java, "claw_grip"),
            Servos.PewPew to robot.hardwareMap.get(Servo::class.java, "pewpewpewpew")
        );

        robot.servos[Servos.ClawRot]?.direction = Servo.Direction.REVERSE;

        robot.telemetry.addLine("[ROBOT]: Initialized all Motors, Servos, and Sensors")

        robot.currentState = "Initialized";
    }
}