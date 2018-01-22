package com.logicmaster63.tdgalaxy;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.logicmaster63.tdgalaxy.constants.Eye;

public class VRCamera implements Disposable {

    private FrameBuffer leftBuffer, rightBuffer;
    private TextureRegion leftTexture, rightTexture;
    private PerspectiveCamera leftCamera, rightCamera;
    private Batch batch;
    private int width, height, viewportWidth, viewportHeight;
    private Vector3 position, positionLeft, positionRight, direction, up, tmpVector3;
    private float yaw, pitch, roll, eyeDistance;

    public VRCamera(int width, int height, int viewportWidth, int viewportHeight) {
        leftCamera = new PerspectiveCamera(90, viewportWidth, viewportHeight);
        rightCamera = new PerspectiveCamera(90, viewportWidth, viewportHeight);
        leftBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, true);
        rightBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, true);
        leftTexture = new TextureRegion();
        rightTexture = new TextureRegion();
        batch = new SpriteBatch();
        this.width = width;
        this.height = height;
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
        tmpVector3 = new Vector3();
        position = new Vector3(250, 20, 250);
        positionLeft = new Vector3(249.5f, 20, 250);
        positionRight = new Vector3(250.5f, 20, 250);
        direction = new Vector3();
        up = new Vector3(0, 1, 0);
        eyeDistance = 0.5f;
    }

    public void update() {
        this.leftCamera.position.set(this.positionLeft);
        this.leftCamera.direction.set(this.direction);
        this.leftCamera.up.set(this.up);
        this.leftCamera.update();
        this.rightCamera.position.set(this.positionRight);
        this.rightCamera.direction.set(this.direction);
        this.rightCamera.up.set(this.up);
        this.rightCamera.update();
    }

    public void render() {
        leftTexture.setRegion(leftBuffer.getColorBufferTexture());
        leftTexture.flip(false, true);
        rightTexture.setRegion(rightBuffer.getColorBufferTexture());
        rightTexture.flip(false, true);

        batch.begin();
        batch.draw(leftTexture, 0, 0, width / 2f, height);
        batch.draw(rightTexture, width / 2f, 0, width / 2f, height);
        batch.end();
    }

    public Camera beginCamera(Eye eye) {
        if(Eye.LEFT.equals(eye)) {
            leftBuffer.begin();
            return leftCamera;
        } else {
            rightBuffer.begin();
            return rightCamera;
        }
    }

    public void endCamera(Eye eye) {
        if(Eye.LEFT.equals(eye))
            leftBuffer.end();
        else
            rightBuffer.end();
    }

    public void translate(Vector3 translate) {
        this.translate(translate.x, translate.y, translate.z);
    }

    public void translate(float x, float y, float z) {
        position.add(x, y, z);
        positionRight.add(x, y, z);
        positionRight.add(x, y, z);
    }

    public void setToTranslation(Vector3 translation) {
        tmpVector3.set(translation);
        tmpVector3.sub(position);
        translate(tmpVector3);
        tmpVector3.setZero();
    }

    public void rotate(float dyaw, float dpitch, float droll) {
        this.rotateRad((float)Math.toRadians((double)dyaw), (float)Math.toRadians((double)dpitch), (float)Math.toRadians((double)droll));
    }

    public void rotateRad(float dyaw, float dpitch, float droll) {
        this.setToRotationRad(this.yaw + dyaw, this.pitch + dpitch, this.roll + droll);
    }

    public void setToRotation(float yaw, float pitch, float roll) {
        this.setToRotationRad((float)Math.toRadians((double)yaw), (float)Math.toRadians((double)pitch), (float)Math.toRadians((double)roll));
    }

    public void setToRotationRad(float yaw, float pitch, float roll) {
        while(yaw < 0.0F) {
            yaw += 6.2831855F;
        }

        for(yaw %= 6.2831855F; pitch < 0.0F; pitch += 6.2831855F) {
            ;
        }

        for(pitch %= 6.2831855F; pitch < 0.0F; roll += 6.2831855F) {
            ;
        }

        roll %= 6.2831855F;
        this.yaw = yaw;
        this.pitch = pitch;
        this.roll = roll;
        this.direction.set(this.vectorFromAngles(this.yaw, this.pitch));
        this.tmpVector3 = this.vectorFromAngles(this.yaw + 1.5707964F, this.roll);
        this.up.set(this.direction);
        this.up.crs(this.tmpVector3);
        this.up.setLength(1.0F);
        this.tmpVector3.setLength(this.eyeDistance / 2.0F);
        this.positionLeft.set(this.position);
        this.positionRight.set(this.position);
        this.positionLeft.add(this.tmpVector3);
        this.positionRight.sub(this.tmpVector3);
    }

    private Vector3 vectorFromAngles(float yaw, float pitch) {
        this.tmpVector3.setZero();
        if ((double)pitch == 1.5707963267948966D) {
            this.tmpVector3.set(0.0F, 1.0F, 0.0F);
            return this.tmpVector3;
        } else {
            this.tmpVector3.x = (float)Math.cos((double)(-yaw));
            this.tmpVector3.z = (float)Math.sin((double)(-yaw));
            this.tmpVector3.y = (float)Math.tan((double)pitch) * this.pythagoras(this.tmpVector3.x, this.tmpVector3.z);
            if ((double)pitch > 1.5707963267948966D && (double)pitch < 4.71238898038469D) {
                this.tmpVector3.x = -this.tmpVector3.x;
                this.tmpVector3.z = -this.tmpVector3.z;
            }

            this.tmpVector3.setLength(1.0F);
            return this.tmpVector3;
        }
    }

    private float pythagoras(float a, float b) {
        return (float)Math.sqrt((double)(a * a + b * b));
    }

    @Override
    public void dispose() {
        leftBuffer.dispose();
        rightBuffer.dispose();
    }
}
