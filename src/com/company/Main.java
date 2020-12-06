package com.company;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static final int[][] MEAN_FLTR_5 = {{1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1}};

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        long buffered = filter2DBuffered("images/pepe.jpg", "images/buffered.jpg", MEAN_FLTR_5);
        long openCVmat = filter2DMat("images/pepe.jpg", "images/mat.jpg", MEAN_FLTR_5);
        System.out.println("Buffered took " + buffered + " ms.");
        System.out.println("OpenCV matrix took " + openCVmat + " ms.");

    }


    public static long filter2DMat(String inputFilename, String outputFilename, int[][] filter) {

        Mat source = Imgcodecs.imread(inputFilename);
        Mat out = new Mat(source.rows(), source.cols(), source.type());

//        double[] data = source.get(0,0);
//        System.out.println(data);
        double[] pixel;
        long time = System.currentTimeMillis();
        for (int i = 0; i < source.cols(); i++) {
            for (int j = 0; j < source.rows(); j++) {
                double[] data = new double[3];
                for (int k = 0; k < filter.length; k++) {
                    for (int l = 0; l < filter[k].length; l++) {
                        int col = (i - filter.length / 2 + k + source.cols()) % source.cols();
                        int row = (j - filter[k].length / 2 + l + source.rows()) % source.rows();
                        pixel = source.get(row, col);
                        data[0] += pixel[0] * filter[k][l];
                        data[1] += pixel[1] * filter[k][l];
                        data[2] += pixel[2] * filter[k][l];
                    }
                }
                for(int m = 0; m < 3; m++) {
                    data[m] /= (filter.length * filter[0].length);
                    if (data[m] > 255) {
                        data[m] = 255;
                    } else if (data[m] < 0) {
                        data[m] = 0;
                    }
                }

                out.put(j, i, data);
            }
        }
        time = System.currentTimeMillis() - time;
        Imgcodecs.imwrite(outputFilename, out);
        return time;
    }

    public static long filter2DBuffered(String inputFilename, String outputFilename, int[][] filter) {
        BufferedImage image = null;
        File inputFile = new File(inputFilename);
        try {
            image = ImageIO.read(inputFile);
            long time = System.currentTimeMillis();
            filter2DBuffered(image, MEAN_FLTR_5);
            time = System.currentTimeMillis() - time;
            File output = new File(outputFilename);
            ImageIO.write(image, "jpg", output);
            return time;
        } catch (IOException e) {
            System.out.println(e);
        }
        return 0;
    }



    public static void filter2DBuffered(BufferedImage img, int[][] filter) {
        int[] rgb = new int[3];
        int[] pixel;
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                int n = 0;
                for (int k = 0; k < filter.length; k++) {
                    for (int l = 0; l < filter[k].length; l++) {
                        int imgX = (i - filter.length / 2 + k + img.getWidth()) % img.getWidth();
                        int imgY = (j - filter[k].length / 2 + l + img.getHeight()) % img.getHeight();
                        pixel = img.getRaster().getPixel(imgX, imgY, new int[3]);
                        rgb[0] += pixel[0] * filter[k][l];
                        rgb[1] += pixel[1] * filter[k][l];
                        rgb[2] += pixel[2] * filter[k][l];
                    }
                }
               for(int m = 0 ; m < 3; m++) {
                   rgb[m] /= (filter.length * filter[0].length);
                   if (rgb[m] > 255) {
                       rgb[m] = 255;
                   } else if (rgb[m] < 0) {
                       rgb[m] = 0;
                   }
               }

                Color color = new Color(rgb[0], rgb[1], rgb[2]);
                img.setRGB(i, j, color.getRGB());
            }

        }
    }

}
