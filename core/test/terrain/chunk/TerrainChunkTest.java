package terrain.chunk;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.terrain.block.BlockType;
import com.terrain.chunk.Chunk;
import com.terrain.chunk.ChunkPosition;
import com.terrain.chunk.TerrainChunk;

import org.hamcrest.CoreMatchers;

public class TerrainChunkTest {

	private TerrainChunk chunk;
	private short[][][] testArray = new short[Chunk.X_MAX][Chunk.Y_MAX][Chunk.Z_MAX];
	
	@Before
	public void setUp() throws Exception {
		chunk = new TerrainChunk(new ChunkPosition(0, 0));
		
		for(short x = 0; x < Chunk.X_MAX; x++) {
			for(short y = 0; y < Chunk.Y_MAX; y++) {
				for(short z = 0; z < Chunk.Z_MAX; z++) {
					byte random = (byte) (Math.random() * 6);
					
					assertThat(true, CoreMatchers.is(random >= 0 && random <= 5));
					
					testArray[x][y][z] = random;
					chunk.setBlockAt(x, y, z, BlockType.fromBlockId(random));
				}
			}
		}
	}
	
	@Test
	public void testBlockTypes() {
		assertThat(BlockType.AIR, CoreMatchers.is(BlockType.fromBlockId(0)));
		assertThat(BlockType.WATER, CoreMatchers.is(BlockType.fromBlockId(1)));
		assertThat(BlockType.DIRT, CoreMatchers.is(BlockType.fromBlockId(2)));
		assertThat(BlockType.STONE, CoreMatchers.is(BlockType.fromBlockId(3)));
		assertThat(BlockType.GRASS, CoreMatchers.is(BlockType.fromBlockId(4)));
		assertThat(BlockType.TNT, CoreMatchers.is(BlockType.fromBlockId(5)));
		
		assertThat(BlockType.AIR.getBlockId(), CoreMatchers.is((byte) 0));
		assertThat(BlockType.WATER.getBlockId(), CoreMatchers.is((byte) 1));
		assertThat(BlockType.DIRT.getBlockId(), CoreMatchers.is((byte) 2));
		assertThat(BlockType.STONE.getBlockId(), CoreMatchers.is((byte) 3));
		assertThat(BlockType.GRASS.getBlockId(), CoreMatchers.is((byte) 4));
		assertThat(BlockType.TNT.getBlockId(), CoreMatchers.is((byte) 5));
	}
	
	@Test
	public void testGetBlockAt() {
		
		for(short x = 0; x < Chunk.X_MAX; x++) {
			for(short y = 0; y < Chunk.Y_MAX; y++) {
				for(short z = 0; z < Chunk.Z_MAX; z++) {
					
					assertThat(testArray[x][y][z], CoreMatchers.is(chunk.getBlockAt(x, y, z)));
				}
			}
		}
		
	}

}
