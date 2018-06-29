public class BlockPos {
    ㅌString name = "cube";

    public int[] from = new int[3];
    public int[] to = new int[3];

    public BlockFaces faces = new BlockFaces();
    public BlockPos(int x, int y, int z){
        from[0] = x;
        from[1] = y;
        from[2] = z;
        to[0] = x;
        to[1] = y;
        to[2] = z;
    }
    public void get() {
        System.out.println(from);

    }


    public void print(){
        faces.print();
    }
    public int getZ() {
        return from[2];
    }

    public int getY() {
        return from[1];
    }

    public int getX() {
        return from[0];
    }
}
