package com.util.noise;

public class Noise {
	
	/**
	 * Hash lookup table as defined by Ken Perlin. This is a randomly arranged
	 * array of all numbers from 0-255 inclusive.
	 */
	protected static final int[] permutation_table = { 151, 160, 137, 91, 90, 15, 131, 13, 201, 95, 96, 53, 194, 233, 7,
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
	
	protected final int[] p = new int[512]; // Doubled permutation to avoid overflow
	
    protected double offsetX;
    protected double offsetY;
    protected double offsetZ;
    
    /**
     * Speedy floor, faster than (int) Math.floor(x)
     *
     * @param x Value to floor
     * @return Floored value
     */
    protected static int floor(double x) {
        return x >= 0 ? (int) x : (int) x - 1;
    }
    
    /**
	 * 
	 * Fade function as defined by Ken Perlin.
	 * 
	 * Linear interpolation, while computationally cheap, looks unnatural.
	 * Fade for a smoother transition between gradients (ease curve).
	 * 
     * This eases coordinate values so that they will ease towards integral values.  
     * This ends up smoothing the final output.
     * 
     * 6t^5 - 15t^4 + 10t^3
	 * 
	 */
    protected static double fade(double t) {
	
		return t * t * t * (t * (t * 6 - 15) + 10);  
	}
    
    /**
	 *  Linear Interpolation
	 *  
	 *  @param a first value to interpolate
	 *  @param b second value to interpolate
	 *  @param x interpolation value (0-1)
	 *  
	 */
    protected static double lerp(double a, double b, double x) {
	    return a + x * (b - a);
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
	 * 
	 * 12 vectors (vectors of the center point to the edges of the cube):
	 * (1,1,0), (-1,1,0), (1,-1,0), (-1,-1,0), (1,0,1), (-1,0,1), (1,0,-1), (-1,0,-1), (0,1,1), (0,-1,1), (0,1,-1), (0,-1,-1)
	 * 
	 * @return dot product
	 */
    protected static double grad(int hash, double x, double y, double z)
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
}
