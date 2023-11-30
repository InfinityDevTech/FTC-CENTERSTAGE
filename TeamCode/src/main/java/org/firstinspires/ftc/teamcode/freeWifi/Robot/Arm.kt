package org.firstinspires.ftc.teamcode.freeWifi.Robot

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareDevice
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.freeWifi.Constants
import kotlin.reflect.KProperty1

public data class ArmPos(val button: String, val mid_pos: Int, val low_pos: Int, val name: String);
class Arm(var robot: Robot) : IMovementComposable {
    private var mid_pos = 0;
    private var low_pos = 0;
    private var claw_rot_pos = 0.00;
    private var claw_grip_pos = 0.00;

    private var claw_close_pos = 0.8;
    private var claw_open_pos = 1.0;

    // Its very iffy. I am going to make a calibration mechanic for worlds.
    private var set_positions: Array<ArmPos> = arrayOf(
        ArmPos("y", -6000, 60, "Place"),
        ArmPos("a", 0, 0, "Grab")
    );

    private var arm_left: DcMotor = robot.motors[Motors.ArmLeft]!!;
    private var arm_right: DcMotor = robot.motors[Motors.ArmRight]!!;
    private var arm_mid: DcMotor = robot.motors[Motors.ArmMid]!!;
    private var claw_rot: Servo = robot.servos[Servos.ClawRot]!!;
    private var claw_grip: Servo = robot.servos[Servos.ClawGrip]!!;

    //private var claw_rotation: Servo;
    //private var claw_grip: Servo;

    init {
        // This sucks to do, but it works.
        //this.claw_rotation = robot.hardwareMap.get(Servo::class.java, "claw_rot")
        //this.claw_grip = robot.hardwareMap.get(Servo::class.java, "claw_grip");
        arm_mid.targetPosition = mid_pos
        arm_left.targetPosition = -low_pos
        arm_right.targetPosition = low_pos
        robot.setMotorsMode(DcMotor.RunMode.RUN_TO_POSITION, Motors.ArmLeft, Motors.ArmRight, Motors.ArmMid);
        robot.setMotorsPower(1.0, Motors.ArmLeft, Motors.ArmRight, Motors.ArmMid);
    }

    fun run_logs() {
        robot.telemetry.addLine("[ARM]: Running arm logs")
        robot.telemetry.addLine("[DEBUG] Dont forget, left + right = low, mid is mid")
        robot.telemetry.addLine("Left: " + arm_left.currentPosition.toString())
        robot.telemetry.addLine("Right: " + arm_right.currentPosition.toString())
        robot.telemetry.addLine("Mid: " + arm_mid.currentPosition.toString())
        robot.telemetry.addLine("Claw Rot: " + claw_rot_pos.toString());
        robot.telemetry.addLine("Claw Grip: " + claw_grip_pos.toString());
        //robot.telemetry.addLine("Claw Control: " + claw_rotation.position.toString())
        robot.telemetry.addLine("     ----- END ARM LOGS -----")
    }

    private fun check_set_positions() {
        // goes through each set position and checks if the button is pressed.
        // It's in the order of the array, if they are all pressed, its the first
        // one that gets executed.
        for (setpos in set_positions) {
            // Hack to read property using a string.
            if (readInstanceProperty(robot.opMode.gamepad2, setpos.button) as Boolean) {
                set_pos(setpos);
                return;
            }
        }
    }

    private fun set_pos(pos: ArmPos) {
        mid_pos = pos.mid_pos
        low_pos = pos.low_pos
    }

    override fun run_movement() {
        // Sets the current position each execution frame, so that it can be changed on the fly.
        arm_mid.targetPosition = mid_pos
        arm_left.targetPosition = -low_pos
        arm_right.targetPosition = low_pos
        claw_rot.position = claw_rot_pos
        claw_grip.position = claw_grip_pos;

        // Manual controls.
        if (robot.opMode.gamepad2.right_bumper) {low_pos += 30}
        if (robot.opMode.gamepad2.left_bumper) {low_pos -= 30}
        if (robot.opMode.gamepad2.dpad_up) {mid_pos -= 30}
        if (robot.opMode.gamepad2.dpad_down) {mid_pos += 30}
        if (robot.opMode.gamepad2.dpad_left) {claw_rot_pos -= 0.01}
        if (robot.opMode.gamepad2.dpad_right) {claw_rot_pos += 0.01}
        if (robot.opMode.gamepad2.x) {claw_grip_pos = claw_open_pos}
        if (robot.opMode.gamepad2.b) {claw_grip_pos = claw_close_pos}

        claw_rot_pos = claw_rot_pos.coerceIn(0.0, 1.0);
        claw_grip_pos = claw_grip_pos.coerceIn(0.0, 1.0);

        // Cap the claw rotation.


        check_set_positions();
    }

    // Reflection magic, reads a property using a stream, its pretty cool.
    @Suppress("UNCHECKED_CAST")
    fun <R> readInstanceProperty(instance: Any, propertyName: String): R {
        val property = instance::class.members
            // don't cast here to <Any, R>, it would succeed silently
            .first { it.name == propertyName } as KProperty1<Any, *>
        // force a invalid cast exception if incorrect type here
        return property.get(instance) as R
    }
}