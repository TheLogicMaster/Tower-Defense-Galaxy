package com.logicmaster63.tdgalaxy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

public class TempVRInputAdaptor {
    private VRCamera cam;
    private float yaw;
    private float pitch;
    private float roll;
    private float dYaw;
    private float dPitch;
    private float dRoll;
    private float mYaw;
    private float mPitch;
    private float mRoll;
    private float calibYaw;
    private float calibPitch;
    private float calibRoll;
    private float factorYaw;
    private float factorPitch;
    private float factorRoll;
    private float saveYaw;
    private float savePitch;
    private float saveRoll;
    private Interpolator yawInterpolator;
    private Interpolator pitchInterpolator;
    private Interpolator rollInterpolator;
    private int interpolatorSize;
    private boolean logging;

    public TempVRInputAdaptor(VRCamera cam) {
        this.cam = cam;
        this.yaw = 0.0F;
        this.pitch = 0.0F;
        this.roll = 0.0F;
        this.dYaw = 0.0F;
        this.dPitch = 0.0F;
        this.dRoll = 0.0F;
        this.calibYaw = 0.0F;
        this.calibPitch = 0.0F;
        this.calibRoll = 0.0F;
        this.factorYaw = 1.0F;
        this.factorPitch = 1.0F;
        this.factorRoll = 1.0F;
        this.saveYaw = this.yaw;
        this.savePitch = this.pitch;
        this.saveRoll = this.roll;
        this.yawInterpolator = new Interpolator(4);
        this.pitchInterpolator = new Interpolator(4);
        this.rollInterpolator = new Interpolator(4);
        this.logging = false;
    }

    public void update(float delta) {
        if (Float.isNaN(this.yaw)) {
            this.yaw = this.saveYaw;
        } else {
            this.saveYaw = this.yaw;
        }

        if (Float.isNaN(this.pitch)) {
            this.pitch = this.savePitch;
        } else {
            this.savePitch = this.pitch;
        }

        if (Float.isNaN(this.roll)) {
            this.roll = this.saveRoll;
        } else {
            this.saveRoll = this.roll;
        }

        this.mYaw += Gdx.input.getGyroscopeX() * delta % 6.2831855F;
        this.mPitch = Gdx.input.getAccelerometerZ() / this.getTotalAcceleration();
        if (this.mPitch > 1.0F) {
            this.mPitch = 1.0F;
        } else if (this.mPitch < -1.0F) {
            this.mPitch = -1.0F;
        }

        this.mPitch = (float)Math.asin((double)this.mPitch) - this.pitch;
        this.mRoll = Gdx.input.getAccelerometerY() / this.getTotalAcceleration();
        if (this.mRoll > 1.0F) {
            this.mRoll = 1.0F;
        } else if (this.mRoll < -1.0F) {
            this.mRoll = -1.0F;
        }

        this.mRoll = (float)Math.asin((double)this.mRoll) - this.roll;
        if (Float.isNaN(this.mYaw)) {
            this.mYaw = 0.0F;
        }

        if (Float.isNaN(this.mPitch)) {
            this.mPitch = 0.0F;
        }

        if (Float.isNaN(this.mRoll)) {
            this.mRoll = 0.0F;
        }

        this.dYaw = this.yawInterpolator.calculate(this.mYaw - this.yaw, delta);
        this.dPitch = this.pitchInterpolator.calculate((this.mPitch + Gdx.input.getGyroscopeY() * delta) / 2.0F, delta);
        this.dRoll = this.rollInterpolator.calculate((this.mRoll + Gdx.input.getGyroscopeZ() * delta) / 2.0F, delta);
        if (Float.isNaN(this.dYaw)) {
            this.dYaw = 0.0F;
        }

        if (Float.isNaN(this.dPitch)) {
            this.dPitch = 0.0F;
        }

        if (Float.isNaN(this.dRoll)) {
            this.dRoll = 0.0F;
        }

        this.yaw += this.dYaw;
        this.pitch += this.dPitch;
        this.roll += this.dRoll;
        this.cam.rotateRad(-this.factorYaw * this.dYaw, -this.factorPitch * this.dPitch, this.factorRoll * this.dRoll);
        if (this.logging) {
            Gdx.app.log("Rotation", "Yaw: " + Math.toDegrees((double)this.yaw) + " Pitch: " + Math.toDegrees((double)this.pitch) + " Roll: " + Math.toDegrees((double)this.roll));
            Gdx.app.log("Delta", "dYaw: " + this.dYaw + " dPitch: " + this.dPitch + " dRoll: " + this.dRoll);
        }

    }

