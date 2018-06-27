// ==================================================================
// This file is part of Render Player API.
//
// Render Player API is free software: you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public License
// as published by the Free Software Foundation, either version 3 of
// the License, or (at your option) any later version.
//
// Render Player API is distributed in the hope that it will be
// useful, but WITHOUT ANY WARRANTY; without even the implied
// warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
// See the GNU Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License and the GNU General Public License along with Render
// Player API. If not, see <http://www.gnu.org/licenses/>.
// ==================================================================

package api.player.model;

import org.objectweb.asm.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public final class ModelPlayerArmorClassVisitor extends ClassVisitor
{
	public static final String targetClassName = "api.player.model.ModelPlayerArmor";
	public static final String obfuscatedClassReference = "api/player/model/ModelPlayerArmor";
	public static final String obfuscatedSuperClassReference = "bjp";
	public static final String deobfuscatedClassReference = "api/player/model/ModelPlayerArmor";
	public static final String deobfuscateSuperClassReference = "net/minecraft/client/model/ModelBiped";

	private boolean hadLocalGetArmForSide;
	private boolean hadLocalGetMainHand;
	private boolean hadLocalGetRandomModelBox;
	private boolean hadLocalGetTextureOffset;
	private boolean hadLocalPostRenderArm;
	private boolean hadLocalRender;
	private boolean hadLocalSetInvisible;
	private boolean hadLocalSetLivingAnimations;
	private boolean hadLocalSetModelAttributes;
	private boolean hadLocalSetRotationAngles;
	private boolean hadLocalSetTextureOffset;

	public static byte[] transform(byte[] bytes, boolean isObfuscated)
	{
		try
		{
			ByteArrayInputStream in = new ByteArrayInputStream(bytes);
			ClassReader cr = new ClassReader(in);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			ModelPlayerArmorClassVisitor p = new ModelPlayerArmorClassVisitor(cw, isObfuscated);

			cr.accept(p, 0);

			byte[] result = cw.toByteArray();
			in.close();
			return result;
		}
		catch(IOException ioe)
		{
			throw new RuntimeException(ioe);
		}
	}

	private final boolean isObfuscated;

	public ModelPlayerArmorClassVisitor(ClassVisitor classVisitor, boolean isObfuscated)
	{
		super(262144, classVisitor);
		this.isObfuscated = isObfuscated;
	}

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces)
	{
		if(isObfuscated && superName.equals("net/minecraft/client/model/ModelBiped"))
			superName = "bjp";
		String[] newInterfaces = new String[interfaces.length + 1];
		for(int i=0; i<interfaces.length; i++)
			newInterfaces[i] = interfaces[i];
		newInterfaces[interfaces.length] = "api/player/model/IModelPlayerAPI";
		super.visit(version, access, name, signature, superName, newInterfaces);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
	{
		if(name.equals("<init>") && desc.equals("()V"))
		{
			desc = "(Ljava/lang/String;)V";

			MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", signature, exceptions);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitInsn(Opcodes.ACONST_NULL);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "api/player/model/ModelPlayerArmor", "<init>", desc, false);
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();

			return new ModelPlayerArmorConstructorVisitor(super.visitMethod(access, name, desc, signature, exceptions), isObfuscated, false, 1);
		}

		if(name.equals("<init>") && desc.equals("(F)V"))
		{
			desc = "(FLjava/lang/String;)V";

			MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "(F)V", signature, exceptions);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.FLOAD, 1);
			mv.visitInsn(Opcodes.ACONST_NULL);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "api/player/model/ModelPlayerArmor", "<init>", desc, false);
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();

			return new ModelPlayerArmorConstructorVisitor(super.visitMethod(access, name, desc, signature, exceptions), isObfuscated, false, 2);
		}

		if(name.equals("<init>") && desc.equals("(FFII)V"))
		{
			desc = "(FFIILjava/lang/String;)V";

			MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "(FFII)V", signature, exceptions);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.FLOAD, 1);
			mv.visitVarInsn(Opcodes.FLOAD, 2);
			mv.visitVarInsn(Opcodes.ILOAD, 3);
			mv.visitVarInsn(Opcodes.ILOAD, 4);
			mv.visitInsn(Opcodes.ACONST_NULL);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "api/player/model/ModelPlayerArmor", "<init>", desc, false);
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();

			return new ModelPlayerArmorConstructorVisitor(super.visitMethod(access, name, desc, signature, exceptions), isObfuscated, true, 5);
		}

		if(name.equals(isObfuscated ? "a" : "getArmForSide") && desc.equals(isObfuscated ? "(Lse;)Lblf;" : "(Lnet/minecraft/util/EnumHandSide;)Lnet/minecraft/client/model/ModelRenderer;"))
		{
			hadLocalGetArmForSide = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC, "localGetArmForSide", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "getMainHand") && desc.equals(isObfuscated ? "(Lrw;)Lse;" : "(Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/EnumHandSide;"))
		{
			hadLocalGetMainHand = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC, "localGetMainHand", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "getRandomModelBox") && desc.equals(isObfuscated ? "(Ljava/util/Random;)Lblf;" : "(Ljava/util/Random;)Lnet/minecraft/client/model/ModelRenderer;"))
		{
			hadLocalGetRandomModelBox = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC, "localGetRandomModelBox", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "getTextureOffset") && desc.equals(isObfuscated ? "(Ljava/lang/String;)Lblg;" : "(Ljava/lang/String;)Lnet/minecraft/client/model/TextureOffset;"))
		{
			hadLocalGetTextureOffset = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC, "localGetTextureOffset", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "postRenderArm") && desc.equals(isObfuscated ? "(FLse;)V" : "(FLnet/minecraft/util/EnumHandSide;)V"))
		{
			hadLocalPostRenderArm = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC, "localPostRenderArm", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "render") && desc.equals(isObfuscated ? "(Lrw;FFFFFF)V" : "(Lnet/minecraft/entity/Entity;FFFFFF)V"))
		{
			hadLocalRender = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC, "localRender", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "setInvisible") && desc.equals("(Z)V"))
		{
			hadLocalSetInvisible = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC, "localSetInvisible", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "setLivingAnimations") && desc.equals(isObfuscated ? "(Lsf;FFF)V" : "(Lnet/minecraft/entity/EntityLivingBase;FFF)V"))
		{
			hadLocalSetLivingAnimations = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC, "localSetLivingAnimations", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "setModelAttributes") && desc.equals(isObfuscated ? "(Lbju;)V" : "(Lnet/minecraft/client/model/ModelBase;)V"))
		{
			hadLocalSetModelAttributes = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC, "localSetModelAttributes", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "setRotationAngles") && desc.equals(isObfuscated ? "(FFFFFFLrw;)V" : "(FFFFFFLnet/minecraft/entity/Entity;)V"))
		{
			hadLocalSetRotationAngles = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC, "localSetRotationAngles", desc, signature, exceptions);
		}

		if(name.equals(isObfuscated ? "a" : "setTextureOffset") && desc.equals("(Ljava/lang/String;II)V"))
		{
			hadLocalSetTextureOffset = true;
			return super.visitMethod(Opcodes.ACC_PUBLIC, "localSetTextureOffset", desc, signature, exceptions);
		}

		return super.visitMethod(access, name, desc, signature, exceptions);
	}

	@Override
	public void visitEnd()
	{
		MethodVisitor mv;

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "func_187074_a" : "getArmForSide", "" + (isObfuscated ? "(Lse;)Lblf;" : "(Lnet/minecraft/util/EnumHandSide;)Lnet/minecraft/client/model/ModelRenderer;") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/model/ModelPlayerAPI", "getArmForSide", "(Lapi/player/model/IModelPlayerAPI;Lnet/minecraft/util/EnumHandSide;)" + (isObfuscated ? "Lblf;" : "Lnet/minecraft/client/model/ModelRenderer;") + "", false);
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realGetArmForSide", "(Lnet/minecraft/util/EnumHandSide;)" + (isObfuscated ? "Lblf;" : "Lnet/minecraft/client/model/ModelRenderer;") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "api/player/model/ModelPlayerArmor", isObfuscated ? "func_187074_a" : "getArmForSide", "" + (isObfuscated ? "(Lse;)Lblf;" : "(Lnet/minecraft/util/EnumHandSide;)Lnet/minecraft/client/model/ModelRenderer;") + "", false);
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superGetArmForSide", "(Lnet/minecraft/util/EnumHandSide;)" + (isObfuscated ? "Lblf;" : "Lnet/minecraft/client/model/ModelRenderer;") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bjp" : "net/minecraft/client/model/ModelBiped", isObfuscated ? "func_187074_a" : "getArmForSide", "" + (isObfuscated ? "(Lse;)Lblf;" : "(Lnet/minecraft/util/EnumHandSide;)Lnet/minecraft/client/model/ModelRenderer;") + "", false);
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalGetArmForSide)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC, "localGetArmForSide", "(Lnet/minecraft/util/EnumHandSide;)" + (isObfuscated ? "Lblf;" : "Lnet/minecraft/client/model/ModelRenderer;") + "", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bjp" : "net/minecraft/client/model/ModelBiped", isObfuscated ? "func_187074_a" : "getArmForSide", "" + (isObfuscated ? "(Lse;)Lblf;" : "(Lnet/minecraft/util/EnumHandSide;)Lnet/minecraft/client/model/ModelRenderer;") + "", false);
			mv.visitInsn(Opcodes.ARETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "func_187072_a" : "getMainHand", "" + (isObfuscated ? "(Lrw;)Lse;" : "(Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/EnumHandSide;") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/model/ModelPlayerAPI", "getMainHand", "(Lapi/player/model/IModelPlayerAPI;Lnet/minecraft/entity/Entity;)" + (isObfuscated ? "Lse;" : "Lnet/minecraft/util/EnumHandSide;") + "", false);
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realGetMainHand", "(Lnet/minecraft/entity/Entity;)" + (isObfuscated ? "Lse;" : "Lnet/minecraft/util/EnumHandSide;") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "api/player/model/ModelPlayerArmor", isObfuscated ? "func_187072_a" : "getMainHand", "" + (isObfuscated ? "(Lrw;)Lse;" : "(Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/EnumHandSide;") + "", false);
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superGetMainHand", "(Lnet/minecraft/entity/Entity;)" + (isObfuscated ? "Lse;" : "Lnet/minecraft/util/EnumHandSide;") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bjp" : "net/minecraft/client/model/ModelBiped", isObfuscated ? "func_187072_a" : "getMainHand", "" + (isObfuscated ? "(Lrw;)Lse;" : "(Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/EnumHandSide;") + "", false);
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalGetMainHand)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC, "localGetMainHand", "(Lnet/minecraft/entity/Entity;)" + (isObfuscated ? "Lse;" : "Lnet/minecraft/util/EnumHandSide;") + "", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bjp" : "net/minecraft/client/model/ModelBiped", isObfuscated ? "func_187072_a" : "getMainHand", "" + (isObfuscated ? "(Lrw;)Lse;" : "(Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/EnumHandSide;") + "", false);
			mv.visitInsn(Opcodes.ARETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "func_85181_a" : "getRandomModelBox", "" + (isObfuscated ? "(Ljava/util/Random;)Lblf;" : "(Ljava/util/Random;)Lnet/minecraft/client/model/ModelRenderer;") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/model/ModelPlayerAPI", "getRandomModelBox", "(Lapi/player/model/IModelPlayerAPI;Ljava/util/Random;)" + (isObfuscated ? "Lblf;" : "Lnet/minecraft/client/model/ModelRenderer;") + "", false);
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realGetRandomModelBox", "(Ljava/util/Random;)" + (isObfuscated ? "Lblf;" : "Lnet/minecraft/client/model/ModelRenderer;") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "api/player/model/ModelPlayerArmor", isObfuscated ? "func_85181_a" : "getRandomModelBox", "" + (isObfuscated ? "(Ljava/util/Random;)Lblf;" : "(Ljava/util/Random;)Lnet/minecraft/client/model/ModelRenderer;") + "", false);
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superGetRandomModelBox", "(Ljava/util/Random;)" + (isObfuscated ? "Lblf;" : "Lnet/minecraft/client/model/ModelRenderer;") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bjp" : "net/minecraft/client/model/ModelBiped", isObfuscated ? "func_85181_a" : "getRandomModelBox", "" + (isObfuscated ? "(Ljava/util/Random;)Lblf;" : "(Ljava/util/Random;)Lnet/minecraft/client/model/ModelRenderer;") + "", false);
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalGetRandomModelBox)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC, "localGetRandomModelBox", "(Ljava/util/Random;)" + (isObfuscated ? "Lblf;" : "Lnet/minecraft/client/model/ModelRenderer;") + "", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bjp" : "net/minecraft/client/model/ModelBiped", isObfuscated ? "func_85181_a" : "getRandomModelBox", "" + (isObfuscated ? "(Ljava/util/Random;)Lblf;" : "(Ljava/util/Random;)Lnet/minecraft/client/model/ModelRenderer;") + "", false);
			mv.visitInsn(Opcodes.ARETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "func_78084_a" : "getTextureOffset", "" + (isObfuscated ? "(Ljava/lang/String;)Lblg;" : "(Ljava/lang/String;)Lnet/minecraft/client/model/TextureOffset;") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/model/ModelPlayerAPI", "getTextureOffset", "(Lapi/player/model/IModelPlayerAPI;Ljava/lang/String;)" + (isObfuscated ? "Lblg;" : "Lnet/minecraft/client/model/TextureOffset;") + "", false);
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realGetTextureOffset", "(Ljava/lang/String;)" + (isObfuscated ? "Lblg;" : "Lnet/minecraft/client/model/TextureOffset;") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "api/player/model/ModelPlayerArmor", isObfuscated ? "func_78084_a" : "getTextureOffset", "" + (isObfuscated ? "(Ljava/lang/String;)Lblg;" : "(Ljava/lang/String;)Lnet/minecraft/client/model/TextureOffset;") + "", false);
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superGetTextureOffset", "(Ljava/lang/String;)" + (isObfuscated ? "Lblg;" : "Lnet/minecraft/client/model/TextureOffset;") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bjp" : "net/minecraft/client/model/ModelBiped", isObfuscated ? "func_78084_a" : "getTextureOffset", "" + (isObfuscated ? "(Ljava/lang/String;)Lblg;" : "(Ljava/lang/String;)Lnet/minecraft/client/model/TextureOffset;") + "", false);
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalGetTextureOffset)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC, "localGetTextureOffset", "(Ljava/lang/String;)" + (isObfuscated ? "Lblg;" : "Lnet/minecraft/client/model/TextureOffset;") + "", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bjp" : "net/minecraft/client/model/ModelBiped", isObfuscated ? "func_78084_a" : "getTextureOffset", "" + (isObfuscated ? "(Ljava/lang/String;)Lblg;" : "(Ljava/lang/String;)Lnet/minecraft/client/model/TextureOffset;") + "", false);
			mv.visitInsn(Opcodes.ARETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "func_187073_a" : "postRenderArm", "" + (isObfuscated ? "(FLse;)V" : "(FLnet/minecraft/util/EnumHandSide;)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitVarInsn(Opcodes.ALOAD, 2);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/model/ModelPlayerAPI", "postRenderArm", "(Lapi/player/model/IModelPlayerAPI;FLnet/minecraft/util/EnumHandSide;)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realPostRenderArm", "(FLnet/minecraft/util/EnumHandSide;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitVarInsn(Opcodes.ALOAD, 2);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "api/player/model/ModelPlayerArmor", isObfuscated ? "func_187073_a" : "postRenderArm", "" + (isObfuscated ? "(FLse;)V" : "(FLnet/minecraft/util/EnumHandSide;)V") + "", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superPostRenderArm", "(FLnet/minecraft/util/EnumHandSide;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitVarInsn(Opcodes.ALOAD, 2);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bjp" : "net/minecraft/client/model/ModelBiped", isObfuscated ? "func_187073_a" : "postRenderArm", "" + (isObfuscated ? "(FLse;)V" : "(FLnet/minecraft/util/EnumHandSide;)V") + "", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalPostRenderArm)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC, "localPostRenderArm", "(FLnet/minecraft/util/EnumHandSide;)V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.FLOAD, 1);
			mv.visitVarInsn(Opcodes.ALOAD, 2);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bjp" : "net/minecraft/client/model/ModelBiped", isObfuscated ? "func_187073_a" : "postRenderArm", "" + (isObfuscated ? "(FLse;)V" : "(FLnet/minecraft/util/EnumHandSide;)V") + "", false);
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "func_78088_a" : "render", "" + (isObfuscated ? "(Lrw;FFFFFF)V" : "(Lnet/minecraft/entity/Entity;FFFFFF)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.FLOAD, 2);
		mv.visitVarInsn(Opcodes.FLOAD, 3);
		mv.visitVarInsn(Opcodes.FLOAD, 4);
		mv.visitVarInsn(Opcodes.FLOAD, 5);
		mv.visitVarInsn(Opcodes.FLOAD, 6);
		mv.visitVarInsn(Opcodes.FLOAD, 7);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/model/ModelPlayerAPI", "render", "(Lapi/player/model/IModelPlayerAPI;Lnet/minecraft/entity/Entity;FFFFFF)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realRender", "(Lnet/minecraft/entity/Entity;FFFFFF)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.FLOAD, 2);
		mv.visitVarInsn(Opcodes.FLOAD, 3);
		mv.visitVarInsn(Opcodes.FLOAD, 4);
		mv.visitVarInsn(Opcodes.FLOAD, 5);
		mv.visitVarInsn(Opcodes.FLOAD, 6);
		mv.visitVarInsn(Opcodes.FLOAD, 7);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "api/player/model/ModelPlayerArmor", isObfuscated ? "func_78088_a" : "render", "" + (isObfuscated ? "(Lrw;FFFFFF)V" : "(Lnet/minecraft/entity/Entity;FFFFFF)V") + "", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superRender", "(Lnet/minecraft/entity/Entity;FFFFFF)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.FLOAD, 2);
		mv.visitVarInsn(Opcodes.FLOAD, 3);
		mv.visitVarInsn(Opcodes.FLOAD, 4);
		mv.visitVarInsn(Opcodes.FLOAD, 5);
		mv.visitVarInsn(Opcodes.FLOAD, 6);
		mv.visitVarInsn(Opcodes.FLOAD, 7);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bjp" : "net/minecraft/client/model/ModelBiped", isObfuscated ? "func_78088_a" : "render", "" + (isObfuscated ? "(Lrw;FFFFFF)V" : "(Lnet/minecraft/entity/Entity;FFFFFF)V") + "", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalRender)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC, "localRender", "(Lnet/minecraft/entity/Entity;FFFFFF)V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitVarInsn(Opcodes.FLOAD, 2);
			mv.visitVarInsn(Opcodes.FLOAD, 3);
			mv.visitVarInsn(Opcodes.FLOAD, 4);
			mv.visitVarInsn(Opcodes.FLOAD, 5);
			mv.visitVarInsn(Opcodes.FLOAD, 6);
			mv.visitVarInsn(Opcodes.FLOAD, 7);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bjp" : "net/minecraft/client/model/ModelBiped", isObfuscated ? "func_78088_a" : "render", "" + (isObfuscated ? "(Lrw;FFFFFF)V" : "(Lnet/minecraft/entity/Entity;FFFFFF)V") + "", false);
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "func_178719_a" : "setInvisible", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/model/ModelPlayerAPI", "setInvisible", "(Lapi/player/model/IModelPlayerAPI;Z)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realSetInvisible", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "api/player/model/ModelPlayerArmor", isObfuscated ? "func_178719_a" : "setInvisible", "(Z)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superSetInvisible", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bjp" : "net/minecraft/client/model/ModelBiped", isObfuscated ? "func_178719_a" : "setInvisible", "(Z)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalSetInvisible)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC, "localSetInvisible", "(Z)V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ILOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bjp" : "net/minecraft/client/model/ModelBiped", isObfuscated ? "func_178719_a" : "setInvisible", "(Z)V", false);
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "func_78086_a" : "setLivingAnimations", "" + (isObfuscated ? "(Lsf;FFF)V" : "(Lnet/minecraft/entity/EntityLivingBase;FFF)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.FLOAD, 2);
		mv.visitVarInsn(Opcodes.FLOAD, 3);
		mv.visitVarInsn(Opcodes.FLOAD, 4);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/model/ModelPlayerAPI", "setLivingAnimations", "(Lapi/player/model/IModelPlayerAPI;Lnet/minecraft/entity/EntityLivingBase;FFF)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realSetLivingAnimations", "(Lnet/minecraft/entity/EntityLivingBase;FFF)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.FLOAD, 2);
		mv.visitVarInsn(Opcodes.FLOAD, 3);
		mv.visitVarInsn(Opcodes.FLOAD, 4);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "api/player/model/ModelPlayerArmor", isObfuscated ? "func_78086_a" : "setLivingAnimations", "" + (isObfuscated ? "(Lsf;FFF)V" : "(Lnet/minecraft/entity/EntityLivingBase;FFF)V") + "", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superSetLivingAnimations", "(Lnet/minecraft/entity/EntityLivingBase;FFF)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.FLOAD, 2);
		mv.visitVarInsn(Opcodes.FLOAD, 3);
		mv.visitVarInsn(Opcodes.FLOAD, 4);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bjp" : "net/minecraft/client/model/ModelBiped", isObfuscated ? "func_78086_a" : "setLivingAnimations", "" + (isObfuscated ? "(Lsf;FFF)V" : "(Lnet/minecraft/entity/EntityLivingBase;FFF)V") + "", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalSetLivingAnimations)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC, "localSetLivingAnimations", "(Lnet/minecraft/entity/EntityLivingBase;FFF)V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitVarInsn(Opcodes.FLOAD, 2);
			mv.visitVarInsn(Opcodes.FLOAD, 3);
			mv.visitVarInsn(Opcodes.FLOAD, 4);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bjp" : "net/minecraft/client/model/ModelBiped", isObfuscated ? "func_78086_a" : "setLivingAnimations", "" + (isObfuscated ? "(Lsf;FFF)V" : "(Lnet/minecraft/entity/EntityLivingBase;FFF)V") + "", false);
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "func_178686_a" : "setModelAttributes", "" + (isObfuscated ? "(Lbju;)V" : "(Lnet/minecraft/client/model/ModelBase;)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/model/ModelPlayerAPI", "setModelAttributes", "(Lapi/player/model/IModelPlayerAPI;Lnet/minecraft/client/model/ModelBase;)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realSetModelAttributes", "(Lnet/minecraft/client/model/ModelBase;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "api/player/model/ModelPlayerArmor", isObfuscated ? "func_178686_a" : "setModelAttributes", "" + (isObfuscated ? "(Lbju;)V" : "(Lnet/minecraft/client/model/ModelBase;)V") + "", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superSetModelAttributes", "(Lnet/minecraft/client/model/ModelBase;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bjp" : "net/minecraft/client/model/ModelBiped", isObfuscated ? "func_178686_a" : "setModelAttributes", "" + (isObfuscated ? "(Lbju;)V" : "(Lnet/minecraft/client/model/ModelBase;)V") + "", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalSetModelAttributes)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC, "localSetModelAttributes", "(Lnet/minecraft/client/model/ModelBase;)V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bjp" : "net/minecraft/client/model/ModelBiped", isObfuscated ? "func_178686_a" : "setModelAttributes", "" + (isObfuscated ? "(Lbju;)V" : "(Lnet/minecraft/client/model/ModelBase;)V") + "", false);
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "func_78087_a" : "setRotationAngles", "" + (isObfuscated ? "(FFFFFFLrw;)V" : "(FFFFFFLnet/minecraft/entity/Entity;)V") + "", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitVarInsn(Opcodes.FLOAD, 2);
		mv.visitVarInsn(Opcodes.FLOAD, 3);
		mv.visitVarInsn(Opcodes.FLOAD, 4);
		mv.visitVarInsn(Opcodes.FLOAD, 5);
		mv.visitVarInsn(Opcodes.FLOAD, 6);
		mv.visitVarInsn(Opcodes.ALOAD, 7);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/model/ModelPlayerAPI", "setRotationAngles", "(Lapi/player/model/IModelPlayerAPI;FFFFFFLnet/minecraft/entity/Entity;)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realSetRotationAngles", "(FFFFFFLnet/minecraft/entity/Entity;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitVarInsn(Opcodes.FLOAD, 2);
		mv.visitVarInsn(Opcodes.FLOAD, 3);
		mv.visitVarInsn(Opcodes.FLOAD, 4);
		mv.visitVarInsn(Opcodes.FLOAD, 5);
		mv.visitVarInsn(Opcodes.FLOAD, 6);
		mv.visitVarInsn(Opcodes.ALOAD, 7);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "api/player/model/ModelPlayerArmor", isObfuscated ? "func_78087_a" : "setRotationAngles", "" + (isObfuscated ? "(FFFFFFLrw;)V" : "(FFFFFFLnet/minecraft/entity/Entity;)V") + "", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superSetRotationAngles", "(FFFFFFLnet/minecraft/entity/Entity;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitVarInsn(Opcodes.FLOAD, 2);
		mv.visitVarInsn(Opcodes.FLOAD, 3);
		mv.visitVarInsn(Opcodes.FLOAD, 4);
		mv.visitVarInsn(Opcodes.FLOAD, 5);
		mv.visitVarInsn(Opcodes.FLOAD, 6);
		mv.visitVarInsn(Opcodes.ALOAD, 7);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bjp" : "net/minecraft/client/model/ModelBiped", isObfuscated ? "func_78087_a" : "setRotationAngles", "" + (isObfuscated ? "(FFFFFFLrw;)V" : "(FFFFFFLnet/minecraft/entity/Entity;)V") + "", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalSetRotationAngles)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC, "localSetRotationAngles", "(FFFFFFLnet/minecraft/entity/Entity;)V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.FLOAD, 1);
			mv.visitVarInsn(Opcodes.FLOAD, 2);
			mv.visitVarInsn(Opcodes.FLOAD, 3);
			mv.visitVarInsn(Opcodes.FLOAD, 4);
			mv.visitVarInsn(Opcodes.FLOAD, 5);
			mv.visitVarInsn(Opcodes.FLOAD, 6);
			mv.visitVarInsn(Opcodes.ALOAD, 7);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bjp" : "net/minecraft/client/model/ModelBiped", isObfuscated ? "func_78087_a" : "setRotationAngles", "" + (isObfuscated ? "(FFFFFFLrw;)V" : "(FFFFFFLnet/minecraft/entity/Entity;)V") + "", false);
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC, isObfuscated ? "func_78085_a" : "setTextureOffset", "(Ljava/lang/String;II)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitVarInsn(Opcodes.ILOAD, 3);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/model/ModelPlayerAPI", "setTextureOffset", "(Lapi/player/model/IModelPlayerAPI;Ljava/lang/String;II)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "realSetTextureOffset", "(Ljava/lang/String;II)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitVarInsn(Opcodes.ILOAD, 3);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "api/player/model/ModelPlayerArmor", isObfuscated ? "func_78085_a" : "setTextureOffset", "(Ljava/lang/String;II)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "superSetTextureOffset", "(Ljava/lang/String;II)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitVarInsn(Opcodes.ILOAD, 3);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bjp" : "net/minecraft/client/model/ModelBiped", isObfuscated ? "func_78085_a" : "setTextureOffset", "(Ljava/lang/String;II)V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if(!hadLocalSetTextureOffset)
		{
			mv = cv.visitMethod(Opcodes.ACC_PUBLIC, "localSetTextureOffset", "(Ljava/lang/String;II)V", null, null);
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitVarInsn(Opcodes.ILOAD, 2);
			mv.visitVarInsn(Opcodes.ILOAD, 3);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, isObfuscated ? "bjp" : "net/minecraft/client/model/ModelBiped", isObfuscated ? "func_78085_a" : "setTextureOffset", "(Ljava/lang/String;II)V", false);
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getBipedBodyField", isObfuscated ? "()Lblf;" : "()Lnet/minecraft/client/model/ModelRenderer;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, "api/player/model/ModelPlayerArmor", isObfuscated ? "g" : "bipedBody", isObfuscated ? "Lblf;" : "Lnet/minecraft/client/model/ModelRenderer;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setBipedBodyField", isObfuscated ? "(Lblf;)V" : "(Lnet/minecraft/client/model/ModelRenderer;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, "api/player/model/ModelPlayerArmor", isObfuscated ? "g" : "bipedBody", isObfuscated ? "Lblf;" : "Lnet/minecraft/client/model/ModelRenderer;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getBipedHeadField", isObfuscated ? "()Lblf;" : "()Lnet/minecraft/client/model/ModelRenderer;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, "api/player/model/ModelPlayerArmor", isObfuscated ? "e" : "bipedHead", isObfuscated ? "Lblf;" : "Lnet/minecraft/client/model/ModelRenderer;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setBipedHeadField", isObfuscated ? "(Lblf;)V" : "(Lnet/minecraft/client/model/ModelRenderer;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, "api/player/model/ModelPlayerArmor", isObfuscated ? "e" : "bipedHead", isObfuscated ? "Lblf;" : "Lnet/minecraft/client/model/ModelRenderer;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getBipedHeadwearField", isObfuscated ? "()Lblf;" : "()Lnet/minecraft/client/model/ModelRenderer;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, "api/player/model/ModelPlayerArmor", isObfuscated ? "f" : "bipedHeadwear", isObfuscated ? "Lblf;" : "Lnet/minecraft/client/model/ModelRenderer;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setBipedHeadwearField", isObfuscated ? "(Lblf;)V" : "(Lnet/minecraft/client/model/ModelRenderer;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, "api/player/model/ModelPlayerArmor", isObfuscated ? "f" : "bipedHeadwear", isObfuscated ? "Lblf;" : "Lnet/minecraft/client/model/ModelRenderer;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getBipedLeftArmField", isObfuscated ? "()Lblf;" : "()Lnet/minecraft/client/model/ModelRenderer;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, "api/player/model/ModelPlayerArmor", isObfuscated ? "i" : "bipedLeftArm", isObfuscated ? "Lblf;" : "Lnet/minecraft/client/model/ModelRenderer;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setBipedLeftArmField", isObfuscated ? "(Lblf;)V" : "(Lnet/minecraft/client/model/ModelRenderer;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, "api/player/model/ModelPlayerArmor", isObfuscated ? "i" : "bipedLeftArm", isObfuscated ? "Lblf;" : "Lnet/minecraft/client/model/ModelRenderer;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getBipedLeftLegField", isObfuscated ? "()Lblf;" : "()Lnet/minecraft/client/model/ModelRenderer;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, "api/player/model/ModelPlayerArmor", isObfuscated ? "k" : "bipedLeftLeg", isObfuscated ? "Lblf;" : "Lnet/minecraft/client/model/ModelRenderer;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setBipedLeftLegField", isObfuscated ? "(Lblf;)V" : "(Lnet/minecraft/client/model/ModelRenderer;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, "api/player/model/ModelPlayerArmor", isObfuscated ? "k" : "bipedLeftLeg", isObfuscated ? "Lblf;" : "Lnet/minecraft/client/model/ModelRenderer;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getBipedRightArmField", isObfuscated ? "()Lblf;" : "()Lnet/minecraft/client/model/ModelRenderer;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, "api/player/model/ModelPlayerArmor", isObfuscated ? "h" : "bipedRightArm", isObfuscated ? "Lblf;" : "Lnet/minecraft/client/model/ModelRenderer;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setBipedRightArmField", isObfuscated ? "(Lblf;)V" : "(Lnet/minecraft/client/model/ModelRenderer;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, "api/player/model/ModelPlayerArmor", isObfuscated ? "h" : "bipedRightArm", isObfuscated ? "Lblf;" : "Lnet/minecraft/client/model/ModelRenderer;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getBipedRightLegField", isObfuscated ? "()Lblf;" : "()Lnet/minecraft/client/model/ModelRenderer;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, "api/player/model/ModelPlayerArmor", isObfuscated ? "j" : "bipedRightLeg", isObfuscated ? "Lblf;" : "Lnet/minecraft/client/model/ModelRenderer;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setBipedRightLegField", isObfuscated ? "(Lblf;)V" : "(Lnet/minecraft/client/model/ModelRenderer;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, "api/player/model/ModelPlayerArmor", isObfuscated ? "j" : "bipedRightLeg", isObfuscated ? "Lblf;" : "Lnet/minecraft/client/model/ModelRenderer;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getBoxListField", "()Ljava/util/List;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, "api/player/model/ModelPlayerArmor", isObfuscated ? "r" : "boxList", "Ljava/util/List;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setBoxListField", "(Ljava/util/List;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, "api/player/model/ModelPlayerArmor", isObfuscated ? "r" : "boxList", "Ljava/util/List;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getIsChildField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, "api/player/model/ModelPlayerArmor", isObfuscated ? "q" : "isChild", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setIsChildField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, "api/player/model/ModelPlayerArmor", isObfuscated ? "q" : "isChild", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getIsRidingField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, "api/player/model/ModelPlayerArmor", isObfuscated ? "p" : "isRiding", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setIsRidingField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, "api/player/model/ModelPlayerArmor", isObfuscated ? "p" : "isRiding", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getIsSneakField", "()Z", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, "api/player/model/ModelPlayerArmor", isObfuscated ? "n" : "isSneak", "Z");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setIsSneakField", "(Z)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, "api/player/model/ModelPlayerArmor", isObfuscated ? "n" : "isSneak", "Z");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getLeftArmPoseField", isObfuscated ? "()Lbjp$a;" : "()Lnet/minecraft/client/model/ModelBiped$ArmPose;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, "api/player/model/ModelPlayerArmor", isObfuscated ? "l" : "leftArmPose", isObfuscated ? "Lbjp$a;" : "Lnet/minecraft/client/model/ModelBiped$ArmPose;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setLeftArmPoseField", isObfuscated ? "(Lbjp$a;)V" : "(Lnet/minecraft/client/model/ModelBiped$ArmPose;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, "api/player/model/ModelPlayerArmor", isObfuscated ? "l" : "leftArmPose", isObfuscated ? "Lbjp$a;" : "Lnet/minecraft/client/model/ModelBiped$ArmPose;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getRightArmPoseField", isObfuscated ? "()Lbjp$a;" : "()Lnet/minecraft/client/model/ModelBiped$ArmPose;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, "api/player/model/ModelPlayerArmor", isObfuscated ? "m" : "rightArmPose", isObfuscated ? "Lbjp$a;" : "Lnet/minecraft/client/model/ModelBiped$ArmPose;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setRightArmPoseField", isObfuscated ? "(Lbjp$a;)V" : "(Lnet/minecraft/client/model/ModelBiped$ArmPose;)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, "api/player/model/ModelPlayerArmor", isObfuscated ? "m" : "rightArmPose", isObfuscated ? "Lbjp$a;" : "Lnet/minecraft/client/model/ModelBiped$ArmPose;");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getSwingProgressField", "()F", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, "api/player/model/ModelPlayerArmor", isObfuscated ? "o" : "swingProgress", "F");
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setSwingProgressField", "(F)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.FLOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, "api/player/model/ModelPlayerArmor", isObfuscated ? "o" : "swingProgress", "F");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getTextureHeightField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, "api/player/model/ModelPlayerArmor", isObfuscated ? "t" : "textureHeight", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setTextureHeightField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, "api/player/model/ModelPlayerArmor", isObfuscated ? "t" : "textureHeight", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getTextureWidthField", "()I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, "api/player/model/ModelPlayerArmor", isObfuscated ? "s" : "textureWidth", "I");
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "setTextureWidthField", "(I)V", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, "api/player/model/ModelPlayerArmor", isObfuscated ? "s" : "textureWidth", "I");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getModelPlayerBase", "(Ljava/lang/String;)Lapi/player/model/ModelPlayerBase;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/model/ModelPlayerAPI", "getModelPlayerBase", "(Lapi/player/model/IModelPlayerAPI;Ljava/lang/String;)Lapi/player/model/ModelPlayerBase;", false);
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getModelPlayerBaseIds", "()Ljava/util/Set;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/model/ModelPlayerAPI", "getModelPlayerBaseIds", "(Lapi/player/model/IModelPlayerAPI;)Ljava/util/Set;", false);
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getExpandParameter", "()F", null, null);
		mv.visitVarInsn(/*Opcodes*/25/*ALOAD*/, /*0*/0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/model/ModelPlayerAPI", "getExpandParameter", "(Lapi/player/model/IModelPlayerAPI;)F", false);
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getYOffsetParameter", "()F", null, null);
		mv.visitVarInsn(/*Opcodes*/25/*ALOAD*/, /*0*/0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/model/ModelPlayerAPI", "getYOffsetParameter", "(Lapi/player/model/IModelPlayerAPI;)F", false);
		mv.visitInsn(Opcodes.FRETURN);
		mv.visitMaxs(0, 1);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getTextureWidthParameter", "()I", null, null);
		mv.visitVarInsn(/*Opcodes*/25/*ALOAD*/, /*0*/0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/model/ModelPlayerAPI", "getTextureWidthParameter", "(Lapi/player/model/IModelPlayerAPI;)I", false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 2);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getTextureHeightParameter", "()I", null, null);
		mv.visitVarInsn(/*Opcodes*/25/*ALOAD*/, /*0*/0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/model/ModelPlayerAPI", "getTextureHeightParameter", "(Lapi/player/model/IModelPlayerAPI;)I", false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 3);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getSmallArmsParameter", "()Z", null, null);
		mv.visitVarInsn(/*Opcodes*/25/*ALOAD*/, /*0*/0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/model/ModelPlayerAPI", "getSmallArmsParameter", "(Lapi/player/model/IModelPlayerAPI;)Z", false);
		mv.visitInsn(Opcodes.IRETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getModelPlayerType", "()Ljava/lang/String;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/model/ModelPlayerAPI", "getModelPlayerType", "(Lapi/player/model/IModelPlayerAPI;)Ljava/lang/String;", false);
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "dynamic", "(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/Object;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.ALOAD, 2);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/model/ModelPlayerAPI", "dynamic", "(Lapi/player/model/IModelPlayerAPI;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/Object;", false);
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getModelPlayerAPI", "()Lapi/player/model/ModelPlayerAPI;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, "api/player/model/ModelPlayerArmor", "modelPlayerAPI", "Lapi/player/model/ModelPlayerAPI;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "getModelPlayer", isObfuscated ? "()Lbjp;" : "()Lnet/minecraft/client/model/ModelBiped;", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "getAllInstances", isObfuscated ? "()[Lbjp;" : "()[Lnet/minecraft/client/model/ModelBiped;", null, null);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "api/player/model/ModelPlayerAPI", "getAllInstances", isObfuscated ? "()[Lbjp;" : "()[Lnet/minecraft/client/model/ModelBiped;", false);
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		cv.visitField(Opcodes.ACC_PRIVATE | Opcodes.ACC_FINAL, "modelPlayerAPI", "Lapi/player/model/ModelPlayerAPI;", null, null);
	}
}
