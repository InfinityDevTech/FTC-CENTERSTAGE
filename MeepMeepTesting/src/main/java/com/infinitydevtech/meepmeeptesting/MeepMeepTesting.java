package com.infinitydevtech.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.SampleMecanumDrive;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(1000);

        Pose2d start = new Pose2d(-38.0, 61.0, Math.toRadians(90.0));

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setStartPose(new Pose2d(-38.0, 61.0, Math.toRadians(90.0)))
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 17)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(start)
                                .lineTo(new Vector2d(-38.0, 47.0))
                                .turn(Math.toRadians(180.0))
                                .lineTo(new Vector2d(-34.5, 37.0))

                                .turn(Math.toRadians(-90.0))
                                .strafeLeft(8.0)
                                .forward(4.0)
                                .back(4.0)
                                 .lineTo(new Vector2d(-34.5, 48.0))

                                .lineTo(new Vector2d(-55.0, 48.0))
                                .lineTo(new Vector2d(-55.0, 12.0))
                                .lineTo(new Vector2d(57.0, 12.0))
                                .lineTo(new Vector2d(57.0, 25.0))
                        .build()
                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}