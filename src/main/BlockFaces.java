public class BlockFaces {
    private BlockFace north = new BlockFace(0,0,1,1);
    private BlockFace east = new BlockFace(0,0,1,1);
    private BlockFace south = new BlockFace(0,0,1,1);
    private BlockFace west = new BlockFace(0,0,1,1);
    private BlockFace up = new BlockFace(0,0,1,1);
    private BlockFace down = new BlockFace(0,0,1,1);

    public BlockFace getEast() {
        return east;
    }

    public BlockFace getNorth() {
        return north;
    }

    public BlockFace getSouth() {
        return south;
    }

    public BlockFace getDown() {
        return down;
    }

    public BlockFace getUp() {
        return up;
    }

    public BlockFace getWest() {
        return west;
    }


    public void print(){
        north.print("north");
        south.print("south");
        down.print("down");
        up.print("up");
        west.print("west");
    }
}

