package com.example.opengllayerscode;

import android.opengl.GLES20;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

class ShaderCompiler {

    private final float[] vertices = {
// Front face
            -1.0f, -1.0f, 1.0f,  // Bottom left
            -1.0f, 1.0f, 1.0f,   // Top left
            1.0f, -1.0f, 1.0f,   // Bottom right
            1.0f, 1.0f, 1.0f,    // Top right

// Back face
            -1.0f, -1.0f, -1.0f, // Bottom left
            -1.0f, 1.0f, -1.0f,  // Top left
            1.0f, -1.0f, -1.0f,  // Bottom right
            1.0f, 1.0f, -1.0f,   // Top right

// Left face
            -1.0f, -1.0f, -1.0f, // Bottom front
            -1.0f, 1.0f, -1.0f,  // Top front
            -1.0f, -1.0f, 1.0f,  // Bottom back
            -1.0f, 1.0f, 1.0f,   // Top back

// Right face
            1.0f, -1.0f, -1.0f,  // Bottom front
            1.0f, 1.0f, -1.0f,   // Top front
            1.0f, -1.0f, 1.0f,   // Bottom back
            1.0f, 1.0f, 1.0f,    // Top back

// Top face
            -1.0f, 1.0f, 1.0f,   // Front left
            -1.0f, 1.0f, -1.0f,  // Back left
            1.0f, 1.0f, 1.0f,    // Front right
            1.0f, 1.0f, -1.0f,   // Back right

// Bottom face
            -1.0f, -1.0f, 1.0f,  // Front left
            -1.0f, -1.0f, -1.0f, // Back left
            1.0f, -1.0f, 1.0f,   // Front right
            1.0f, -1.0f, -1.0f,  // Back right
    };

    private final float[] textureVertices = {
            // Front face
            1, 1,
            1, 0,
            0, 1,
            0, 0,

            // Back face
            0, 1,
            0, 0,
            1, 1,
            1, 0,

            // Left face
            0, 1,
            0, 0,
            1, 1,
            1, 0,

            // Right face
            1, 1,
            1, 0,
            0, 1,
            0, 0,

            // Top face
            0, 1,
            1, 1,
            0, 0,
            1, 0,

            // Bottom face
            1, 0,
            0, 0,
            1, 1,
            0, 1,
    };



    private FloatBuffer verticesBuffer;
    private FloatBuffer textureBuffer;

    private int vertexShader;
    private int fragmentShader;

    private int program;

    private void initializeBuffers() {
        ByteBuffer buff = ByteBuffer.allocateDirect(vertices.length * 4);
        buff.order(ByteOrder.nativeOrder());
        verticesBuffer = buff.asFloatBuffer();
        verticesBuffer.put(vertices);
        verticesBuffer.position(0);

        buff = ByteBuffer.allocateDirect(textureVertices.length * 4);
        buff.order(ByteOrder.nativeOrder());
        textureBuffer = buff.asFloatBuffer();
        textureBuffer.put(textureVertices);
        textureBuffer.position(0);
    }

    public void initializeProgram(String vertexShaderCode, String fragmentShaderCode) {
        vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);

        GLES20.glLinkProgram(program);
    }

    private int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }

    void draw4D(int texture, int depthMapTexture, float[] mvpMatrix, float sensorX, float sesnorY, float displacementFactor) {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        GLES20.glUseProgram(program);

        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        int positionHandle = GLES20.glGetAttribLocation(program, "aPosition");
        int textureCoordHandle = GLES20.glGetAttribLocation(program, "aTextureCoord");
        int mvpMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix");
        int sTextureHandle = GLES20.glGetUniformLocation(program, "sTexture");
        int fTextureHandle = GLES20.glGetUniformLocation(program, "fTexture");
        int mSensorHandle = GLES20.glGetUniformLocation(program, "mSensor");
        int displacementHandle = GLES20.glGetUniformLocation(program, "displacementFactor");

        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 0, verticesBuffer);

        GLES20.glEnableVertexAttribArray(textureCoordHandle);
        GLES20.glVertexAttribPointer(textureCoordHandle, 2, GLES20.GL_FLOAT, false, 0, textureBuffer);

        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);
        GLES20.glUniform1i(sTextureHandle, 0);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, depthMapTexture);
        GLES20.glUniform1i(fTextureHandle, 1);


        GLES20.glUniform2f(mSensorHandle, sesnorY, sensorX);
        GLES20.glUniform1f(displacementHandle, displacementFactor);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(textureCoordHandle);
    }

    void draw3D(int texture, float[] mvpMatrix) {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        GLES20.glUseProgram(program);

        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        int positionHandle = GLES20.glGetAttribLocation(program, "aPosition");
        int textureHandle = GLES20.glGetUniformLocation(program, "uTexture");
        int texturePositionHandle = GLES20.glGetAttribLocation(program, "aTexPosition");
        int MVPMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix");

        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 12, verticesBuffer);

        GLES20.glEnableVertexAttribArray(texturePositionHandle);
        GLES20.glVertexAttribPointer(texturePositionHandle, 2, GLES20.GL_FLOAT, false, 8, textureBuffer);

        GLES20.glUniformMatrix4fv(MVPMatrixHandle, 1, false, mvpMatrix, 0);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);
        GLES20.glUniform1i(textureHandle, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(texturePositionHandle);
    }

    ShaderCompiler() {
        initializeBuffers();
    }
}
