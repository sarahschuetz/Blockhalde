package com.util;

/**
 * Utility class for following bit operations:
 * <ul>
 * 	<li>Set bit at position</li>
 *  <li>Clear bit at position</li>
 *  <li>Check if bit is set</li>
 * </ul>
 * These utility functions are made for <strong>int</strong>, <strong>short</strong> and <strong>byte</strong>
 */
public class FlagUtils {
	
	/**
	 * Sets a bit at the given bit position in the mask.
	 */
	public static int setBitAt(int mask, int bitPosition) {
		mask |= 1 << bitPosition;
		return mask;
	}
	
	/**
	 * Sets a bit at the given bit position in the mask.
	 */
	public static short setBitAt(short mask, short bitPosition) {
		mask |= (short) 1 << bitPosition;
		return mask;
	}
	
	/**
	 * Sets a bit at the given bit position in the mask.
	 */
	public static byte setBitAt(byte mask, byte bitPosition) {
		mask |= (byte) 1 << bitPosition;
		return mask;
	}
	
	/**
	 * Clears a bit at the given bit position in the mask.
	 */
	public static int clearBitAt(int mask, int bitPosition) {
		mask &= ~(1 << bitPosition);
		return mask;
	}
	
	/**
	 * Clears a bit at the given bit position in the mask.
	 */
	public static short clearBitAt(short mask, short bitPosition) {
		mask &= ~((short) 1 << bitPosition);
		return mask;
	}
	
	/**
	 * Clears a bit at the given bit position in the mask.
	 */
	public static byte clearBitAt(byte mask, byte bitPosition) {
		mask &= ~((byte) 1 << bitPosition);
		return mask;
	}
	
	/**
	 * Checks if a bit at the given position is set.
	 */
	public static boolean isBitSetAt(int mask, int bitPosition) {
		return (mask & (1 << bitPosition)) != 0;
	}
	
	/**
	 * Checks if a bit at the given position is set.
	 */
	public static boolean isBitSetAt(short mask, short bitPosition) {
		return (mask & ((short) 1 << bitPosition)) != 0;
	}
	
	/**
	 * Checks if a bit at the given position is set.
	 */
	public static boolean isBitSetAt(byte mask, byte bitPosition) {
		return (mask & ((byte) 1 << bitPosition)) != 0;
	}
}