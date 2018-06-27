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

package api.player.forge;

import api.player.model.ModelPlayerArmorClassVisitor;
import api.player.model.ModelPlayerClassVisitor;
import api.player.render.LayerPlayerArmorClassVisitor;
import api.player.render.RenderPlayerClassVisitor;
import net.minecraft.launchwrapper.IClassTransformer;

import java.util.Hashtable;
import java.util.Map;
import java.util.Stack;

public class RenderPlayerAPITransformer implements IClassTransformer
{
	public byte[] transform(String name, String transformedName, byte[] bytes)
	{
		if(transformedName.equals(RenderPlayerClassVisitor.targetClassName))
		{
			Stack<String> models = new Stack<String>();
			models.push(ModelPlayerClassVisitor.deobfuscatedClassReference + ":main");

			Stack<String> layers = new Stack<String>();
			layers.push(LayerPlayerArmorClassVisitor.deobfuscatedClassReference);

			Map<String, Stack<String>> constructorReplacements = new Hashtable<String, Stack<String>>();
			constructorReplacements.put(ModelPlayerClassVisitor.obfuscatedClassReference, models);
			constructorReplacements.put(ModelPlayerClassVisitor.deobfuscatedClassReference, models);
			constructorReplacements.put(LayerPlayerArmorClassVisitor.obfuscatedSuperClassReference, layers);
			constructorReplacements.put(LayerPlayerArmorClassVisitor.deobfuscateSuperClassReference, layers);

			return RenderPlayerClassVisitor.transform(bytes, RenderPlayerAPIPlugin.isObfuscated, constructorReplacements);
		}

		if(name.equals(LayerPlayerArmorClassVisitor.targetClassName))
		{
			Stack<String> models = new Stack<String>();
			models.push(ModelPlayerArmorClassVisitor.deobfuscatedClassReference + ":chestplate");
			models.push(ModelPlayerArmorClassVisitor.deobfuscatedClassReference + ":armor");

			Map<String, Stack<String>> constructorReplacements = new Hashtable<String, Stack<String>>();
			constructorReplacements.put(ModelPlayerArmorClassVisitor.deobfuscatedClassReference, models);

			return LayerPlayerArmorClassVisitor.transform(bytes, RenderPlayerAPIPlugin.isObfuscated, constructorReplacements);
		}

		if(transformedName.equals(ModelPlayerClassVisitor.targetClassName))
			return ModelPlayerClassVisitor.transform(bytes, RenderPlayerAPIPlugin.isObfuscated);

		if(transformedName.equals(ModelPlayerArmorClassVisitor.targetClassName))
			return ModelPlayerArmorClassVisitor.transform(bytes, RenderPlayerAPIPlugin.isObfuscated);

		return bytes;
	}
}