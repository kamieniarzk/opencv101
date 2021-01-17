package com.company;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Main {

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        long myImpl = filter2DMyImpl("images/nyc.jpg", "images/buffered.jpg", Filters.SOBEL_VER);
        long openCVFilter = filter2DopenCV("images/nyc.jpg", "images/opencv.jpg", Filters.SOBEL_VER);
        System.out.println("My filter2D took " + myImpl + " ms.");
        System.out.println("OpenCV filter2D took " + openCVFilter + " ms.");

    }

    public static long filter2DopenCV(String inputFilename, String outputFilename, int[][] filter) {

        Mat source = Imgcodecs.imread(inputFilename);
        Mat destination = new Mat(source.rows(),source.cols(),source.type());

        Mat filterMat = new Mat(filter.length, filter[0].length, CvType.CV_32F);
        long time = System.currentTimeMillis();
        for(int row = 0; row < filter.length; row++) {
            for(int col = 0; col < filter[0].length; col++) {
                filterMat.put(row, col, filter[row][col]);
            }
        }

        Imgproc.filter2D(source, destination, source.depth(), filterMat);
        time = System.currentTimeMillis() - time;
        Imgcodecs.imwrite(outputFilename,destination);
        return time;
    }

    public static long filter2DMat(String inputFilename, String outputFilename, int[][] filter) {

        Mat source = Imgcodecs.imread(inputFilename);
        Mat out = new Mat(source.rows(), source.cols(), source.type());

        double[] pixel = new double[3];
        long time = System.currentTimeMillis();

        for (int col = 0; col < source.cols(); col++) {
            for (int row = 0; row < source.rows(); row++) {
                double[] data = new double[3];
                for (int y = 0; y < filter.length; y++) {
                    for (int x = 0; x < filter[y].length; x++) {
                        int imgY = row - filter.length / 2 + y;
                        int imgX = col - filter[y].length / 2 + x;

                        if(imgY < 0 || imgY >= source.rows() || imgX < 0 || imgX >= source.cols()) {
//                            pixel[0] = 0; pixel[1] = 0; pixel[2] = 0;
                            continue;
                        } else {
                            pixel = source.get(imgY, imgX);
                        }
                        data[0] += pixel[0] * filter[y][x];
                        data[1] += pixel[1] * filter[y][x];
                        data[2] += pixel[2] * filter[y][x];
                    }
                }
                for(int m = 0; m < 3; m++) {
                    data[m] = Math.min(data[m], 255);
                    data[m] = Math.max(data[m], 0);
                }
                out.put(row, col, data);
            }
        }
        time = System.currentTimeMillis() - time;
        Imgcodecs.imwrite(outputFilename, out);
        return time;
    }

    public static long filter2DMyImpl(String inputFilename, String outputFilename, int[][] filter) {
        BufferedImage image = null;
        File inputFile = new File(inputFilename);
        try {
            image = ImageIO.read(inputFile);
            long time = System.currentTimeMillis();
            image = filter2DMyImpl(image, filter);
            time = System.currentTimeMillis() - time;
            File output = new File(outputFilename);
            ImageIO.write(image, "jpg", output);
            return time;
        } catch (IOException e) {
            System.out.println(e);
        }
        return 0;
    }



    public static BufferedImage filter2DMyImpl(BufferedImage input, int[][] filter) {
        BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(), input.getType());
        int[] pixel = new int[3];
        for (int col = 0; col < input.getWidth(); col++) {
            for (int row = 0; row < input.getHeight(); row++) {
                int[] rgb = new int[3];
                for (int y = 0; y < filter.length; y++) {
                    for (int x = 0; x < filter[y].length; x++) {
                        int imgY = row - filter.length / 2 + y;
                        int imgX = col - filter[y].length / 2 + x;
                        if(imgY < 0 || imgY >= input.getHeight() || imgX < 0 || imgX >= input.getWidth()) {
                            continue;
                        } else {
                            pixel = input.getRaster().getPixel(imgX, imgY, new int[3]);
                        }
                        rgb[0] += pixel[0] * filter[y][x];
                        rgb[1] += pixel[1] * filter[y][x];
                        rgb[2] += pixel[2] * filter[y][x];
                    }
                }
               for(int m = 0 ; m < 3; m++) {
                   rgb[m] = Math.min(rgb[m], 255);
                   rgb[m] = Math.max(rgb[m], 0);
               }
                int color = new Color(rgb[0], rgb[1], rgb[2]).getRGB();
                output.setRGB(col, row, color);
            }
        }
        return output;
    }

}
