/* Copyright (C) 4/12/2015 Ben Andrews ben.andrews@gmail.com
 * All Rights Reserved.
 */

package com.example.beandrew.simplepedometer;

import java.util.Arrays;

public class StepCounter {

    float Series[];
    float NormalizedSeries[];
    float Threshold;
    int Steps;
    int Size;
    int Pos;
    int PrevPos;
    boolean Full;

    StepCounter(int windowSize, float threshold) {
        Size = windowSize;
        Series = new float[windowSize];
        NormalizedSeries = new float[windowSize];
        Threshold = threshold;
        Pos = 0;
        Steps = 0;
        Full = false;
    }

    void ResetSteps() {
        Steps = 0;
    }

    float InputData(float data) {
        if(!Full) {
            Full = (Pos == Size - 1);
        }

        PrevPos = Pos;
        Series[Pos++] = data;
        Pos = Pos % Size;

        float normalized = 0;
        if(Full) {
            //
            // Normalize data.
            //
            normalized = data - SeriesAverage();
            NormalizedSeries[Pos] = normalized;

            //
            // Count steps if we cross threshold.
            //
            if(NormalizedSeries[PrevPos] <= Threshold && NormalizedSeries[Pos] > Threshold){
                Steps++;
            }
        }
        return normalized;
    }

    private float SeriesAverage() {
        float sum = 0;
        for (float d : Series) sum += d;

        return sum / Size;
    }
}
