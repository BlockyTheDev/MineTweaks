package me.jishuna.minetweaks.tweaks.mobs;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Tameable;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.projectiles.ProjectileSource;

import me.jishuna.commonlib.utils.FileUtils;
import me.jishuna.minetweaks.api.RegisterTweak;
import me.jishuna.minetweaks.api.tweak.Tweak;

@RegisterTweak(name = "pet_protection")
public class PetProtection extends Tweak {

	public PetProtection(JavaPlugin plugin, String name) {
		super(plugin, name);

		addEventHandler(EntityDamageByEntityEvent.class, this::onDamage);
	}

	@Override
	public void reload() {
		FileUtils.loadResource(getOwningPlugin(), "Tweaks/Mobs/" + this.getName() + ".yml").ifPresent(config -> {
			loadDefaults(config, true);
		});
	}

	private void onDamage(EntityDamageByEntityEvent event) {
		if (!(event.getEntity()instanceof Tameable tamable) || tamable.getOwner() == null)
			return;

		Entity damager = event.getDamager();
		Player player = null;

		if (damager instanceof Player) {
			player = (Player) damager;
		} else if (damager instanceof Projectile projecitle) {
			ProjectileSource shooter = projecitle.getShooter();

			if (shooter instanceof Player) {
				player = (Player) shooter;
			}
		}

		if (player == null)
			return;

		if (tamable.getOwner().getUniqueId().equals(player.getUniqueId()))
			event.setCancelled(true);
	}
}
