package cn.com.dhcc.creditquery.ent.queryweb.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;

import cn.com.dhcc.credit.platform.util.ResourceManager;

/**
 * @author lekang.liu
 * @date 2018年4月17日
 *
 */
public class ImageCreaterUtil {

	private static int width = 250;
	private static int mainInfoFontHeight = 30;
	private static int subInfoFontHeight = 24;
	private static int timeStringFontHeight = 20;
	private static Color COLOR = Color.GRAY;

	public ImageCreaterUtil() {

	}

	/**
	 * 仅用于生成信用报告水印
	 * 
	 * @param mainInfo
	 *            图片上的第一行文字
	 * @param subInfo
	 *            图片上的第二行文字
	 * @param timeString
	 *            需生成在水印上的时间，若水印不需附带时间显示，传{@code null}即可
	 * @param fos
	 *            {@link OutputStream} 用于写出图片的输出流
	 */
	public static void getMarkImage(String mainInfo, String subInfo, String timeString, OutputStream fos) {
		try {
			BufferedImage bi = new BufferedImage(width, width, BufferedImage.TYPE_INT_BGR);
			Graphics2D graphics2d = bi.createGraphics();
			graphics2d.setBackground(Color.WHITE);
			graphics2d.clearRect(0, 0, width, width);
			graphics2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.3f));
			graphics2d.setPaint(COLOR);
			graphics2d.setFont(new Font("宋体", Font.BOLD, mainInfoFontHeight));
			// X:是从左上角向右的偏移量
			// Y:是从左上角向下的偏移量
			// 近似可以理解坐标的起点为文本的坐下角
			// 重新以图片中心为坐标系
			graphics2d.translate((width / 2), (width / 2));
			// 逆时针旋转π/4
			graphics2d.rotate(-Math.PI / 4);
			float y0 = 0;
			if (mainInfo.length() < 15) {
				// 计算出偏移量
				float x = -(getLength(mainInfo, mainInfoFontHeight));
				float y = mainInfoFontHeight / 2;
				y0 = y;
				graphics2d.drawString(mainInfo, x, y);
			} else {
				String tempString = mainInfo.substring(0, mainInfo.length() % 2 == 0 ? mainInfo.length() / 2 : mainInfo.length() / 2 + 1);
				String temp2String = mainInfo.substring(mainInfo.length() % 2 == 0 ? mainInfo.length() / 2 : mainInfo.length() / 2 + 1, mainInfo.length());
				// 计算出偏移量
				float x1 = -(getLength(tempString, mainInfoFontHeight));
				float y1 = 0;
				float x2 = -(getLength(temp2String, mainInfoFontHeight));
				float y2 = mainInfoFontHeight;
				y0 = y2;
				graphics2d.drawString(tempString, x1, y1);
				graphics2d.drawString(temp2String, x2, y2);

			}
			// 填写水印生成时间和用户机构
			float x3 = -(getLength(subInfo, subInfoFontHeight));
			float y3 = y0 + subInfoFontHeight * 3 / 2;

			graphics2d.setFont(new Font("宋体", Font.ITALIC, subInfoFontHeight));
			graphics2d.drawString(subInfo, x3, y3);

			if (StringUtils.isNotBlank(timeString)) {
				float x4 = -(getLength(timeString, timeStringFontHeight));
				float y4 = y3 + timeStringFontHeight;
				graphics2d.setFont(new Font("宋体", Font.PLAIN, timeStringFontHeight));
				graphics2d.drawString(timeString, x4, y4);
			}

			graphics2d.dispose();
			ImageIO.write(bi, "PNG", fos);
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	private static float getLength(String in, int mainInfoFontHeight) {
		char[] chararray = in.toCharArray();
		int half = 0;
		int full = 0;
		for (int i = 0; i < chararray.length; i++) {
			if ((int) chararray[i] < 19968 || (int) chararray[i] > 171941) {
				if (Character.isLowerCase(chararray[i])) {
					half = half + 1;
					continue;
				}
				if (Character.isDigit(chararray[i])) {
					half = half + 1;
					continue;
				}
				if ('_' == chararray[i]) {
					half = half + 1;
					continue;
				}
			}
			full = full + 1;
		}
		float halfF = Float.valueOf(half);
		float fullF = Float.valueOf(full);
		float fontHeightF = Float.valueOf(mainInfoFontHeight);

		return ((halfF / 2f + fullF) / 2f * fontHeightF);
	}

	public static void main(String[] args) {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream("C:\\Users\\Administrator\\Desktop\\7.png");
			ImageCreaterUtil.getMarkImage("liulekang", "东华软件", "", fos);
			System.out.println("-------------");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
