package com.infinitydevtech.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.SampleMecanumDrive;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(700);

        Pose2d start = new Pose2d(-38.0, -61.0, Math.toRadians(270.0));

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setStartPose(start)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 18)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(-38.0, -61.0, Math.toRadians(270.0)))
                                .lineTo(new Vector2d(-52.0, -50.0))
                    .lineTo(new Vector2d(-52.0, -25.0))
                .splineToSplineHeading(new Pose2d(38.0, -33.0, Math.toRadians(180.0)), Math.toRadians(-75.0))

                //.splineToSplineHeading(new Pose2d(38.0, 38.0, Math.toRadians(-180.0)), Math.toRadians(75.0))
                                //.splineToSplineHeading(new Pose2d(-34.0, 10.0, Math.toRadians(90.0)), Math.toRadians(90.0))
                        .build()
                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}