package me.jishuna.minetweaks.tweaks.recipes;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import me.jishuna.commonlib.utils.FileUtils;
import me.jishuna.minetweaks.MineTweaks;
import me.jishuna.minetweaks.api.RecipeManager;
import me.jishuna.minetweaks.api.RegisterTweak;
import me.jishuna.minetweaks.api.tweak.Tweak;

@RegisterTweak("more_stairs")
public class StairsRecipeTweak extends Tweak {
	private final Set<ShapedRecipe> recipes = new HashSet<>();

	public StairsRecipeTweak(MineTweaks plugin, String name) {
		super(plugin, name);
	}

	@Override
	public void reload() {
		FileUtils.loadResource(getPlugin(), "Tweaks/Recipes/" + this.getName() + ".yml").ifPresent(config -> {
			loadDefaults(config, true);

			if (!isEnabled())
				return;

			Iterator<Recipe> iterator = Bukkit.recipeIterator();

			while (iterator.hasNext()) {
				Recipe recipe = iterator.next();
				ItemStack result = recipe.getResult();

				if (result.getType().toString().endsWith("_STAIRS") && result.getAmount() > 1
						&& recipe instanceof ShapedRecipe shaped
						&& shaped.getKey().getNamespace().equals("minecraft")) {
					iterator.remove();
					this.recipes.add(shaped);
				}
			}

			this.recipes.forEach(recipe -> {
				ItemStack result = recipe.getResult();
				result.setAmount(config.getInt("more-stairs-amount", 8));

				ShapedRecipe newRecipe = RecipeManager.copyRecipe(getPlugin(), recipe, result);
				RecipeManager.getInstance().addRecipe(getPlugin(), newRecipe);
			});
		});
	}
}
