package ruo.cmplus.cm.v18.function;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import ruo.cmplus.util.CommandPlusBase;

import java.util.List;

public class CommandEvent extends CommandPlusBase {

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		
		
		if(args[1].equals("edit")) {
			FunctionEvent.edit(args[0]);
		}
		FunctionEvent.add(args[0], t.getCommand(args, 1, args.length));
	}
	
	@Override
	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos pos) {
		
		if(args.length <= 0)
			return getListOfStringsMatchingLastWord(args, "좌클릭,우클릭,차원이동,일어남,아이템파괴,경험치획득,리스폰,채팅,스샷,소리재생,명령어"
					+ ",포션마심,엔티티스폰,라이딩,번개맞음,아이템버림,엔더텔포,공격,죽음,추락,회복,카트충돌,카트클릭,업적,양동이채움"
					+ ",아이템주음,낫,아이템버림,아이템조합".split(","));

		if(args[0].equals("좌클릭") || args[0].equals("우클릭")){
			return splayer(args, "@블럭X", "@블럭Y", "@블럭Z");
		}
		if(args[0].equals("차원이동")){
			return splayer(args, "@차원");
		}
		if(args[0].equals("일어남")){
			return splayer(args);
		}
		if(args[0].equals("아이템파괴")){
			return splayer(args,"@이벤트아이템");
		}
		if(args[0].equals("경험치획득")){
			return splayer(args,"@경험치");
		}
		if(args[0].equals("리스폰")){
			return splayer(args);
		}
		if(args[0].equals("채팅")){
			return splayer(args,"@채팅");
		}
		if(args[0].equals("스샷")){
			return splayer(args,"@파일()");
		}
		if(args[0].equals("소리재생")){
			return splayer(args,"@이름");
		}
		if(args[0].equals("명령어")){
			return splayer(args,"이름");
		}
		if(args[0].equals("포션마심")){
			return splayer(args);
		}
		if(args[0].equals("엔티티스폰")){
			return splayer(args, "@엔티티");
		}
		if(args[0].equals("라이딩")){
			return splayer(args, "@엔티티");
		}
		if(args[0].equals("번개맞음")){
			return splayer(args, "@엔티티");
		}
		if(args[0].equals("아이템버림")){
			return splayer(args);
		}
		if(args[0].equals("엔더텔포")){
			return splayer(args);
		}
		if(args[0].equals("공격")){
			return splayer(args,"@공격", "@피해");
		}
		if(args[0].equals("죽음")){
			return splayer(args,"@공격", "@피해");
		}
		if(args[0].equals("추락")){
			return splayer(args);
		}
		if(args[0].equals("회복")){
			return splayer(args,"@회복량");
		}
		if(args[0].equals("카트충돌")){
			return splayer(args,"@카트","@이벤트엔티티");
		}
		if(args[0].equals("카트클릭")){
			return splayer(args,"@카트");
		}
		if(args[0].equals("업적")){
			return splayer(args,"@이름");
		}
		if(args[0].equals("양동이채움")){
			return splayer(args);
		}
		if(args[0].equals("아이템주움")){
			return splayer(args, "@이벤트아이템");
		}
		if(args[0].equals("낫")){
			return splayer(args);
		}
		if(args[0].equals("아이템조합")){
			return splayer(args, "@이벤트아이템");
		}
		
		if(args[0].equals("아이템태움")){
			return splayer(args, "@이벤트아이템");
		}
		
		
		return super.getTabCompletionOptions(server, sender, args, pos);

	}
	public List<String> splayer(String[] args, String...strings){
		String str = "@취소,@플레이어,@아이템,@X,@Y,@Z,@체력,@이름,";
		for(String string : strings){
			str = str+string;
		}
		return getListOfStringsMatchingLastWord(args, str.split("."));
		
	}
}
