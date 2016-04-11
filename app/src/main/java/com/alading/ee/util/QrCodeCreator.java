package com.alading.ee.util;

import java.util.EnumMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class QrCodeCreator {

	public static Bitmap createQrCode(String content, int qrWidth,
			int qrheight, Bitmap logo, int logoWidth) throws WriterException {
		// 图片宽度的一半
		int IMAGE_HALFWIDTH = logoWidth / 2;
		// 构造需要插入的图片对象
		Bitmap mBitmap = Bitmap.createBitmap(logo);
		// 缩放图片
		Matrix m = new Matrix();
		float sx = (float) 2 * IMAGE_HALFWIDTH / mBitmap.getWidth();
		float sy = (float) 2 * IMAGE_HALFWIDTH / mBitmap.getHeight();
		m.setScale(sx, sy);
		// 重新构造一个40*40的图片
		mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(),
				mBitmap.getHeight(), m, false);

		// 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
		BitMatrix matrix = new MultiFormatWriter().encode(content,
				BarcodeFormat.QR_CODE, qrWidth, qrheight);// 如果要指定二维码的边框以及容错率，最好给encode方法增加一个参数：hints
		// 一个Hashmap
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		// 二维矩阵转为一维像素数组,也就是一直横着排了
		int halfW = width / 2;
		int halfH = height / 2;
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (x > halfW - IMAGE_HALFWIDTH && x < halfW + IMAGE_HALFWIDTH
						&& y > halfH - IMAGE_HALFWIDTH
						&& y < halfH + IMAGE_HALFWIDTH) {
					pixels[y * width + x] = mBitmap.getPixel(x - halfW
							+ IMAGE_HALFWIDTH, y - halfH + IMAGE_HALFWIDTH);
				} else {
					// 此处可以修改二维码的颜色，可以分别制定二维码和背景的颜色；
					pixels[y * width + x] = matrix.get(x, y) ? 0xff000000
							: 0xfffffff;
				}

			}
		}
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		// 通过像素数组生成bitmap
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

		return bitmap;
	}

	public static Bitmap createQrCode(String content, int qrWidth, int qrheight)
			throws WriterException {
		// 生成二维码,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
		BitMatrix matrix = new MultiFormatWriter().encode(content,
				BarcodeFormat.QR_CODE, qrWidth, qrheight);
		/*
		 * Map<EncodeHintType, Object> hints = new EnumMap<EncodeHintType,
		 * Object>( EncodeHintType.class);
		 * hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		 * hints.put(EncodeHintType.MARGIN, 1); default = 4 BitMatrix matrix =
		 * new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE,
		 * qrWidth, qrheight, hints);
		 */
		// 矩阵的宽度
		int width = matrix.getWidth();
		// 矩阵的高度
		int height = matrix.getHeight();
		// 矩阵像素数组
		int[] pixels = new int[width * height];
		// 双重循环遍历每一个矩阵点
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
					// 设置矩阵像素点的值
					pixels[y * width + x] = 0xff000000;
				}
			}
		}
		// 根据颜色数组来创建位图
		/**
		 * 此函数创建位图的过程可以简单概括为为:更加width和height创建空位图，
		 * 然后用指定的颜色数组colors来从左到右从上至下一次填充颜色。 config是一个枚举，可以用它来指定位图“质量”。
		 */
		Bitmap bm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		// 通过像素数组生成bitmap,具体参考api
		bm.setPixels(pixels, 0, width, 0, 0, width, height);
		// 将生成的条形码返回给调用者
		return bm;
	}
}
