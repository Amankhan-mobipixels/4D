package com.example.opengllayerscode;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;


import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Renderer4D implements GLSurfaceView.Renderer {

    private final String vertexShader4D =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 aPosition;" +
                    "attribute vec2 aTextureCoord;" +
                    "varying vec2 vTextureCoord;" +
                    "varying vec2 mSensor;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * aPosition;" +
                    "  vTextureCoord = aTextureCoord;" +
                    "}";
    //  "  vec2 displacement = vec2(mSensor * ((mapColor.g - 0.5 ) * 0.09" +  change value * 0.09 with displacementFactor to achieve 4d runtime dynamically
    private final String fragmentShader4D =
            "precision highp float;" +
                    "varying vec2 vTextureCoord;" +
                    "uniform sampler2D sTexture;" +
                    "uniform sampler2D fTexture;" +
                    "uniform vec2 mSensor;" +
                    "uniform float displacementFactor;" +
                    "void main() {" +
                    "  vec4 mapColor = texture2D(fTexture, vTextureCoord);" +
                    "  vec2 displacement = vec2(mSensor * ((mapColor.g - 0.5 ) * displacementFactor" +
                    " ));" +
                    "  gl_FragColor = texture2D(sTexture, vTextureCoord+displacement);" +
                    "  if (gl_FragColor.a == 0.0) discard;" +
                    "}";

    private ShaderCompiler shaderCompiler;
    private GLTextureHelper glTextureHelper;
    private int[] textures;
    private int[] depthMapTextures;
    private Context context;
    private float depthIncrement = 0.2f;
    private MotionSensorHelper sensor;
    private float zoomSpeed = 0.001f; // Adjust the zoom speed
    private float currentZoom = 0.0f;
    private float maxIntensity = 0.0f;
    private boolean isZoomingIn = true;
    private float[] modelMatrix = new float[16];
    private float[] viewMatrix = new float[16];
    private float[] projectionMatrix = new float[16];
    private float[] mvpMatrix = new float[16];
    private SharedPreferences sharedPreferences;
    float aspectRatio;
    private List<Integer> drawableIds;
    private List<Integer> depthMapIds;

    public Renderer4D(Context context) {
        this.context = context;
        sensor = new MotionSensorHelper(context);
        sharedPreferences = context.getSharedPreferences("seekbarprefs", Context.MODE_PRIVATE);

    }

    public void setLists(List<Integer> drawableIds, List<Integer> depthMapIds) {
        this.drawableIds = drawableIds;
        this.depthMapIds = depthMapIds;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glEnable(3024);
        GLES20.glDisable(2929);
        GLES20.glEnable(3042);
        GLES20.glBlendFunc(1, 771);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glCullFace(GLES20.GL_BACK);
        GLES20.glFrontFace(GLES20.GL_CCW);
        generateLayers(drawableIds, depthMapIds);
        Log.d("sdfsafsdf", "onSurfaceCreated: ");
    }

    public void resetSurface() {
        // Clean up and reinitialize resources
        GLES20.glDeleteTextures(textures.length, textures, 0);
        GLES20.glDeleteTextures(depthMapTextures.length, depthMapTextures, 0);
    }


    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        Log.d("fgsdfasdf", "Renderer onSurfaceChanged 1");
        aspectRatio = (float) width / height;
        Log.d("fgsdfasdf", "Renderer onSurfaceChanged 2");

    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);


        int zoom=sharedPreferences.getInt("zoomspeed",2);
        int intensity=sharedPreferences.getInt("zoomintensity",5);
        if(zoom==1){
            zoomSpeed=0.001f;
        }else if (zoom==2){
            zoomSpeed=0.002f;
        }else if (zoom==3){
            zoomSpeed=0.003f;
        }else if (zoom==4){
            zoomSpeed=0.004f;
        }else if (zoom==5){
            zoomSpeed=0.005f;
        }else if (zoom==6){
            zoomSpeed=0.006f;
        }else if (zoom==7){
            zoomSpeed=0.007f;
        }else if (zoom==8){
            zoomSpeed=0.008f;
        }else if (zoom==9){
            zoomSpeed=0.009f;
        }else {
            zoomSpeed=0.01f;
        }

        if(intensity==1){
            maxIntensity=0.05f;
        }else if (intensity==2){
            maxIntensity=0.06f;
        }else if (intensity==3){
            maxIntensity=0.07f;
        }else if (intensity==4){
            maxIntensity=0.08f;
        }else if (intensity==5){
            maxIntensity=0.09f;
        }else if (intensity==6){
            maxIntensity=0.1f;
        }else if (intensity==7){
            maxIntensity=0.2f;
        }else if (intensity==8){
            maxIntensity=0.3f;
        }else if (intensity==9){
            maxIntensity=0.4f;
        }else {
            maxIntensity=0.5f;
        }

        if (isZoomingIn) {
            currentZoom += zoomSpeed;
            if (currentZoom >= maxIntensity) {
                currentZoom = maxIntensity;
                isZoomingIn = false;
            }
        } else {
            currentZoom -= zoomSpeed;
            if (currentZoom <= 0.0f) {
                currentZoom = 0.0f;
                isZoomingIn = true;
            }
        }


        double pitch = Math.atan2(-sensor.getX(), Math.sqrt(sensor.getY() * sensor.getY() + sensor.getZ() * sensor.getZ()));
        double roll = Math.atan2(sensor.getY(), Math.sqrt(sensor.getX() * sensor.getX() + sensor.getZ() * sensor.getZ()));
        float maxRotation = 10.0f; // Set the maximum rotation angle (in degrees)
        float clampedPitch = Math.max(-maxRotation, Math.min((float) pitch * 5, maxRotation));
        float clampedRoll = Math.max(-maxRotation, Math.min((float) roll * 10, maxRotation));

        Log.d("SfsdfsaDf", "pitch: " + clampedPitch);
        Log.d("SfsdfsaDf", "roll: " + clampedRoll);

        for (int i = 0; i < textures.length; i++) {
            float left = -aspectRatio;
            float right = aspectRatio;
            float bottom = -1.0f;
            float top = 1.0f;
            float near = -1.0f;
            float far = 1000.0f;
            Matrix.orthoM(projectionMatrix, 0, left, right, bottom, top, near, far);
            Matrix.setIdentityM(modelMatrix, 0);
            Matrix.translateM(modelMatrix, 0, 0, 0, -depthIncrement * i);
            Matrix.setLookAtM(viewMatrix, 0, 0, 0, -1, 0, 0, 0, 0, 1, 0);
            if (i == 0) {
                bottom = -0.8f;
                top = 0.8f;
                // Original code for rendering with texture and displacement
                Matrix.orthoM(projectionMatrix, 0, left, right, bottom, top, near, far);
                Matrix.rotateM(modelMatrix, 0, clampedPitch, 1.0f, 0.0f, 0.0f);
                Matrix.rotateM(modelMatrix, 0, clampedRoll, 0.0f, -1.0f, 0.0f);
                Matrix.scaleM(modelMatrix, 0, 1.0f + currentZoom, 1.0f + currentZoom, 1.0f + currentZoom);

            }
//            Matrix.rotateM(modelMatrix, 0, clampedPitch, 1.0f, 0.0f, 0.0f);
//            Matrix.rotateM(modelMatrix, 0, clampedRoll, 0.0f, -1.0f, 0.0f);
//            Matrix.scaleM(modelMatrix,0,1+0.3f,1+0.3f,1+0.3f);
            Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
            Matrix.multiplyMM(mvpMatrix, 0, mvpMatrix, 0, modelMatrix, 0);
            float disp = sharedPreferences.getInt("3deffect", 5);
            Log.d("fdsfdsf", "onDrawFrame: " + disp);
            shaderCompiler.draw4D(textures[i], depthMapTextures[i], mvpMatrix, (float) pitch, (float) roll, disp/100);
        }
    }

    public void generateLayers(List<Integer> drawableIds, List<Integer> depthMapIds) {
        //Initialize GLLayer and GLTextureHelper
        shaderCompiler = new ShaderCompiler();
        shaderCompiler.initializeProgram(vertexShader4D, fragmentShader4D);
        glTextureHelper = new GLTextureHelper();
        // Generate textures
        int layerCount = drawableIds.size(); // Set the number of layers
        textures = new int[layerCount];
        depthMapTextures = new int[layerCount];
        GLES20.glGenTextures(layerCount, textures, 0);
        GLES20.glGenTextures(layerCount, depthMapTextures, 0);
        // Generate layers using GLTextureHelper
        GLTextureHelper.generateLayers(textures, drawableIds, Color.RED, context);
        GLTextureHelper.generateLayers(depthMapTextures, depthMapIds, Color.WHITE, context);
    }


    public void start() {
        sensor.startListening();
    }

    public void stop() {
        sensor.stopListening();
    }


}