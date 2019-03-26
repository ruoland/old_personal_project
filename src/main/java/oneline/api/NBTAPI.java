package oneline.api;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

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
            if(file.isFile()) {
                FileInputStream fileinputstream = new FileInputStream(file);
                nbttagcompound = CompressedStreamTools.readCompressed(fileinputstream);
                fileinputstream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
