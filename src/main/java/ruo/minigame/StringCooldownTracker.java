package ruo.minigame;

import com.google.common.collect.Maps;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class StringCooldownTracker
{
    private final Map<String, StringCooldownTracker.Cooldown> cooldowns = Maps.<String, StringCooldownTracker.Cooldown>newHashMap();
    private int ticks;

    public boolean hasCooldown(String itemIn)
    {
        return this.getCooldown(itemIn, 0.0F) > 0.0F;
    }

    public float getCooldown(String itemIn, float partialTicks)
    {
        StringCooldownTracker.Cooldown cooldowntracker$cooldown = (StringCooldownTracker.Cooldown)this.cooldowns.get(itemIn);

        if (cooldowntracker$cooldown != null)
        {
            float f = (float)(cooldowntracker$cooldown.expireTicks - cooldowntracker$cooldown.createTicks);
            float f1 = (float)cooldowntracker$cooldown.expireTicks - ((float)this.ticks + partialTicks);
            return MathHelper.clamp_float(f1 / f, 0.0F, 1.0F);
        }
        else
        {
            return 0.0F;
        }
    }

    public void tick()
    {
        ++this.ticks;

        if (!this.cooldowns.isEmpty())
        {
            Iterator<Entry<String, StringCooldownTracker.Cooldown>> iterator = this.cooldowns.entrySet().iterator();

            while (iterator.hasNext())
            {
                Entry<String, StringCooldownTracker.Cooldown> entry = (Entry)iterator.next();

                if ((entry.getValue()).expireTicks <= this.ticks)
                {
                    iterator.remove();
                    this.notifyOnRemove((String)entry.getKey());
                }
            }
        }
    }

    public void setCooldown(String itemIn, int ticksIn)
    {
        this.cooldowns.put(itemIn, new StringCooldownTracker.Cooldown(this.ticks, this.ticks + ticksIn));
        this.notifyOnSet(itemIn, ticksIn);
    }

    @SideOnly(Side.CLIENT)
    public void removeCooldown(String itemIn)
    {
        this.cooldowns.remove(itemIn);
        this.notifyOnRemove(itemIn);
    }

    protected void notifyOnSet(String itemIn, int ticksIn)
    {
    }


    protected void notifyOnRemove(String itemIn)
    {
    }

    class Cooldown
    {
        final int createTicks;
        final int expireTicks;

        private Cooldown(int createTicksIn, int expireTicksIn)
        {
            this.createTicks = createTicksIn;
            this.expireTicks = expireTicksIn;
        }
    }
}