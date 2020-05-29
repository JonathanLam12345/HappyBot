package com.myapp.jlam.motivationalbot;

import com.myapp.jlam.motivationalbot.unitTest.Computation;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ComputationUnitTest
{
    @Test
    public void sum_isCorrect()
    {
        Computation computation = new Computation();
        assertEquals(4, computation.Sum(2, 2));
    }

    @Test
    public void multiply_isCorrect()
    {
        Computation computation = new Computation();
        assertEquals(5, computation.Multiply(2, 2));
    }
}