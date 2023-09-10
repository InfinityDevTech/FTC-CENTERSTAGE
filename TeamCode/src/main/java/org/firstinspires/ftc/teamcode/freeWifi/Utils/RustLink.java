package org.firstinspires.ftc.teamcode.freeWifi.Utils;

import android.util.Log;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.jetbrains.annotations.Nullable;

/**
 * Few important notes:
 *   ALL BOOLEANS PASSED TO NATIVE CODE **MUST** BE PASSED AS A STRING, EITHER "TRUE" or "FALSE" (Case does not matter)
 */
public class RustLink {
    public String CurrentState = "Nothing";;

    private static native String foobarTest(String silent);
    private static native String JNISpeedTest(String CallTime);
    public static native void StartRustThreading(RustLink link);

    static {
        //IT IS CRUCIAL THAT THE FILE NAME MATCHES THE .so, OR THE ROBOT WILL CRASH!!!!!!!!!
        System.loadLibrary("rust_code");
    }

    private static LinearOpMode currentOp;

    public RustLink(LinearOpMode opMode) {
          currentOp = opMode;
    }

    public void init() {
        StartRustThreading(new RustLink(currentOp));
        currentOp.telemetry.addLine("[RUST]: Initialized");
        CurrentState = "Initialized";
    }

    public String JNIConvertToString(int message) {
        switch (message) {
            case 0:
                return "Initialized thread management";
            default:
                return "Unknown message";
        }
    }

    public void SendToTelemetry(int messageI) {
        String message = JNIConvertToString(messageI);
        Log.d("RUST", message);
        currentOp.telemetry.addLine("[RUST]: " + message);
    }




    public static long multiplyWithoutOptimization(long a, long b) {
        long result = 0;
        for (int i = 0; i < 32; i++) {
            result += (a * b) >> i;
        }
        return result;
    }
    public static void BlockingFooBarTest(Boolean silent) {
        //JAVA
        long JavaStartTime = System.nanoTime();
        long result = 1;
        for (int i = 0; i < 100000; i++) {
            result = multiplyWithoutOptimization(result, i);
        }
        long endTime = System.nanoTime();
        long javaTime = endTime - JavaStartTime;
        //RUST
        long rustTime = Long.parseLong(foobarTest(silent ? "true" : "false"));

        //COMPARE
        System.out.println("Java says rust finished at " + System.currentTimeMillis() + " ns");
        System.out.println("-------------------------------");
        System.out.println("JNI Speed - " + JNISpeedTest(String.valueOf(System.currentTimeMillis())) + "ms");
        System.out.println("Java Time - "+ javaTime + " Nano Seconds");
        System.out.println("Rust Time - "+ rustTime + " Nano Seconds");
        System.out.println("Exec Diff - "+(javaTime - rustTime));
        System.out.println("-------------------------------");
    }
}
