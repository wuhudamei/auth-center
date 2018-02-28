package com.rocoinfo.utils;

import com.google.common.collect.Maps;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * 用于生成二位码的工具类
 *
 * @author huyt
 * @date 2015/10/27
 */
public class QrcodeUtils {

    /**
     * 私有化构造方法,禁止创建实例
     */
    private QrcodeUtils() {
    }

    /**
     * 默认宽度
     */
    private static final int DEFAULT_WIDTH = 300;

    /**
     * 默认长度
     */
    private static final int DEFAULT_HEIGHT = 300;

    /**
     * 默认格式类型
     */
    private static final String DEFAULT_FORMAT = "png";

    /**
     * 二维码周围白边的宽度
     */
    private static final Integer DEFAULT_MARGIN = 0;

    /**
     * 默认纠错等级
     * 纠错登记越高可携带内容越少
     */
    private static final ErrorCorrectionLevel DEFAULT_ECL = ErrorCorrectionLevel.L;

    /**
     * 二维码图片的存储目录
     */
    private static final String DEFAULT_FILE_DIR = "qrcode";

    /**
     * 生成二维码
     *
     * @param content 二维码携带内容
     * @return 生成二维码图片的url
     */
    public static String generateQRCode(String content) {
        return generateQRCode(content, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_FORMAT, DEFAULT_MARGIN, DEFAULT_ECL, null);
    }

    /**
     * 生成二维码
     *
     * @param content 内容
     * @param os      输出流
     * @return
     */
    public static String generateQRCode(String content, OutputStream os) {
        return generateQRCode(content, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_FORMAT, DEFAULT_MARGIN, DEFAULT_ECL, os);
    }

    /**
     * 生成二维码
     *
     * @param content 二维码携带内容
     * @param format  二维码图片的格式
     * @return 生成二维码图片的url
     */
    public static String generateQRCode(String content, String format) {
        return generateQRCode(content, DEFAULT_WIDTH, DEFAULT_HEIGHT, format, DEFAULT_MARGIN, DEFAULT_ECL, null);
    }

    /**
     * 生成二维码
     *
     * @param content 二维码携带内容
     * @param width   二维码宽度
     * @param height  二维码高度
     * @return 生成二维码图片的url
     */
    public static String generateQRCode(String content, int width, int height) {
        return generateQRCode(content, width, height, DEFAULT_FORMAT, DEFAULT_MARGIN, DEFAULT_ECL, null);
    }

    /**
     * 生成二维码
     *
     * @param content 二维码携带内容
     * @param width   二维码宽度
     * @param height  二维码高度
     * @param format  二维码格式类型
     * @return 生成二维码图片的url
     */
    public static String generateQRCode(String content, int width, int height, String format) {
        return generateQRCode(content, width, height, format, DEFAULT_MARGIN, DEFAULT_ECL, null);
    }

    /**
     * 生成二维码图片
     *
     * @param content 二维码携带内容
     * @param width   二维码宽度
     * @param height  二维码高度
     * @param format  二维码格式类型
     * @param margin  二维码内容距离边框的距离(白边的宽度)
     * @param ecl     纠错等级,等级越高,携带内容越少
     * @param os      输出流（为null时，默认输出到文件）
     * @return 生成二维码图片的url
     */
    public static String generateQRCode(String content, int width, int height, String format, Integer margin, ErrorCorrectionLevel ecl, OutputStream os) {
        Map<EncodeHintType, Object> hint = Maps.newConcurrentMap();
        hint.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hint.put(EncodeHintType.ERROR_CORRECTION, ecl);
        hint.put(EncodeHintType.MARGIN, margin);
        String extension = format;
        if (StringUtils.isBlank(extension)) {
            extension = DEFAULT_FORMAT;
        }
        try {
            String filePath = getOrGenerateFilePath(extension);
            if (StringUtils.isNoneBlank(filePath)) {
                // 生成字节矩阵
                BitMatrix matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hint);
                // 字节矩阵输出为二维码图片
                if (os == null) {
                    os = new FileOutputStream(new File(filePath));
                }
                MatrixToImageWriter.writeToStream(matrix, format, os);
                return filePath;
            }
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(os);
        }
        // 如果失败,并且没有抛出异常,返回null
        return null;
    }

    /**
     * 获取二维码图片的存储路径（不存在重新创建创建），包含文件名
     *
     * @param extension 文件扩展名
     * @return
     */
    private static String getOrGenerateFilePath(String extension) {
        // TODO 需要修改
        return "/";
    }

    /**
     * 生成文件名
     *
     * @param extension 文件扩展名
     * @return
     */
    private static String generateFileName(String extension) {
        return System.currentTimeMillis() + RandomStringUtils.random(6) + "." + extension;
    }

}
