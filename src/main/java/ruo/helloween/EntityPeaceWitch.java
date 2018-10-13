package ruo.helloween;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.ClickEvent.Action;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.effect.AbstractTick;
import ruo.minigame.effect.TextEffect;
import ruo.minigame.effect.TickRegister;

public class EntityPeaceWitch extends EntityWitch {
	public static long startTime;
	TextEffect effect;
	
	public EntityPeaceWitch(World worldIn) {
		super(worldIn);
		
	}
    protected void initEntityAI()
    {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackRanged(this, 1.0D, 60, 10.0F));
        this.tasks.addTask(2, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(3, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
    }
    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
    	if(isServerWorld()) {
    	}
    	
    	return super.attackEntityFrom(source, amount);
    }
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
		this.setCustomNameTag("마아아연");
		effect = TextEffect.getHelper(this);
		TickRegister.register(new AbstractTick(40, true) {
			
			@Override
			public void run(Type type) {
				long endTime = EntityWeen.endTime;
				long startTime = EntityWeen.startTime;
				long se = endTime - startTime;
		 		long sec = se / (1000);
		 		long minute = sec/60;
		 		long second = sec-sec/60*60;
				
				if(absRunCount == 0)
					effect.addChat(0, "안녕 "+getName(1));
				if(absRunCount == 1)
					effect.addChat(0, "내가 만든 호박을 죽이는데 걸린 시간은 총 "+(EntityWeen.endTime - EntityWeen.startTime) / 1000+"초야.");
				if(absRunCount == 2)
					effect.addChat(0, "분으로 바꾸면 "+minute +"분 " +second+"초.");
				if(absRunCount == 3)
					effect.addChat(0, "이제 더 준비한 건 없어. 더 준비하고 싶었지만 할로윈 이벤트가 2주동안 진행하거든. 아무튼 플레이 해줘서 고마워.");
				if(absRunCount == 4)
					effect.addChat(0, ".....");
				if(absRunCount == 5)
					effect.addChat(0, "하지만 옛날에 만든 컨텐츠를 소개해줄 수는 있지.");
				if(absRunCount == 6) {
					ITextComponent text = new TextComponentString("내가 한달 전에 만든 모드 스토리 맵이 있어. 한번 해봐.");
					WorldAPI.getPlayer().addChatComponentMessage(text);
				}
				if(absRunCount == 7) {
					Style style = new Style();
					style.setClickEvent(new ClickEvent(Action.OPEN_URL, "http://cafe.naver.com/minecraftgame/1600031"));
					ITextComponent text = new TextComponentString("[사이트 열기 마우스로 클릭]").setStyle(style);
					WorldAPI.getPlayer().addChatComponentMessage(text);
				}
				if(absRunCount == 8) {
				}
				if(absRunCount == 13) {
				}
				if(absRunCount == 14) {
					Style style = new Style();
					style.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/elytra start 0 아아아가아강각가아아아가각가각"));
					ITextComponent text = new TextComponentString("[해상도가 FHD(1080p) 이상이라면 여기를 클릭]").setStyle(style);
					WorldAPI.getPlayer().addChatComponentMessage(text);
					Style style1 = new Style();
					style1.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/elytra start 0"));
					ITextComponent text1 = new TextComponentString("[해상도가 FHD인지 모르겠다면 이걸 클릭]").setStyle(style1);
					WorldAPI.getPlayer().addChatComponentMessage(text1);
				}
			}
				
		});
    	return super.onInitialSpawn(difficulty, livingdata);
    }
    public static String getName(int a) {
    	String pn = WorldAPI.getPlayer().getName();
    	if(pn.equals("D0XA"))
    		pn = "독사";
    	if(pn.equals("bjacau"))
    		pn = "악어";
    	if(pn.equals("Jungryeok"))
    		pn = "중력";
    	if(pn.equals("P_PingMan"))
    		pn = "핑맨";
    	if(pn.equals("ms213"))
    		pn = "멋사";
    	if(pn.equals("dog5658"))
    		pn = "개리";
    	if(pn.equals("EssTree"))
    		pn = "만득";
    	if(pn.equals("Nubul_"))
    		pn = "너불";
    	if(pn.equals("Su_Ning424"))
    		pn = "수닝";
    	if(pn.equals("Nubul_"))
    		pn = "너불";
    	if(pn.equals("EssTree"))
    		pn = "만득";
    	if(pn.equals("_LITA"))
    		pn = "리이타";
    	if(pn.equals("dotri"))
    		pn = "도트리";
    	if(pn.equals("Dalza"))
    		pn = "달자";
    	if(pn.equals("noix_p"))
    		pn = "느피";
    	if(pn.equals("flordelluviaa"))
    		pn = "귤우유";
    	if(pn.equals("cherang"))
    		pn = "체랑";
    	if(pn.equals("KimNamSoon"))
    		pn = "김남순";
    	if(pn.equals("Dol"))
    		pn = "짱돌";
    	if(pn.equals("konggal"))
    		pn = "공갈";
    	if(pn.equals("gosxo"))
    		pn = "태경";
    	if(pn.equals("DDotty"))
    		pn = "도티";
    	if(pn.equals("_MAYBE"))
    		pn = "메이비";
    	if(pn.equals("Unhak39"))
    		pn = "운학";
    	if(pn.equals("herb_o"))
    		pn = "쁘띠허브";
    	if(pn.equals("choikevin"))
    		pn = "최케빈";
    	
    	if(pn.equals("IYMAN"))
    		pn = "잉여맨";
    	if(pn.equals("Danmiho"))
    		pn = "단미호";
    	if(pn.equals("bomsoo"))
    		pn = "봄수";
    	if(pn.equals("TeTekee"))
    		pn = "태택이";
    	if(pn.equals("E_SuGun"))
    		pn = "이수건";
    	if(pn.equals("Ohback"))
    		pn = "오박사";
    	if(pn.equals("Ban_gpal"))
    		pn = "쥐늑대";
    	if(pn.equals("YoRoo"))
    		pn = "요루루";
    	if(pn.equals("BJreha"))
    		pn = "김리하";
    	
    	if(pn.equals("gag2327"))
    		pn = "가그";
    	if(pn.equals("FlowerYo"))
    		pn = "꽃요";
    	
    	if(pn.equals("Sleepground"))
    		pn = "잠뜰";
    	if(pn.equals("doldokki"))
    		pn = "돌도끼";
    	if(pn.equals("blackdoves3"))
    		pn = "찬이";
    	if(pn.equals("R_zza"))
    		pn = "알짜";
    	if(pn.equals("mild_"))
    		pn = "마일드";
    	if(pn.equals("Pickyhunter"))
    		pn = "헌터";
    	
    	if(pn.equals("chameleonj"))
    		pn = "카멜레온";
    	if(pn.equals("Kim_gun"))
    		pn = "김군";
    	if(pn.equals("lemong"))
    		pn = "레몽";
    	if(pn.equals("bicon"))
    		pn = "비콘";
    	if(pn.equals("choko1213"))
    		pn = "쪼꼬";
    	if(pn.equals("takju82"))
    		pn = "탁주";
    	if(pn.equals("d7297"))
    		pn = "양띵";
    	
    	if(pn.equals("MinedApple"))
    		pn = "마인애플";
    	if(pn.equals("FGcounter"))
    		pn = "카운터";
    	if(pn.equals("_queentroi"))
    		pn = "퀸토리";
    	if(pn.equals("hoodie"))
    		pn = "후디";
    	
    	if(pn.equals("dusql"))
    		pn = "쵸코";
    	if(pn.equals("FGcounter"))
    		pn = "카운터";
    	if(pn.equals("sopypie"))
    		pn = "소피";
      	if(pn.equals("villager_story"))
    		pn = "주민이야기";
      	if(pn.equals("creategolem"))
    		pn = "바위골렘";
      	
      	if(pn.equals("realwakgood"))
    		pn = "우왁굳";
     	
      	if(pn.equals("R_369"))
    		pn = "369랑께";
      	if(pn.equals("clone_jhs"))
    		pn = "법관 전현수";
      	
      	if(pn.equals("gagnamga912"))
    		pn = "강남자";
      	if(pn.equals("TwoGo_"))
    		pn = "투고";
      	
      	if(pn.equals("polly1203")) {
      		pn = "블루위키";
      	}
     	if(pn.equals("SoulDragon93")) {
      		pn = "소라곤";
      	}
      	return pn ;
    }
    @Override
    public void onLivingUpdate() {
    	
    	super.onLivingUpdate();
    }
}
