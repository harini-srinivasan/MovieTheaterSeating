package com.harini;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class MovieSeatingTest {

    Allocator allocator  = new Allocator();

    @Test
    public void testInputs() {
        Assert.assertEquals(-1, allocator.reserveSeats("R001 0"));
        Assert.assertEquals(-2, allocator.reserveSeats("R001 500"));
        Assert.assertEquals(0, allocator.reserveSeats("R001 7"));
    }

}