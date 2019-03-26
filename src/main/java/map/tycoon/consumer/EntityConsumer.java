package map.tycoon.consumer;

import cmplus.cm.CommandChat;
import oneline.api.EntityAPI;
import oneline.api.WorldAPI;
import oneline.effect.AbstractTick;
import oneline.effect.AbstractTick.BlockXYZ;
import oneline.effect.Move;
import oneline.effect.TextEffect;
import oneline.effect.TickRegister;
import oneline.map.EntityDefaultNPC;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;
import map.tycoon.BreadData;
import map.tycoon.BreadDataComparator;
import map.tycoon.TyconHelper;
import map.tycoon.block.TileBreadDisplay;

import java.util.ArrayList;
import java.util.Collections;

public class EntityConsumer extends EntityDefaultNPC {
    private boolean isNoWallet, lackWallet;//lackWallet =  지갑을 들고 왔지만 돈이 부족함
    public ArrayList<BreadData> breadList = new ArrayList<>();
    private BlockPos currentTable = null;
    private ArrayList<BlockPos> posList = new ArrayList<>();
    private BlockPos breadShow, breadCon;
    private int breadIndex;

    public EntityConsumer(World worldIn) {
        super(worldIn);
        this.setRotate(0, 180, 0);
        setTexture(getPlayerSkin(true));
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
        if (worldObj.rand.nextInt(10) == 0) {
            setRider();//돼지나 말을 타고 다님
        }
        if (worldObj.rand.nextInt(70) == 0) {
            isNoWallet = true;
        }
        findBread();
        startShoping();
        return super.onInitialSpawn(difficulty, livingdata);
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        if (!(entityIn instanceof EntityConsumer)) {
            super.collideWithEntity(entityIn);
        }
    }

    public void startShoping() {
        if (isServerWorld()) {
            EntityAPI.move(new Move(this, TyconHelper.getTyconDoorPos(), false) {
                @Override
                public void complete() {
                    if (movecount == 1) {
                        System.out.println("빵이 없어 돌아감");
                        setDead();
                        return;
                    }
                    TyconHelper.spawnDust(worldObj);
                    findBread();
                    EntityAPI.pauseMove(mob, true, 20);
                    if (posList.size() == 0) {
                        setPosition(getSpawnX(), getSpawnY(), getSpawnZ());
                    }
                }
            }.setDistance(2));
            breadShow = posList.get(0);
            breadCon = posList.get(0).add(1, 0, 0);
            moveBread(this);
        }
    }

    private void findBread() {
        posList.clear();
        WorldAPI.blockTick(worldObj, posX, posY, posZ, 15, new BlockXYZ() {
            @Override
            public void run(Type type) {
                TileEntity tileEntity = worldObj.getTileEntity(getPos());
                if (tileEntity instanceof TileBreadDisplay) {
                    TileBreadDisplay tbd = (TileBreadDisplay) tileEntity;
                    if (tbd.getBread() == null) {
                        return;
                    }
                    posList.add(getPos().add(-1, 0, 0));
                }
            }
        });
        Collections.sort(posList, new BreadDataComparator());
        System.out.println("발견한 빵블럭의 수" + posList.size());
    }

    /**
     * 손님이 빵블럭을 구경하게 움직이게 함
     */
    private void moveBread(EntityConsumer consumer) {
        if (posList.size() <= breadIndex) {
            System.out.println("빵 블럭을 모두 돌아봄");
            TyconHelper.addWaitConsumer(consumer);
            moveBreadSell(consumer);
            return;
        }
        TileBreadDisplay bread = (TileBreadDisplay) TyconHelper.getTileEntity(breadShow);
        TickRegister.register(new AbstractTick(10, true) {

            @Override
            public void run(Type type) {
                if (!bread.canConsumerUse()) {
                    System.out.println("빵 블럭이 사용중입니다.");
                    return;
                } else {
                    absLoop = false;
                    bread.setConsumerUse(true);
                    System.out.println("빵을 확인하러 이동합니다.");
                    EntityAPI.move(new Move(consumer, breadCon, false) {
                        @Override
                        public void complete() {
                            checkBread(consumer);
                        }
                    });
                }
            }
        });

    }

    /**
     * 빵이 있는 위치에 도달하면 빵을 확인함 2분의 1의 확률로 빵을 담고 다음 장소로 이동함
     */
    private void checkBread(EntityConsumer con) {
        EntityAPI.look(con, breadShow);// 빵의 위치
        System.out.println("빵을 확인하는 중 breadcount:" + breadIndex);

        TickRegister.register(new AbstractTick(40, false) {
            @Override
            public void run(Type type) {
                TileBreadDisplay bread = (TileBreadDisplay) TyconHelper
                        .getTileEntity(breadShow);
                ItemStack item = bread.getItem();
                System.out.println("tickcount: " + absRunCount + " 아이템 이름:" + item.getDisplayName() + "아이템을 확인함");
                if (con.worldObj.rand.nextBoolean() && bread.canDisplayUse()) {
                    con.swingArm();
                    con.breadList.add(bread.splitBread(WorldAPI.minRand2(1, 10)));
                    con.setHeldItem(EnumHand.MAIN_HAND, bread.getItem());
                    System.out.println(item.getDisplayName() + "을 챙겼음" + breadIndex);
                    if (worldObj.rand.nextInt(10) == 0)
                        TyconHelper.dustList.add(breadShow);
                }
                bread.setConsumerUse(false);
                breadIndex++;
                breadCon = posList.get(breadIndex).add(1, 0, 0);
                breadShow = posList.get(breadIndex);
                moveBread(con);
            }
        });
    }

