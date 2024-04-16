package org.firstinspires.ftc.teamcode.freeWifi.Robot;

import android.graphics.Bitmap
import android.graphics.Canvas
import com.acmerobotics.dashboard.FtcDashboard
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.robotcore.external.function.Consumer
import org.firstinspires.ftc.robotcore.external.function.Continuation
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration
import org.firstinspires.ftc.vision.VisionPortal
import org.firstinspires.ftc.vision.VisionProcessor
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.Rect
import org.opencv.core.Scalar
import org.openftc.easyopencv.OpenCvCamera
import org.openftc.easyopencv.OpenCvCameraFactory
import org.openftc.easyopencv.OpenCvCameraRotation
import org.openftc.easyopencv.OpenCvPipeline
import java.util.concurrent.atomic.AtomicReference
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt


class ElementDetector(robot: Robot) : OpenCvPipeline() {
    private var robot: Robot;
    private var telemetry: Telemetry;
    private var camera: OpenCvCamera? = null

    private var ELEMENT_COLOR: List<Int> = mutableListOf(255, 0, 0) //(red, green, blue)

    private var toggleShow: Int = 1

    private var original: Mat? = null

    private var zone1: Mat? = null
    private var zone2: Mat? = null

    private var avgColor1: Scalar? = null
    private var avgColor2: Scalar? = null

    private var distance1: Double = 1.0
    private var distance2: Double = 1.0

    private var maxDistance: Double = 0.0

    var aprilTagProcessor: AprilTagProcessor? = null
    var visionPortal: VisionPortal? = null

    init {
        this.robot = robot;
        this.telemetry = robot.telemetry;
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
        this.camera = camera;
        FtcDashboard.getInstance().startCameraStream(camera, 0.0)
    }

    override fun processFrame(input: Mat): Mat {
        //Creating duplicate of original frame with no edits

        val original = input.clone()

        //input = input.submat(new Rect(0));

        //Defining Zones
        //Rect(top left x, top left y, bottom right x, bottom right y)
        val zone2 = input.submat(Rect(550, 200, 300, 200))
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
            mutableListOf(255, 50, 0)
        } else {
            mutableListOf(0, 50, 255)
        }
    }

    fun get_element_zone(): Int {
        return color_zone
    }

    fun close(): Unit {
        zone1?.release()
        zone2?.release()
        original?.release()
        camera?.closeCameraDevice()
    }

    private fun CameraStreamSource(): CameraStreamSource {

        return TODO("Provide the return value")
    }

    fun start_april_pipe() {
        val process = CameraStreamGetter();
        this.aprilTagProcessor = AprilTagProcessor.Builder()
            .setDrawAxes(true)
            .setDrawTagID(true)
            .setDrawCubeProjection(true)
            .build()

        this.visionPortal = VisionPortal.Builder()
            .setCamera(robot.hardwareMap.get("Webcam 1") as WebcamName?)
            //.enableLiveView(true)
            //.setStreamFormat(VisionPortal.StreamFormat.MJPEG)
            .addProcessor(process)
            .addProcessor(aprilTagProcessor!!)
            .build()

        FtcDashboard.getInstance().startCameraStream(process, 30.0);
    }

    fun april_telemetry() {
        val currentDetections: List<AprilTagDetection> = this.aprilTagProcessor!!.getDetections()
        if (currentDetections.isEmpty()) {
            telemetry.addLine("No AprilTags Detected")
            return
        }
        telemetry.addLine("AprilTags Detected ${currentDetections.size}")

        // Step through the list of detections and display info for each one.
        for (detection in currentDetections) {
            if (detection.metadata != null) {
                telemetry.addLine("\n==== (ID ${detection.id}) ${detection.metadata.name}")
                telemetry.addLine(
                    "XYZ ${detection.ftcPose.x.roundToInt()} ${detection.ftcPose.y.roundToInt()} ${detection.ftcPose.z.roundToInt()} (inch)"
                )
                telemetry.addLine(
                    "PRY ${detection.ftcPose.pitch.roundToInt()} ${detection.ftcPose.roll.roundToInt()} ${detection.ftcPose.yaw.roundToInt()} (deg)"
                )
            } else {
                telemetry.addLine("\n==== (ID ${detection.id})")
                telemetry.addLine(
                    "Center ${detection.center.x} ${detection.center.y}"
                )
            }
        } // end for() loop


        // Add "key" information to telemetry
        telemetry.addLine("\nkey:\nXYZ = X (Right), Y (Forward), Z (Up) dist.")
        telemetry.addLine("PRY = Pitch, Roll & Yaw (XYZ Rotation)")
        telemetry.addLine("RBE = Range, Bearing & Elevation")
    }


    companion object {
        var color_zone: Int = 1
    }
}

public class CameraStreamGetter : VisionProcessor, CameraStreamSource {

    private var lastFrame: AtomicReference<Bitmap> = AtomicReference<Bitmap>(Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565))
    override fun init(p0: Int, p1: Int, p2: CameraCalibration?) {
        lastFrame.set(Bitmap.createBitmap(p0, p1, Bitmap.Config.RGB_565))
    }

    override fun processFrame(p0: Mat?, p1: Long): Any? {
        var b = Bitmap.createBitmap(p0!!.width(), p0!!.height(), Bitmap.Config.RGB_565);
        Utils.matToBitmap(p0, b);
        lastFrame.set(b)
        return null;
    }

    override fun onDrawFrame(p0: Canvas?, p1: Int, p2: Int, p3: Float, p4: Float, p5: Any?) {
        // nothing
    }

    override fun getFrameBitmap(continuation: Continuation<out Consumer<Bitmap?>?>) {
        continuation.dispatch { bitmapConsumer: Consumer<Bitmap?>? ->
            bitmapConsumer!!.accept(
                lastFrame.get()
            )
        }
    }
}