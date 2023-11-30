package org.firstinspires.ftc.teamcode.freeWifi

import com.acmerobotics.dashboard.config.Config

@Config
object Constants {
    @JvmField var Debug: Boolean = true;

    // When you push button to change speed, how much to modify speed by per tick.
    @JvmField var speed_modifier: Float = 0.05F;
    @JvmField var mid_target: Int = -1000;
    @JvmField var low_target: Int = 1500;
}