package org.firstinspires.ftc.teamcode.freeWifi.Autonomous

import com.qualcomm.hardware.bosch.BNO055IMU
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode

@Autonomous(group = "Right")
class RightAuto : LinearOpMode() {
    override fun runOpMode() {
        telemetry.addLine("AUTO HAS NOT BEEN IMPLEMENTED YET");

        val imu = hardwareMap.get<BNO055IMU>(BNO055IMU::class.java, "imu")
        val parameters = BNO055IMU.Parameters()
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS
        imu.initialize(parameters)

        telemetry.update()
        waitForStart();
        while (opModeIsActive()) {
            telemetry.addLine("AUTO HAS NOT BEEN IMPLEMENTED YET");
            telemetry.update()
        }
    }
}