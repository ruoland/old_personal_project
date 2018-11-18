package ruo.minigame.minigame.scroll;

import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import ruo.minigame.map.EntityDefaultNPC;
import ruo.minigame.map.TypeModel;
import scala.xml.dtd.EntityDef;

public class EntityScrollCreeper extends EntityDefaultNPC {
    public EntityScrollCreeper(World worldIn) {
        super(worldIn);
        setModel(TypeModel.CREEPER);
        setCollision(true);
    }


    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {

        if(source.getSourceOfDamage() instanceof EntityArrow){
            EntityArrow arrow = (EntityArrow) source.getSourceOfDamage();
            double headPos = posY + height;
            System.out.println("화살에 맞음"+headPos+" - "+arrow.posY);
            if(headPos > arrow.posY && headPos - 0.7 < arrow.posY){
                setHealth(0);
                System.out.println("머리에 맞음");
            }
        }

        return super.attackEntityFrom(source, amount);

    }
}
