package com.company;

public class Filters {
    public static final int[][] MEAN_FLTR_5 = {
            {1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1}
    };

    public static final int[][] PREWITT_VER = {
            {-1,0,1},
            {-1,0,1},
            {-1,0,1}
    };

    public static final int[][] PREWITT_HOR = {
            {-1,-1,-1},
            {0,0,0},
            {1,1,1}
    };

    public static final int[][] SOBEL_VER = {
            {-1,0,1},
            {-2,0,2},
            {-1,0,1}
    };

    public static final int[][] SOBEL_HOR = {
            {-1,-2,-1},
            {0,0,0},
            {1,2,1}
    };

    public static final int[][] KIRSCH_W = {
            {-3,-3,5},
            {-3,0,5},
            {-3,-3,5}
    };

    public static final int[][] LAPLACIAN_P = {
            {0,1,0},
            {1,-4,1},
            {0,1,0}
    };

    public static final int[][] LAPLACIAN_N = {
            {0,-1,0},
            {-1,4,-1},
            {0,-1,0}
    };
}
