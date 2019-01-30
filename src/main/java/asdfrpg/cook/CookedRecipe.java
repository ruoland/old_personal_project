package ruo.asdfrpg.cook;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CookedRecipe {
    private List<ItemStack> itemList = new ArrayList<>();
    private ItemStack value;
    public CookedRecipe(ItemStack value, ItemStack... stack) {
        for (ItemStack itemStack : stack)
            itemList.add(itemStack);
        this.value = value;
    }

    public boolean checkRecipe(List<ItemStack> list) {
        for(int i = 0;i<list.size();i++){
            System.out.println("2222 "+itemList.get(i).getItem()+" - "+list.get(i).getItem()+" - "+itemList.size() + " - "+i);

            if(itemList.get(i).getItem() == list.get(i).getItem()) {
                System.out.println("3333 "+itemList.get(i).getItem()+" - "+list.get(i).getItem()+" - "+itemList.size() + " - "+i);
                if (itemList.size() - 1 == i) {
                    System.out.println("4444"+true);
                    return true;
                }
                continue;
            }
            else
                break;
        }
        return false;
    }

    public List<ItemStack> getItemList() {
        return itemList;
    }

    public ItemStack getValue() {
        return value.copy();
    }
}