    public void calibrate() {
        this.calibYaw = this.yaw;
        this.calibPitch = (float)Math.asin((double)(Gdx.input.getAccelerometerZ() / 9.81F));
        this.calibRoll = (float)Math.asin((double)(Gdx.input.getAccelerometerY() / 9.81F));
        this.cam.rotateRad(this.calibYaw, this.calibPitch, this.calibRoll);
    }

    private float getTotalAcceleration() {
        return (float)Math.sqrt((double)(Gdx.input.getAccelerometerX() * Gdx.input.getAccelerometerX() + Gdx.input.getAccelerometerY() * Gdx.input.getAccelerometerY() + Gdx.input.getAccelerometerZ() * Gdx.input.getAccelerometerZ()));
    }

    public float getCalibYaw() {
        return this.calibYaw;
    }

    public void setCalibYaw(float calibYaw) {
        this.calibYaw = calibYaw;
        this.cam.rotate(calibYaw, 0.0F, 0.0F);
    }

    public float getCalibPitch() {
        return this.calibPitch;
    }

    public void setCalibPitch(float calibPitch) {
        this.calibPitch = calibPitch;
        this.cam.rotate(0.0F, calibPitch, 0.0F);
    }

    public float getCalibRoll() {
        return this.calibRoll;
    }

    public void setCalibRoll(float calibRoll) {
        this.calibRoll = calibRoll;
        this.cam.rotate(0.0F, 0.0F, calibRoll);
    }

    public float getFactorYaw() {
        return this.factorYaw;
    }

    public void setFactorYaw(float factorYaw) {
        this.factorYaw = factorYaw;
    }

    public float getFactorPitch() {
        return this.factorPitch;
    }

    public void setFactorPitch(float factorPitch) {
        this.factorPitch = factorPitch;
    }

    public float getFactorRoll() {
        return this.factorRoll;
    }

    public void setFactorRoll(float factorRoll) {
        this.factorRoll = factorRoll;
    }

    public boolean isLogging() {
        return this.logging;
    }

    public void setLogging(boolean logging) {
        this.logging = logging;
    }

    public void setInterpolatorSize(int size) {
        if (size < 1) {
            size = 1;
        }

        this.interpolatorSize = size;
        this.yawInterpolator = new Interpolator(size);
        this.pitchInterpolator = new Interpolator(size);
        this.rollInterpolator = new Interpolator(size);
    }

    public int getInterpolatorSize() {
        return this.interpolatorSize;
    }

    private class Interpolator {
        private Array<Float> lastValues;
        private int size;
        private float tmp;

        public Interpolator(int size) {
            this.size = size;
            this.lastValues = new Array(size);
        }

        public float calculate(float value, float delta) {
            value /= delta;
            if (this.lastValues.size == this.size) {
                this.lastValues.removeIndex(this.size - 1);
            }

            this.lastValues.add(value);
            this.tmp = 0.0F;

            for(int i = 0; i < this.lastValues.size; ++i) {
                this.tmp += (Float)this.lastValues.get(i);
            }

            this.tmp /= (float)this.lastValues.size;
            value = (value + this.tmp) / 2.0F;
            return value * delta;
        }
    }
}
