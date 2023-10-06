package org.firstinspires.ftc.teamcode.freeWifi.Robot

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.Gamepad.RumbleEffect
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.robotcore.external.Telemetry

enum class Motors {
    LeftFront,
    RightFront,
    LeftBack,
    RightBack
}



enum class Servos {}

enum class Sensors {}

class Robot(val opMode: OpMode) {
    var currentState = "Nothing"
    private val init = Initialize(this)

    lateinit var motors: HashMap<Motors, DcMotor>
    lateinit var servos: HashMap<Servos, Servo>
    lateinit var sensors: HashMap<Sensors, Any>

    lateinit var halfTimeRumble: RumbleEffect;
    lateinit var endGameRumble: RumbleEffect;

    val telemetry: Telemetry = opMode.telemetry;

    val hardwareMap: HardwareMap get() = opMode.hardwareMap
    val isActive get() = (opMode as LinearOpMode).opModeIsActive()

    fun init() {
      init.init(); // Populate fields in this class.

        this.motors[Motors.RightFront]?.direction = DcMotorSimple.Direction.REVERSE;
      telemetry.addLine("[ROBOT]: Initialized bot")
    }

    fun setMotorMode(motor: Motors, mode: DcMotor.RunMode) {
        this.motors[motor]?.mode = mode;
    }

    fun setMotorPower(motor: Motors, power: Double) {
        this.motors[motor]?.power = power;
    }

    fun setMotorsMode(mode: DcMotor.RunMode, vararg motors: Motors) {
        for (motor in motors) {
            setMotorMode(motor, mode)
        }
    }
}