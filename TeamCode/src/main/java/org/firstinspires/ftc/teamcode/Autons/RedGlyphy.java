package org.firstinspires.ftc.teamcode.Autons;

import android.util.Log;

import com.qualcomm.hardware.lynx.LynxVoltageSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.Subsystems.DriveTrain;
import org.firstinspires.ftc.teamcode.Subsystems.Robot;
import org.firstinspires.ftc.teamcode.Util.Gyro;
import org.firstinspires.ftc.teamcode.Util.VisionUtil;
import org.firstinspires.ftc.teamcode.Util.*;

/**
 * Created by Sumanth Kondapalli on 11/16/2017.
 */
@Autonomous(name = "RedGlyphy")
public class RedGlyphy extends LinearOpMode {

    Robot robot = new Robot();
    Gyro gyro = new Gyro();
    ElapsedTime timer = new ElapsedTime();
    RobotConstants constant = new RobotConstants();
    VisionUtil vision = new VisionUtil(this);
    boolean hasBlock = false;
    RelicRecoveryVuMark reading;
    VisionUtil visionUtil = new VisionUtil(this);

    double firstAnlge;

    @Override
    public void runOpMode() throws InterruptedException {

        gyro.initGyro(hardwareMap);
        robot.init(hardwareMap, this, gyro);
        reading = vision.readGraph(hardwareMap);

        double heading;

        int dist = 24;
        reading = vision.readGraph2(hardwareMap);
        telemetry.addData("vision", reading);
        telemetry.update();
        waitForStart();
        if (opModeIsActive()){
            firstAnlge = gyro.getYaw();
            robot.jewelArm.armDown();

            if (visionUtil.isDetected()){
                reading = vision.readGraph2(hardwareMap);
            }

        sleep(500);

            if(String.valueOf(robot.jewelArm.getColor()) == "BLUE"){
                telemetry.addData(String.valueOf(robot.jewelArm.getColor()),"00");
                telemetry.update();

                robot.driveTrain.setMoveDist(4);
                dist-=4;

            }


            else if(String.valueOf(robot.jewelArm.getColor()) == "RED"){

                telemetry.addData(String.valueOf(robot.jewelArm.getColor()),"00");
                telemetry.update();

                robot.driveTrain.setMoveDist(-4);
                dist+=4;

            }
//
            robot.jewelArm.armMid();
            robot.jewelArm.arm2Mid();





            robot.driveTrain.setMoveDist(dist);
//            robot.bar4.setMoveAngle(265.9);
            robot.driveTrain.singleSideRotateDeg(DriveTrain.Side.RIGHT_SIDE, gyro.getYaw());



            switch (reading){
                case RIGHT:{
                    robot.driveTrain.setMoveDist(3.8);
                    robot.driveTrain.singleSideRotateDeg(DriveTrain.Side.LEFT_SIDE,gyro.getYaw()-360);
                    robot.driveTrain.rotateDeg(90);
                    robot.driveTrain.setMoveDist(-7);
                    robot.bar4.setMoveAngle(145);

                    correctAt90();


                    placeBlock();
                    break;
                }
                case CENTER:{

                    robot.driveTrain.setMoveDist(10);
                    robot.driveTrain.singleSideRotateDeg(DriveTrain.Side.LEFT_SIDE,gyro.getYaw()-360);

                    robot.driveTrain.rotateDeg(90);
                    robot.driveTrain.setMoveDist(-7);

                    robot.bar4.setMoveAngle(145);
                    correctAt90();
                    placeBlock();

                    break;
                }
                case LEFT:{


                    robot.driveTrain.setMoveDist(20);
                    robot.driveTrain.singleSideRotateDeg(DriveTrain.Side.LEFT_SIDE,gyro.getYaw()-360);

                    robot.driveTrain.rotateDeg(90);
                    robot.driveTrain.setMoveDist(-7);

                    robot.bar4.setMoveAngle(145);
                    correctAt90();
                    placeBlock();

                    break;
                }
                default:{
                    robot.driveTrain.rotateDeg(90);
                    placeBlock();


                    break;

                }
            }
            robot.driveTrain.setMoveDist(-3);

            heading = gyro.getYaw();
            telemetry.addData("heading",heading);
            Log.i("heading1",String.valueOf(heading));
            telemetry.update();

            robot.driveTrain.rotateDeg(180);

            timer.startTime();
            int startLeftEnc = robot.driveTrain.getLeftCurrentPosition();
            while(!robot.range.isGlyph() && opModeIsActive() && Math.abs(robot.driveTrain.getLeftCurrentPosition() - startLeftEnc) < (constant.getTICKS_PER_INCH() * 62.5)){

                if (timer.milliseconds() <= 1000) {

                    robot.wheels.intakeLeft();
                    robot.wheels.intakeRight();

                }
                else{

                    robot.wheels.intakeRight();
                    robot.wheels.setLeftWheelPwr(-0.4);
                }
                robot.driveTrain.setLeftPower(0.2);
                robot.driveTrain.setRightPower(0.2);

                Log.i("dist",robot.range.getDist()+"");
            }

            robot.driveTrain.setLeftPower(0);
            robot.driveTrain.setRightPower(0);
            robot.wheels.setLeftWheelPwr(0);
            robot.wheels.setRightWheels(0);


            int leftTarget = robot.driveTrain.getLeftCurrentPosition() - startLeftEnc;

            robot.driveTrain.setMoveDist(-10.5);
            robot.driveTrain.rotateDeg(180);

            robot.bar4.setMoveAngle(167);

            correctAt90();

            robot.driveTrain.setMoveDistEnc(-(leftTarget- (10 * constant.getTICKS_PER_INCH())));

            correctAt90();
            robot.driveTrain.setLeftPower(0);
            robot.driveTrain.setRightPower(0);
            robot.wheels.setLeftWheelPwr(0);
            robot.wheels.setRightWheels(0);


            robot.bar4.setMoveAngle(167);


            placeBlock();
            while(robot.bar4.isLowerHit())
                robot.bar4.setPower(-1);
            robot.driveTrain.setMoveDist(7);
            robot.driveTrain.setMoveDist(-2);

        }

    }

    public void placeBlock(){
        sleep(200);
        while(robot.range.getDist()<10 && opModeIsActive()) {
            robot.wheels.setRightWheels(1);
            robot.wheels.setLeftWheelPwr(1);
        }

        robot.wheels.setRightWheels(1);
        robot.wheels.setLeftWheelPwr(1);
        sleep(1000);

        robot.wheels.stopLeft();
        robot.wheels.stopRight();

    }

    public void correctAt90(){
        if (gyro.getYaw() > 90){
            robot.driveTrain.singleSideRotateDegCorrect(DriveTrain.Side.LEFT_SIDE,gyro.getYaw()-90);

        } else if (gyro.getYaw() < 90){
            robot.driveTrain.singleSideRotateDegCorrect(DriveTrain.Side.RIGHT_SIDE,gyro.getYaw()-90);

        }
    }


}
