package com.render;

import java.util.Comparator;

import com.badlogic.gdx.math.Vector3;

public class SubchunkDistanceComparator implements Comparator<CachedSubchunk> {

	private Vector3 referencePos = new Vector3();
	private Vector3 subchunkPos = new Vector3();
	
	public void setReferencePosition(Vector3 referencePosition) {
		this.referencePos.set(referencePosition);
	}
	
	private float getSubchunkDistanceSqr(CachedSubchunk subchunk) {
		if(subchunk.isUnused()) {
			return Float.MAX_VALUE;
		}
		
		// Set subchunkpos to (0, 0, 0) in the chunk
		subchunkPos.x = subchunk.chunkPos.getXPosition();
		subchunkPos.y = subchunk.subchunkIdx * RenderSystem.SUBCHUNK_HEIGHT;
		subchunkPos.z = subchunk.chunkPos.getZPosition();

		// Set subchunk pos to center of subchunk
		subchunkPos.add(RenderSystem.SUBCHUNK_HEIGHT * 0.5f);
		
		return referencePos.dst2(subchunkPos);
	}

	@Override
	public int compare(CachedSubchunk o1, CachedSubchunk o2) {
		float dst1 = getSubchunkDistanceSqr(o1);
		float dst2 = getSubchunkDistanceSqr(o2);
		
		if(dst1 < dst2) {
			return -1;
		} else if(dst1 > dst2) {
			return 1;
		} else {
			return 0;
		}
	}

}
