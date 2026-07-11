package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .setDimensions(18, 17.5)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(-55.8 , 43.1, Math.toRadians(135)))
                .strafeToSplineHeading(new Vector2d(-30,30), Math.toRadians(140))
                .strafeToSplineHeading(new Vector2d(-12,12), Math.toRadians(90))
                .strafeToSplineHeading(new Vector2d(-12,55), Math.toRadians(90))
                .strafeToSplineHeading(new Vector2d(-30,30), Math.toRadians(140))
                .strafeToConstantHeading(new Vector2d(-12,45))

                .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