    /**
     * 빵을 점원에게 이동함
     */
    public void moveBreadSell(EntityConsumer consumer) {
        TyconHelper.setSellerPos(this);
        System.out.println("빵을 점원에게 갖고 가는 중 ");
        EntityAPI.move(new Move(consumer, TyconHelper.calcX,
                TyconHelper.calcY, TyconHelper.calcZ - 2 - TyconHelper.waitConsumerIndexOf(consumer), false) {// 계산대로 이동함
            @Override
            public void complete() {
                System.out.println("빵을 사러 점원에게 왔음");
                System.out.println("이 고객은 " + TyconHelper.waitConsumerIndexOf(consumer) + "번째에 서있음");
                if (WorldAPI.getPlayer().getDistance(posX, posY, posZ) < 5)
                    sellBread(consumer);
            }
        });
    }

    private void sellBread(EntityConsumer consumer) {
        EntityAPI.look(consumer, WorldAPI.getPlayer());
        TickRegister.register(new AbstractTick(60, true) {
            int findPlayer = 0;

            public void run(Type type) {
                System.out.println("점원을 기다리는 중 플레이어와 거리:" + WorldAPI.getPlayer().getDistance(posX, posY, posZ));
                if (WorldAPI.getPlayer().getDistance(posX, posY, posZ) < 5) {
                    if (findPlayer == 0) {
                        consumer.cancel();
                        TextEffect.getPlayerHelper().addChat(0, "빵의 개수는 " + TyconHelper.calcBreadAmount(consumer)
                                + "이고 " + TyconHelper.calcBread(consumer) + "원.");
                        absDefTick = 20;
                        findPlayer++;

                    }
                    if (findPlayer == 1) {
                        String text = CommandChat.getChatLine(0);
                        if (text.indexOf("원") != -1) {
                            String va = text.split("원")[0];
                            int money = Integer.valueOf(va);
                            CommandChat.deleteChatLine(0);
                            if (money > TyconHelper.calcBreadAmount(consumer) + 1000) {
                                TextEffect.getPlayerHelper().addChat(0, "너무 비쌈");
                            }
                            if (lackWallet) {
                                boolean lackWallet2 = false;//빵을 두개 이상 들고 있는지
                                int breadAllAmount = 0;//들고있는 빵의 갯수
                                for(BreadData breadData : breadList){
                                    breadAllAmount += breadData.getAmount();
                                    if(breadData.getAmount() >= 2) {
                                        lackWallet2 = true;
                                    }
                                }
                                if(lackWallet2 || breadAllAmount > 2) {
                                    TextEffect.getPlayerHelper().addChat(0, "돈을 덜 들고 옴");
                                    int subBread = worldObj.rand.nextInt(breadAllAmount - 1);//빵 뺄 개수
                                    for(BreadData breadData : breadList){
                                        if(0 >= subBread)
                                            break;
                                        if(breadData.getAmount() > subBread){
                                            breadData.subAmount(subBread);
                                            break;
                                        }else{
                                            int subRandom = worldObj.rand.nextInt(breadData.getAmount() - 1);
                                            breadData.subAmount(subRandom);
                                            breadAllAmount -= subRandom;
                                            subBread -= subRandom;
                                        }
                                    }
                                }
                            }
                            if (isNoWallet) {
                                TextEffect.getPlayerHelper().addChat(0, "지갑을 안 갖고 옴");
                            } else
                                TyconHelper.playermoney += money;
                        }
                        else{
                            absDefTick = 20;
                            if(absRunCount == 5) {
                            }
                            if(absRunCount == 10) {
                            }
                            if(absRunCount == 15) {
                            }
                            if(absRunCount == 20) {
                            }
                            return;

                        }
                        absLoop = false;
                        endShoping();
                    }
                }
            }
        });
    }


    private void endShoping() {
        TyconHelper.subWaitConsumer(this);
        //moveTable();
        outBreadTycon();
    }

    private void moveTable() {
        currentTable = TyconHelper.findEmptyTable();
        EntityAPI.move(new Move(this, currentTable, false) {
            @Override
            public void complete() {
                TickRegister.register(new AbstractTick(200, false) {
                    @Override
                    public void run(Type type) {
                        outBreadTycon();
                    }
                });
            }
        });
    }

    private void outBreadTycon() {
        EntityConsumer con = this;
        EntityAPI.move(new Move(con, TyconHelper.getTyconDoorPos(), false) {
            @Override
            public void complete() {
                con.setDead();
            }
        }.setDistance(3));
        TyconHelper.removeTable(currentTable);
        System.out.println("10초가 지나서 밖으로 나감");

    }


    private EntityConsumer setRider() {
        switch (worldObj.rand.nextInt(10)) {
            case 0:
                EntityPig pig = new EntityPig(worldObj);
                pig.setPosition(posX, posY, posZ);
                worldObj.spawnEntityInWorld(pig);
                pig.setSaddled(true);
                pig.startRiding(this);

            case 1:
                EntityHorse horse = new EntityHorse(worldObj);
                horse.setPosition(posX, posY, posZ);
                worldObj.spawnEntityInWorld(horse);
                horse.setHorseSaddled(true);
                horse.startRiding(this);
            case 2:
                EntityChildConsumer childConsumer = new EntityChildConsumer(worldObj);
                childConsumer.setPosition(posX, posY, posZ);
                worldObj.spawnEntityInWorld(childConsumer);
                childConsumer.startRiding(this);

            default:
                return this;
        }
    }
}
