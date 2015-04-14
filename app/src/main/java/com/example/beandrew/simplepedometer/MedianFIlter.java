/* Copyright (C) 4/12/2015 Ben Andrews ben.andrews@gmail.com
 * All Rights Reserved.
 *
 * You may use, distribute and modify this code
 * under the terms of the The MIT License (MIT).
 */

package com.example.beandrew.simplepedometer;

import java.util.Arrays;

public class MedianFIlter {

    float Series[];
    int Size;
    int Pos;

    MedianFIlter(int size) {
        Size = size;
        Series = new float[size];
        int Pos = 0;
    }

    float ApplyFilter(float data) {
        Series[Pos++] = data;
        Pos = Pos % Size;

        return Median(Series);
    }

    private float Median(float series[]) {
        float[] tempArray = new float[Size];
        System.arraycopy(series, 0, tempArray, 0, Size);

        Arrays.sort(tempArray);
        float median;
        if (tempArray.length % 2 == 0) {
            median = ( tempArray[tempArray.length / 2] + tempArray[tempArray.length / 2 - 1]) / 2;
        }
        else {
            median = tempArray[tempArray.length / 2];
        }

        return median;
    }
}
