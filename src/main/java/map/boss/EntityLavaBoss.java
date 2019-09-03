package map.boss;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import olib.api.BlockAPI;
import olib.api.WorldAPI;
import olib.effect.AbstractTick;
import olib.effect.TickRegister;
import olib.map.EntityDefaultNPC;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class EntityLavaBoss extends EntityDefaultNPC {
    private BlockPos pos1 = new BlockPos(1240, 7, 291);
    private BlockPos pos2 = new BlockPos(1249, 7, 279);

    public EntityLavaBoss(World worldIn) {
        super(worldIn);
        isImmuneToFire = true;
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand, @Nullable ItemStack stack) {
        if (hand == EnumHand.MAIN_HAND && isServerWorld()) {
            //outline();
            blockToMagmaBlock();
        }
        return super.processInteract(player, hand, stack);
    }

    public void thorwMagmaBlock() {

    }

    public void outline(boolean remove) {
        int posX1 = pos1.getX();
        int posX2 = pos2.getX();
        int posZ1 = pos1.getZ();
        int posZ2 = pos2.getZ();
        for (int x = pos1.getX(); x < pos2.getX(); x++) {
            BlockPos pos1 = new BlockPos(x, 7, posZ1);
            BlockPos pos2 = new BlockPos(x, 7, posZ2);
            if (remove) {
                worldObj.setBlockToAir(pos1);
                worldObj.setBlockToAir(pos2);
            } else {
                worldObj.setBlockState(pos1, Blocks.STONE.getDefaultState());
                worldObj.setBlockState(pos2, Blocks.STONE.getDefaultState());
            }
            System.out.println("asdf");
        }
        for (int z = pos2.getZ(); z < pos1.getZ(); z++) {
            BlockPos pos1 = new BlockPos(posX1, 7, z);
            BlockPos pos2 = new BlockPos(posX2, 7, z);
            if (remove) {
                worldObj.setBlockToAir(pos1);
                worldObj.setBlockToAir(pos2);
            } else {
                worldObj.setBlockState(pos1, Blocks.STONE.getDefaultState());
                worldObj.setBlockState(pos2, Blocks.STONE.getDefaultState());
            }
            System.out.println("22222222222");
        }
        pos1 = new BlockPos(pos1.getX() - 1, 7, pos1.getZ() - 1);
        pos2 = new BlockPos(pos2.getX() - 1, 7, pos2.getZ() - 1);
    }

    public void blockToMagmaBlock() {
        WorldAPI.blockTick(worldObj, pos1, pos2, new AbstractTick.BlockXYZ() {
            @Override
            public void run(TickEvent.Type type) {
                if (worldObj.rand.nextInt(10) == 0) {
                    worldObj.setBlockState(getPos(), Blocks.REDSTONE_BLOCK.getDefaultState());
                    BlockPos pos = getPos();
                    TickRegister.register(new AbstractTick(60, true) {

                        @Override
                        public boolean isPause() {
                            return super.isPause() || Minecraft.getMinecraft().isGamePaused();
                        }

                        @Override
                        public void run(TickEvent.Type type) {
                            System.out.println("실행됨" + absRunCount + pos.getX() + " - " + pos.getY() + " - " + pos.getZ());
                            if (absRunCount == 0)
                                worldObj.setBlockState(pos, Blocks.MAGMA.getDefaultState());
                            else {
                                worldObj.setBlockState(pos, Blocks.STONE.getDefaultState());
                                absLoop = false;
                            }

                        }
                    });
                }
            }
        });
    }


}
