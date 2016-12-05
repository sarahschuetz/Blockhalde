package test.util;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;

import com.util.FlagUtils;

public class FlagUtilsTest {

	@Test
	public void setBitTest_Integer() {
		int mask  = 0;
		mask = FlagUtils.setBitAt(mask, 0);
		assertThat("Should be 1.", mask, is(1));
		
		mask = 0;
		mask = FlagUtils.setBitAt(mask, 3);
		assertThat("Should be 8.", mask, is(8));
		
		mask = 0;
		mask = FlagUtils.setBitAt(mask, 31);
		assertThat("Should be -2147483648.", mask, is(Integer.MIN_VALUE));
		
		// 'Overflow' -> After 31 it starts again from the start.
		mask = 0;
		mask = FlagUtils.setBitAt(mask, 33);
		assertThat("Should be 2.", mask, is(2));
	}
	
	@Test
	public void setBitTest_Short() {
		short mask  = 0;
		mask = FlagUtils.setBitAt(mask, (short) 0);
		assertThat("Should be 1.", mask, is((short) 1));
		
		mask = 0;
		mask = FlagUtils.setBitAt(mask, (short) 3);
		assertThat("Should be 8.", mask, is((short) 8));
		
		mask = 0;
		mask = FlagUtils.setBitAt(mask, (short) 15);
		assertThat("Should be -‭32760‬.", mask, is(Short.MIN_VALUE));
	}
	
	@Test
	public void setBitTest_Byte() {
		byte mask  = 0;
		mask = FlagUtils.setBitAt(mask, (byte) 0);
		assertThat("Should be 1.", mask, is((byte) 1));
		
		mask = 0;
		mask = FlagUtils.setBitAt(mask, (byte) 3);
		assertThat("Should be 8.", mask, is((byte) 8));
		
		mask = 0;
		mask = FlagUtils.setBitAt(mask, (byte) 7);
		assertThat("Should be -‭128.", mask, is(Byte.MIN_VALUE));
	}
	
	@Test
	public void clearBitTest_Integer() {
		int mask = Integer.MIN_VALUE;
		
		mask = FlagUtils.clearBitAt(mask, 31);
		assertThat(mask, is(0));
		
		mask = 33686528;
		mask = FlagUtils.clearBitAt(mask, 25);
		assertThat(mask, is(132096));
		mask = FlagUtils.clearBitAt(mask, 17);
		assertThat(mask, is(1024));
		mask = FlagUtils.clearBitAt(mask, 10);
		assertThat(mask, is(0));
	}
	
	@Test
	public void clearBitTest_Short() {
		short mask = Short.MIN_VALUE;
		
		mask = FlagUtils.clearBitAt(mask, (short) 15);
		assertThat(mask, is((short) 0));
		
		mask = 544;
		mask = FlagUtils.clearBitAt(mask, (short) 9);
		assertThat(mask, is((short) 32));
		mask = FlagUtils.clearBitAt(mask, (short) 5);
		assertThat(mask, is((short) 0));
	}
	
	@Test
	public void clearBitTest_Byte() {
		byte mask = Byte.MIN_VALUE;
		
		mask = FlagUtils.clearBitAt(mask, (byte) 7);
		assertThat(mask, is((byte) 0));
		
		mask = 36;
		mask = FlagUtils.clearBitAt(mask, (byte) 5);
		assertThat(mask, is((byte) 4));
		mask = FlagUtils.clearBitAt(mask, (byte) 2);
		assertThat(mask, is((byte) 0));
	}
	
	@Test
	public void bitSetTest_Integer() {
		final int mask = 67108900;
		
		assertThat(FlagUtils.isBitSetAt(mask, 26), is(true));
		assertThat(FlagUtils.isBitSetAt(mask, 5), is(true));
		assertThat(FlagUtils.isBitSetAt(mask, 2), is(true));
		assertThat(FlagUtils.isBitSetAt(mask, 22), is(false));
	}
	
	@Test
	public void bitSetTest_Short() {
		final short mask = 16656;
		
		assertThat(FlagUtils.isBitSetAt(mask, (short) 14), is(true));
		assertThat(FlagUtils.isBitSetAt(mask, (short) 8), is(true));
		assertThat(FlagUtils.isBitSetAt(mask, (short) 4), is(true));
		assertThat(FlagUtils.isBitSetAt(mask, (short) 12), is(false));
	}
	
	@Test
	public void bitSetTest_Byte() {
		final short mask = 82;
		
		assertThat(FlagUtils.isBitSetAt(mask, (byte) 6), is(true));
		assertThat(FlagUtils.isBitSetAt(mask, (byte) 4), is(true));
		assertThat(FlagUtils.isBitSetAt(mask, (byte) 1), is(true));
		assertThat(FlagUtils.isBitSetAt(mask, (byte) 3), is(false));
	}
	
	@Test
	public void getByteOf_Test() {
		// 0010 0010 | 0010 0010 = 8738
		final short value = 8738;
		
		// Should be 0010 0010 = 34
		final byte value0 = FlagUtils.getByteOf(value, 0);
		assertThat(value0, is((byte) 34));
		
		// The same like above
		final byte value1 = FlagUtils.getByteOf(value, 1);
		assertThat(value1, is((byte) 34));
	}
	
	@Test
	public void getByteOf_Test2() {
		// 0010 0010 | 1110 0010 = 8930
		final short value = 8930;
		
		// Should be 1110 0010 = -30
		final byte value0 = FlagUtils.getByteOf(value, 0);
		assertThat(value0, is((byte) -30));
		
		// Should be 0010 0010 = 34
		final byte value1 = FlagUtils.getByteOf(value, 1);
		assertThat(value1, is((byte) 34));
	}
	
	@Test
	public void makeShortFrom_Test() {
		final byte byte0 = 50;	// 0011 0010
		final byte byte1 = 35;	// 0010 0011
		
		final short result = FlagUtils.makeShortFrom(byte0, byte1);
		
		assertThat("Should be 9010.", result, is((short) 9010));
	}
	
	@Test
	public void makeShortFrom_Test2() {
		final byte byte0 = -77;	// 1011 0011
		final byte byte1 = -30;	// 1110 0010
		
		final short result = FlagUtils.makeShortFrom(byte0, byte1);
		
		assertThat("Should be -7501.", result, is((short) -7501));
	}
}