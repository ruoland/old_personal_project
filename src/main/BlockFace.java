public class BlockFace {
    private int[] uv = new int[4];

    String texture = "#missing";

    public BlockFace(int x, int y, int z, int z2){
        uv[0] = x;
        uv[1] = y;
        uv[2] = z;
        uv[3] = z2;
    }

    public String getTexture() {
        return texture;
    }

    public void print(String name){
        System.out.println(name+"UV "+uv[0]+uv[1]+uv[2]+uv[3]);
    }

    public int getZ2() {
        return uv[3];
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
