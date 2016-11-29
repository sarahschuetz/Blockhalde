package com.terrain;

public class ChunkPosition{

    //as a chunk is covering the whole height of the game world, only 2d coordinates are required
    private int xPosition;
    private int zPosition;

    public ChunkPosition(int xPosition, int zPosition){
        this.xPosition = xPosition;
        this.zPosition = zPosition;
    }

    public int getXPosition(){
        return xPosition;
    }

    public int getZPosition(){
        return zPosition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChunkPosition that = (ChunkPosition) o;

        if (xPosition != that.xPosition) return false;
        return zPosition == that.zPosition;

    }

    @Override
    public int hashCode() {
        int result = xPosition;
        result = 31 * result + zPosition;
        return result;
    }
  }
