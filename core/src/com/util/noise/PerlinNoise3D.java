package com.util.noise;

import java.util.Random;

public class PerlinNoise3D {
	
	public int repeat;

	/**
	 * Hash lookup table as defined by Ken Perlin. This is a randomly arranged
	 * array of all numbers from 0-255 inclusive.
	 */
	private static final int[] permutation_table = { 151, 160, 137, 91, 90, 15, 131, 13, 201, 95, 96, 53, 194, 233, 7,
			225, 140, 36, 103, 30, 69, 142, 8, 99, 37, 240, 21, 10, 23, 190, 6, 148, 247, 120, 234, 75, 0, 26, 197, 62,
			94, 252, 219, 203, 117, 35, 11, 32, 57, 177, 33, 88, 237, 149, 56, 87, 174, 20, 125, 136, 171, 168, 68, 175,
			74, 165, 71, 134, 139, 48, 27, 166, 77, 146, 158, 231, 83, 111, 229, 122, 60, 211, 133, 230, 220, 105, 92,
			41, 55, 46, 245, 40, 244, 102, 143, 54, 65, 25, 63, 161, 1, 216, 80, 73, 209, 76, 132, 187, 208, 89, 18,
			169, 200, 196, 135, 130, 116, 188, 159, 86, 164, 100, 109, 198, 173, 186, 3, 64, 52, 217, 226, 250, 124,
			123, 5, 202, 38, 147, 118, 126, 255, 82, 85, 212, 207, 206, 59, 227, 47, 16, 58, 17, 182, 189, 28, 42, 223,
			183, 170, 213, 119, 248, 152, 2, 44, 154, 163, 70, 221, 153, 101, 155, 167, 43, 172, 9, 129, 22, 39, 253,
			19, 98, 108, 110, 79, 113, 224, 232, 178, 185, 112, 104, 218, 246, 97, 228, 251, 34, 242, 193, 238, 210,
			144, 12, 191, 179, 162, 241, 81, 51, 145, 235, 249, 14, 239, 107, 49, 192, 214, 31, 181, 199, 106, 157, 184,
			84, 204, 176, 115, 121, 50, 45, 127, 4, 150, 254, 138, 236, 205, 93, 222, 114, 67, 29, 24, 72, 243, 141,
			128, 195, 78, 66, 215, 61, 156, 180 };

	private final int[] p = new int[512]; // Doubled permutation to avoid overflow
	
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
		double total = 0;
	    double frequency = 1;
	    double amplitude = 1;
	    double maxValue = 0;  // Used for normalizing result to 0.0 - 1.0
	    
	    for(int i = 0; i < octaves; i++) {
	        total += calcPerlinAt(x * frequency, y * frequency, z * frequency) * amplitude;
	        maxValue += amplitude;
	        
	        amplitude *= persistence;
	        frequency *= 2;
	    }
	    
	    return total / maxValue;
	}
	
	
	/**
	 * 
	 * @return double between 0.0 and 1.0
	 */
	public double calcPerlinAt(double x, double y, double z) {
		
		// add random offset
		x += offsetX;
        y += offsetY;
        z += offsetZ;

		if (repeat > 0) { // If we have any repeat on, change the coordinates to their "local" repetitions
			x = x % repeat;
			y = y % repeat;
			z = z % repeat;
		}
		
		// & -> clamp between 0 and 255
		int xi = (int) x & 255; // Calculate the "unit cube" that the point asked will be located in
		int yi = (int) y & 255; // The left bound is ( |_x_|,|_y_|,|_z_| ) and the right bound is that
		int zi = (int) z & 255; // plus 1. Next we calculate the location (from 0.0 to 1.0) in that cube.
		
		// floating difference
		double xf = x - (int) x;
		double yf = y - (int) y;
		double zf = z - (int) z;
		
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
	    
	    return (lerp(y1, y2, w) + 1) /2; 
	}
	
	/**
	 * 
	 * Fade function as defined by Ken Perlin.
     * This eases coordinate values so that they will ease towards integral values.  
     * This ends up smoothing the final output.
     * 
     * 6t^5 - 15t^4 + 10t^3
	 * 
	 */
	private static double fade(double t) {
	
		return t * t * t * (t * (t * 6 - 15) + 10);  
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
	
	/**
	 * 
	 * Calculate the dot product of a randomly selected gradient vector and the 8 location vectors.
	 * (alternate way of writing the original code from Perlin in a much more easy-to-understand way (and actually faster in many languages))
	 * Source: http://riven8192.blogspot.com/2010/08/calculate-perlinnoise-twice-as-fast.html
	 * 
	 * @param hash last 4 bits of the hash value determine which vector is being used 
	 * @param x represents part x of the location vector (that will be used for the dot product)
	 * @param y represents part y of the location vector (that will be used for the dot product)
	 * @param z represents part z of the location vector (that will be used for the dot product)
	 * @return random vector from the following 12 vectors (1,1,0), (-1,1,0), (1,-1,0), (-1,-1,0), (1,0,1), (-1,0,1), (1,0,-1), (-1,0,-1), (0,1,1), (0,-1,1), (0,1,-1), (0,-1,-1)
	 */
	private static double grad(int hash, double x, double y, double z)
	{
	    switch(hash & 0xF)
	    {
	        case 0x0: return  x + y;
	        case 0x1: return -x + y;
	        case 0x2: return  x - y;
	        case 0x3: return -x - y;
	        case 0x4: return  x + z;
	        case 0x5: return -x + z;
	        case 0x6: return  x - z;
	        case 0x7: return -x - z;
	        case 0x8: return  y + z;
	        case 0x9: return -y + z;
	        case 0xA: return  y - z;
	        case 0xB: return -y - z;
	        case 0xC: return  y + x;
	        case 0xD: return -y + z;
	        case 0xE: return  y - x;
	        case 0xF: return -y - z;
	        default: return 0; // never happens
	    }
	}
	
	/**
	 *  Linear Interpolate
	 */
	private static double lerp(double a, double b, double x) {
	    return a + x * (b - a);
	}
	
}
