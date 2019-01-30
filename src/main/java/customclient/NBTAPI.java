package customclient;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.io.*;

public class NBTAPI {
    private NBTTagCompound nbttagcompound = new NBTTagCompound();
    private File file;

    public  NBTAPI(String f) {
        file = new File(f);
        try {
            if (!file.exists()) {
                file.createNewFile();
                saveNBT();
            }else
                readNBT();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public NBTTagCompound getNBT() {
        return this.nbttagcompound;
    }
    public void resetNBT(){
        nbttagcompound = new NBTTagCompound();
    }

    public void saveNBT() {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            CompressedStreamTools.writeCompressed(nbttagcompound, fos);
            fos.close();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public void readNBT() {
        try {
            ResourceLocation loc = new ResourceLocation("customclient:mainmenu.dat");
            InputStream in = Minecraft.getMinecraft().getResourceManager().getResource(loc).getInputStream();
            FileInputStream fileinputstream = new FileInputStream(file);
            nbttagcompound = CompressedStreamTools.readCompressed(in);
            fileinputstream.close();
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
