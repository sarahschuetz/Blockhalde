package com.util.noise;

import java.util.Random;

public class PerlinNoise3D extends Noise {
	
	public int repeat;
	
	// for more random results
	double offsetX;
	double offsetY;
	double offsetZ;
	
	public PerlinNoise3D() {
		this.repeat = -1;
		
		for (int x = 0; x < 512; x++) {
			p[x] = permutation_table[x % 256];
		}
	}
	
	public PerlinNoise3D(long seed) {
		this(seed, -1);
	}
	
	public PerlinNoise3D(long seed, int repeat) {
		this.repeat = repeat;
		
		Random random = new Random(seed);
		
		// nextDouble() returns the next pseudorandom double value between 0.0 and 1.0
		// * 256 -> numbers between 0 and 255
		offsetX = random.nextDouble() * 256;
	    offsetY = random.nextDouble() * 256;
	    offsetZ = random.nextDouble() * 256;

	    // fill first 256 values of p[] with pseudorandom numbers
	    for(int i = 0; i < 256; i++) {
	    	
	    	// Returns a pseudorandom int value between 0 and the specified value (exclusive)
	    	p[i] = random.nextInt(256);
	    }

	    // TODO: find out what this is for!
        for(int i = 0; i < 256; i++) {
            int pos = random.nextInt(256 - i) + i;
            int old = p[i];

            p[i] = p[pos];
            p[pos] = old;
            p[i + 256] = p[i];
        }
	}

	/**
	 * Use octaves and persistence for calculating perlin noise
	 * 
	 * @return double between 0.0 and 1.0
	 */
	public double calcPerlinAt(double x, double y, double z, int octaves, double persistence) {
		double total = 0.0;
	    double frequency = 1.0;
	    double amplitude = 1.0;
	    double maxValue = 0.0;  // Used for normalizing result to 0.0 - 1.0
	    
	    for(int i = 0; i < octaves; i++) {
	        total += calcPerlinAt(x * frequency, y * frequency, z * frequency) * amplitude;
	        maxValue += amplitude;
	        
	        amplitude *= persistence;
	        frequency *= 2;
	    }
	    
	    return total / maxValue;
	}
	
	
	/**
	 * Calculate perlin noise at coordinates
	 * 
	 * @return double between 0.0 and 1.0
	 */
	public double calcPerlinAt(double x, double y, double z) {
		
//		if(x < 0 || y < 0 || z < 0) {
//			throw new RuntimeException("Negative input for calcPerlinAt() is not possible! (x: " + x + ", y: " + y + ", z: " + z + ")");
//		}
		
		// add random offset
		x += offsetX;
        y += offsetY;
        z += offsetZ;

		if (repeat > 0) { // If there is a repeat, change the coordinates to their "local" repetitions
			x = x % repeat;
			y = y % repeat;
			z = z % repeat;
		}
		
		int floorX = floor(x);
		int floorY = floor(y);
		int floorZ = floor(z);
		
		// Find unit cube containing the point
		// & -> clamp between 0 and 255
		int xi = floorX & 255; // Calculate the "unit cube" that the point asked will be located in
		int yi = floorY & 255; // The left bound is ( |_x_|,|_y_|,|_z_| ) and the right bound is that
		int zi = floorZ & 255; // plus 1. Next we calculate the location (from 0.0 to 1.0) in that cube.
		
		// Get relative xyz coordinates of the point within the cube
		// floating difference
		double xf = x - floorX;
		double yf = y - floorY;
		double zf = z - floorZ;
		
		// Compute fade curves for xyz
		// used for interpolation
		double u = fade(xf);
	    double v = fade(yf);
	    double w = fade(zf);
	    
	    // The Perlin Noise hash function is used to get a unique value for every coordinate input.
	    // The hash function hashes all 8 unit cube coordinates surrounding the input coordinate.
	    // The result of this hash function is a value between 0 and 255 (inclusive) because of our p[] array.
	    int aaa, aba, aab, abb, baa, bba, bab, bbb;
	    
	    aaa = p[p[p[    xi ] +    yi ] +    zi ];
	    aba = p[p[p[    xi ] + inc(yi)] +    zi ];
	    aab = p[p[p[    xi ] +    yi ] + inc(zi)];
	    abb = p[p[p[    xi ] + inc(yi)] + inc(zi)];
	    baa = p[p[p[inc(xi)] +    yi ] +    zi ];
	    bba = p[p[p[inc(xi)] + inc(yi)] +    zi ];
	    bab = p[p[p[inc(xi)] +    yi ] + inc(zi)];
	    bbb = p[p[p[inc(xi)] + inc(yi)] + inc(zi)];
	    
		
		// The gradient function calculates the dot product between a pseudorandom
		// gradient vector and the vector from the input coordinate to the 8
		// surrounding points in its unit cube.
		// This is all then lerped together as a sort of weighted average based on the faded (u,v,w)
		// values we made earlier.
	    double x1, x2, y1, y2;
	    x1 = lerp(grad(aaa, xf, yf, zf), grad(baa, xf-1, yf, zf), u);                                     
	    x2 = lerp(grad(aba, xf, yf-1, zf), grad(bba, xf-1, yf-1, zf), u);
	   
	    y1 = lerp(x1, x2, v);

	    x1 = lerp(grad(aab, xf, yf, zf-1), grad(bab, xf-1, yf, zf-1), u);
	    x2 = lerp(grad(abb, xf, yf-1, zf-1), grad(bbb, xf-1, yf-1, zf-1), u);
	   
	    y2 = lerp(x1, x2, v);
	    
	    // For convenience we bind the result to 0 - 1 (theoretical min/max before is [-1, 1])
	    return (lerp(y1, y2, w) + 1) /2; 
	}
	
	/**
	 * 
	 * Simply used to increment the numbers and make sure that the noise still repeats.
	 * If you didn't care about the ability to repeat num + 1 would have the same effect.
	 * 
	 */
	private int inc(int num) {
	    
		num++;
	    
		if (repeat > 0) {
	    	num %= repeat;
	    }
	    
	    return num;
	}
	
}
