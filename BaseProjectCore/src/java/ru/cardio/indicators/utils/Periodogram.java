/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.cardio.indicators.utils;

public class Periodogram {
    private double frequency;
    private double value;

    public Periodogram(double frequency, double value) {
        this.frequency = frequency;
        this.value = value;
    }

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String toString() {
        return frequency + " " + value;
    }
}
