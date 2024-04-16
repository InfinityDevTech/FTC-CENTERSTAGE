package org.firstinspires.ftc.teamcode.freeWifi.DriveModes

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotorSimple

@TeleOp(name = "MSMHS - Tank Drive", group = "TeleOp")
class MSMHSTankDrive : LinearOpMode() {
    override fun runOpMode() {
        val leftMotor = hardwareMap.dcMotor.get("leftMotor")
        val rightMotor = hardwareMap.dcMotor.get("rightMotor")
        val armMotor = hardwareMap.dcMotor.get("armMotor")

        rightMotor.direction = DcMotorSimple.Direction.REVERSE;

        waitForStart()

        while (opModeIsActive()) {
            val leftPower = -gamepad1.left_stick_y.toDouble()
            val rightPower = -gamepad1.right_stick_y.toDouble()

            leftMotor.power = leftPower
            rightMotor.power = rightPower

            if (gamepad1.left_bumper) {
                armMotor.power = 0.5
            } else if (gamepad1.right_bumper) {
                armMotor.power = -0.5
            } else {
                armMotor.power = 0.0
            }
        }
    }
};