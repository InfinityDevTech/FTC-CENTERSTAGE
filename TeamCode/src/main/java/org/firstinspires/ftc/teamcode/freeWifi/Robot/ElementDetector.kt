package org.firstinspires.ftc.teamcode.freeWifi.Robot;

import com.acmerobotics.dashboard.FtcDashboard
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.Rect
import org.opencv.core.Scalar
import org.openftc.easyopencv.OpenCvCamera
import org.openftc.easyopencv.OpenCvCameraFactory
import org.openftc.easyopencv.OpenCvCameraRotation
import org.openftc.easyopencv.OpenCvPipeline
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

class ElementDetector(robot: Robot) : OpenCvPipeline() {
    var ELEMENT_COLOR: List<Int> = mutableListOf(255, 0, 0) //(red, green, blue)

    var toggleShow: Int = 1

    var original: Mat? = null

    var zone1: Mat? = null
    var zone2: Mat? = null

    var avgColor1: Scalar? = null
    var avgColor2: Scalar? = null

    var distance1: Double = 1.0
    var distance2: Double = 1.0

    var maxDistance: Double = 0.0

    init {
        val camera = OpenCvCameraFactory
            .getInstance()
            .createWebcam(
                robot.hardwareMap.get("Webcam 1") as WebcamName?,
                robot.hardwareMap.appContext.resources.getIdentifier(
                    "cameraMonitorViewId",
                    "id",
                    robot.hardwareMap.appContext.packageName
                )
            );

        class AsyncListener : OpenCvCamera.AsyncCameraOpenListener {

            override fun onOpened() {
                camera.startStreaming(1280, 720, OpenCvCameraRotation.UPRIGHT);
            }

            override fun onError(errorCode: Int) {
                // The async open operation has failed
            }
        }

        camera.setPipeline(this);
        camera.openCameraDeviceAsync(
            AsyncListener()
        )
        FtcDashboard.getInstance().startCameraStream(camera, 0.0)
    }

    override fun processFrame(input: Mat): Mat {
        //Creating duplicate of original frame with no edits

        val original = input.clone()

        //input = input.submat(new Rect(0));

        //Defining Zones
        //Rect(top left x, top left y, bottom right x, bottom right y)
        val zone2 = input.submat(Rect(450, 200, 300, 200))
        val zone1 = input.submat(Rect(1027, 170, 253, 230))

        //Averaging the colors in the zones
        avgColor1 = Core.mean(zone1)
        avgColor2 = Core.mean(zone2)

        //Putting averaged colors on zones (we can see on camera now)
        zone1.setTo(avgColor1)
        zone2.setTo(avgColor2)

        distance1 = color_distance(avgColor1, ELEMENT_COLOR)
        distance2 = color_distance(avgColor2, ELEMENT_COLOR)

        if ((distance1 > 195) && (distance2 > 190)) {
            color_zone = 3
            maxDistance = -1.0
        } else {
            maxDistance = min(distance1, distance2)

            color_zone = if (maxDistance == distance1) {
                1
            } else {
                2
            }
        }

        this.zone1 = zone1;
        this.zone2 = zone2;
        this.original = original;

        zone1.release();
        zone2.release();
        original.release();

        // Allowing for the showing of the averages on the stream
        return if (toggleShow == 1) {
            input
        } else {
            original
        }
    }

    fun color_distance(color1: Scalar?, color2: List<*>): Double {
        val r1 = color1!!.`val`[0]
        val g1 = color1.`val`[1]
        val b1 = color1.`val`[2]

        val r2 = color2[0] as Int
        val g2 = color2[1] as Int
        val b2 = color2[2] as Int

        return sqrt((r1 - r2).pow(2.0) + (g1 - g2).pow(2.0) + (b1 - b2).pow(2.0))
    }

    fun setAlliancePipe(alliance: String) {
        ELEMENT_COLOR = if (alliance == "red") {
            mutableListOf(255, 0, 0)
        } else {
            mutableListOf(0, 0, 255)
        }
    }

    fun get_element_zone(): Int {
        return color_zone
    }

    fun toggleAverageZonePipe() {
        toggleShow = toggleShow * -1
    }

    companion object {
        //Telemetry telemetry;
        var color_zone: Int = 1
    }
}