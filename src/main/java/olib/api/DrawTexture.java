package olib.api;

import net.minecraft.util.ResourceLocation;

import java.util.HashMap;

public class DrawTexture {
    private ResourceLocation texture;
    private double x, y, z, width, height;
    private float red, green, blue, alpha;
    public DrawTexture(ResourceLocation texture, double x, double y, double z, double width, double height, float red, float green, float blue, float alpha){
        this.texture = (texture);
        this.x = x;
        this.y = y;
        this.z = z;
        this.width = width;
        this.height = height;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public ResourceLocation getTexture() {
        return texture;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public float getRed() {
        return red;
    }

    public float getGreen() {
        return green;
    }

    public float getBlue() {
        return blue;
    }

    public float getAlpha() {
        return alpha;
    }

    public static class Builder{
        private static HashMap<String, ResourceLocation> resourceMap = new HashMap<>();
        private ResourceLocation texture;
        private double x, y, z, width, height;
        private float red=1, green=1, blue=1, alpha=1;

        public Builder setTexture(String texture){
            if(resourceMap.containsKey(texture))
                this.texture =resourceMap.get(texture);
            else {
                resourceMap.put(texture, new ResourceLocation(texture));
                this.texture = resourceMap.get(texture);
            }
            return this;
        }
        public Builder setTexture(ResourceLocation texture) {
            this.texture = texture;
            return this;
        }
        public Builder setXY(double x, double y){
            this.x = x;
            this.y = y;
            return this;
        }
        public Builder setXYAndSize(double x, double y, double width, double height){
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            return this;
        }
        public Builder setZ(double z){
            this.z = z;
            return this;
        }

        public Builder setAlpha(float alpha){
            this.alpha = alpha;
            return this;
        }

        public Builder setSize(double width, double height){
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder setRGB(float red, float green, float blue){
            this.red = red;
            this.green = green;
            this.blue = blue;
            return this;
        }
        public DrawTexture build(){
            return new DrawTexture(texture, x,y,z,width,height,red,green,blue,alpha);
        }
    }

}
