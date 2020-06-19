package nicelee.bilibili;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nicelee.bilibili.gif.GifUtil;
import nicelee.bilibili.gif.ImgUtil;
import nicelee.bilibili.gif.bean.TextOption;

public class Bot {

	private static HashMap<String, int[][]> gifConfigs;
	static {
		gifConfigs = new HashMap<>(20, 0.9f);
		// { {台词数量, 文本加词位置Y坐标}, { gif宽度, gif高度}, {字体类型, 字体颜色类型}, { 台词1起始帧, 台词1结束帧 }, ...
		// }
		// gifConfigs.put("谁赞成谁反对", new int[][] { { num, y }, {0, 0}, { width, height },
		// });
		gifConfigs.put("真香",
				new int[][] { { 4, 165 }, { 298, 184 }, { 0, 0 }, { 0, 8 }, { 12, 23 }, { 25, 34 }, { 37, 47 } });
		gifConfigs.put("谁赞成谁反对", new int[][] { { 3, 150 }, { 300, 168 }, { 0, 0 }, { 4, 16 }, { 18, 35 }, { 37, 44 } });
		gifConfigs.put("元首骂人", new int[][] { { 3, 110 }, { 200, 114 }, { 0, 0 }, { 0, 31 }, { 43, 67 }, { 68, 96 } });
		gifConfigs.put("我卢本伟没有开挂",
				new int[][] { { 3, 210 }, { 300, 225 }, { 1, 0 }, { 0, 12 }, { 14, 20 }, { 21, 33 } });
	}

	public static void main(String[] a) throws FileNotFoundException {
		FileOutputStream output = new FileOutputStream("我卢本伟没有开挂.gif");
		// Bot.gen("真香", output, "我就是饿死", "死外边，从这里跳下去", "不会吃你们一点东西", "真香");
		Bot.gen("我卢本伟没有开挂", output, "我没有开挂", "我卢本伟", "没有开挂");
	}

	/**
	 * 给Gif添加台词
	 * 
	 * @param gifType
	 * @param fout    gif输出流
	 * @param strs    台词
	 */
	public static void gen(String gifType, OutputStream fout, String... strs) {
		int[][] gifConfig = gifConfigs.get(gifType);
		if (gifConfig == null) {
			System.err.println("不存在当前gifType：" + gifType);
			return;
		}
		gen(gifType, gifConfig, fout, strs);
	}

	/**
	 * 给Gif添加台词
	 * 
	 * @param gifType
	 * @param gifConfig 可以调用getGifConfig获得
	 * @param fout      gif输出流
	 * @param strs      台词
	 */
	public static void gen(String gifType, int[][] gifConfig, OutputStream fout, String... strs) {
		int num = gifConfig[0][0];
		if (strs.length < num) {
			System.err.println("台词数量不够");
			return;
		}
		int offsetY = gifConfig[0][1];
		int width = gifConfig[1][0];
		Font font = TextOption.fonts[gifConfig[2][0]];
		Color color = TextOption.colors[gifConfig[2][1]];
		List<TextOption> options = new ArrayList<TextOption>();
		for (int i = 0; i < gifConfig.length - 3; i++) {
			TextOption option = new TextOption(gifConfig[i + 3][0], gifConfig[i + 3][1], strs[i],
					ImgUtil.offsetXCenter(width, strs[i], font), offsetY, font, color);
			options.add(option);
		}
		run(gifType, fout, options);
	}

	private static void run(String gifType, OutputStream fout, List<TextOption> options) {
		try {
			File gif = new File("pics/" + gifType + "/notext.gif");
			int frameRate = GifUtil.getAssumingFrameRate(gif, 1);
			FileInputStream source = new FileInputStream(gif);
			GifUtil.addText(source, fout, options, frameRate);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static int[][] getGifConfig(String gifType) {
		return gifConfigs.get(gifType);
	}
}
