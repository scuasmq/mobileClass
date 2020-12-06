package zt.demo.ClassExample;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;

public class MatrixToImageWriter {

    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;
    String rootPath = this.getClass().getResource("../../../../").getPath();

    private MatrixToImageWriter() {
    }

    public static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }

    public static void writeToFile(BitMatrix matrix, String format, File file)
            throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, file)) {
            throw new IOException("Could not write an image of format "
                    + format + " to " + file);
        }
    }

    public static void writeToStream(BitMatrix matrix, String format,
                                     OutputStream stream) throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, stream)) {
            throw new IOException("Could not write an image of format " + format);
        }
    }

    public  static void  writePng(String path,String couponBatchCode) throws Exception{
        String text = couponBatchCode; // 二维码内容
        int width = 300; // 二维码图片宽度
        int height = 300; // 二维码图片高度
        String format = "png";// 二维码的图片格式

        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8"); // 内容所使用字符集编码

        BitMatrix bitMatrix = new MultiFormatWriter().encode(text,
                BarcodeFormat.QR_CODE, width, height, hints);
        // 生成二维码 文件地址 是否存在不存在则新增
        File outputFile = new File(path);
        if(!outputFile.getParentFile().exists()){
            outputFile.getParentFile().mkdirs();
        }
        if(!outputFile.exists()){
            outputFile.createNewFile();
        }
        MatrixToImageWriter.writeToFile(bitMatrix, format, outputFile);
    }
    public static void  writeTxt(String path,String couponBatchCode) throws Exception{
        String text = couponBatchCode; // 二维码内容
        File outputFile = new File(path);
        if(!outputFile.getParentFile().exists()){
            outputFile.getParentFile().mkdirs();
        }
        if(!outputFile.exists()){
            outputFile.createNewFile();
        }
        FileWriter fw=new FileWriter(outputFile);
        fw.write(text);
        fw.close();
    }

    public static void main(String[] args) throws Exception {
        String text="310055227524767";
        String path=new MatrixToImageWriter().rootPath+ File.separator +"31005"+File.separator+"QR"+File.separator+"31005_QR_"+text+".png";
        writePng(path,text);
        String path2=new MatrixToImageWriter().rootPath+ File.separator +"31005"+File.separator+"TXT"+File.separator+"31005_TXT_"+text+".txt";
        writeTxt(path2,text);
    }
}