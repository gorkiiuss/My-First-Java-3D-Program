package com.github.gorkiiuss.myfirstjava3dprogram;

import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.SimpleUniverse;

import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.Vector3f;
import java.awt.*;

public class MyFirstJava3DProgram extends JPanel {
    private static final int DEFAULT_CUBE_PER_SIDE = 3;
    private static final float INITIAL_CAMERA_POS_X = 0f;
    private static final float INITIAL_CAMERA_POS_Y = 0f;
    private static final float INITIAL_CAMERA_POS_Z = 2.5f;
    private static final float INITIAL_CAMERA_ROT_X_DEGREES = -15;
    private static final float DEFAULT_CUBE_LENGTH = 0.6f;
    private static final float DEFAULT_OFFSET = 0.15f;
    private static final int DEFAULT_ROTATION_NUMBER = 5;
    private static final long DEFAULT_ROTATION_SPEED = 4000;


    public MyFirstJava3DProgram() {
        GraphicsConfiguration graphicsConfiguration = SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas3D = new Canvas3D(graphicsConfiguration);

        setLayout(new BorderLayout());
        add(canvas3D);

        SimpleUniverse universe = new SimpleUniverse(canvas3D);

        Vector3f viewPos = new Vector3f();
        viewPos.x = INITIAL_CAMERA_POS_X;
        viewPos.y = INITIAL_CAMERA_POS_Y;
        viewPos.z = INITIAL_CAMERA_POS_Z;

        Transform3D viewTransform = new Transform3D();
        viewTransform.setTranslation(viewPos);

        Transform3D viewRotation = new Transform3D();
        viewRotation.rotX(Math.toRadians(INITIAL_CAMERA_ROT_X_DEGREES));
        viewRotation.mul(viewTransform);

        universe.getViewingPlatform().getViewPlatformTransform().setTransform(viewRotation);
        universe.getViewingPlatform().getViewPlatformTransform().getTransform(viewTransform);

        BranchGroup sceneGraph = createSceneGraph();
        sceneGraph.compile();

        universe.addBranchGraph(sceneGraph);
    }

    private BranchGroup createSceneGraph() {
        BranchGroup root = new BranchGroup();

        TransformGroup fullCubeRotation = new TransformGroup();
        fullCubeRotation.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        float cubeSize = DEFAULT_CUBE_LENGTH / DEFAULT_CUBE_PER_SIDE;
        float offset = DEFAULT_OFFSET / DEFAULT_CUBE_PER_SIDE;
        ColorCube colorCube;
        Transform3D transform3D;
        TransformGroup cubePosition;
        float x, y, z;
        for (int i = 0; i < DEFAULT_CUBE_PER_SIDE; i++) {
            for (int j = 0; j < DEFAULT_CUBE_PER_SIDE; j++) {
                for (int k = 0; k < DEFAULT_CUBE_PER_SIDE; k++) {
                    colorCube = new ColorCube(cubeSize / 2);
                    transform3D = new Transform3D();
                    x = (i - (DEFAULT_CUBE_PER_SIDE - 1) / 2.0f) * cubeSize + offset * (i - (DEFAULT_CUBE_PER_SIDE - 1) / 2.0f);
                    y = (j - (DEFAULT_CUBE_PER_SIDE - 1) / 2.0f) * cubeSize + offset * (j - (DEFAULT_CUBE_PER_SIDE - 1) / 2.0f);
                    z = (k - (DEFAULT_CUBE_PER_SIDE - 1) / 2.0f) * cubeSize + offset * (k - (DEFAULT_CUBE_PER_SIDE - 1) / 2.0f);
                    transform3D.setTranslation(new Vector3f(x, y, z));
                    cubePosition = new TransformGroup(transform3D);
                    cubePosition.addChild(colorCube);
                    fullCubeRotation.addChild(cubePosition);
//                    System.out.println("New cube created in " + x + ", " + y + ", " + z);
                }
            }
        }

        Alpha alpha = new Alpha(DEFAULT_ROTATION_NUMBER, DEFAULT_ROTATION_SPEED);
        RotationInterpolator rotationInterpolator = new RotationInterpolator(alpha, fullCubeRotation);
        rotationInterpolator.setSchedulingBounds(new BoundingSphere());

        root.addChild(rotationInterpolator);
        root.addChild(fullCubeRotation);

        return root;
    }

    public static void main(String[] args) {
        System.setProperty("sun.awt.noerasebackground", "true");

        JFrame frame = new JFrame("My First Java 3D Program");
        MyFirstJava3DProgram myFirstJava3DProgram = new MyFirstJava3DProgram();

        frame.add(myFirstJava3DProgram);
        frame.setSize(700, 700);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
    }
}
