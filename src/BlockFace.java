public class BlockFace {
    private int[] uv = new int[4];

    String texture = "#missing";

    public BlockFace(int x, int y, int z, int z2){
        uv[0] = x;
        uv[1] = y;
        uv[2] = z;
        uv[3] = z2;

    }

    public int getZ() {
        return uv[2];
    }

    public int getY() {
        return uv[1];
    }

    public int getX() {
        return uv[0];
    }
}
