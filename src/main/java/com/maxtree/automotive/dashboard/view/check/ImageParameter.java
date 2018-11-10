package com.maxtree.automotive.dashboard.view.check;

public class ImageParameter {

	public double getScale() {
		return scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

	public double getRotate() {
		return rotate;
	}

	public void setRotate(double rotate) {
		this.rotate = rotate;
	}

	public double getTransparency() {
		return transparency;
	}

	public void setTransparency(double transparency) {
		this.transparency = transparency;
	}

	public double getBrightness() {
		return brightness;
	}

	public void setBrightness(double brightness) {
		this.brightness = brightness;
	}

	public double getContrast() {
		return contrast;
	}

	public void setContrast(double contrast) {
		this.contrast = contrast;
	}

	public void reset() {
		scale = 100;
		rotate = 0;
		transparency = 100;
		brightness = 130;
		contrast = 130;
	}

	private double scale = 100; // 默认值-缩放
	private double rotate = 0;//旋转角度
	private double transparency = 100.0d; // 透明度
	private double brightness = 130; //亮度
	private double contrast = 130; // 对比度
}
