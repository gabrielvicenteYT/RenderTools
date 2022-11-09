package me.anemoi.rendertools.utils;

import me.anemoi.rendertools.config.modules.AnimationsConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/*
Author: Polyfrost Team
Thanks to Polyfrost for this code
https://github.com/Polyfrost/OverflowAnimationsV2/blob/master/LICENSE-GPL
 */

public class AnimationHandler {

    private final Minecraft mc = Minecraft.getMinecraft();

    public float prevSwingProgress;
    public float swingProgress;
    private int swingProgressInt;
    private boolean isSwingInProgress;

    public float getSwingProgress(float partialTickTime) {
        float currentProgress = this.swingProgress - this.prevSwingProgress;

        if (!isSwingInProgress) {
            return mc.thePlayer.getSwingProgress(partialTickTime);
        }

        if (currentProgress < 0.0F) {
            currentProgress++;
        }

        return this.prevSwingProgress + currentProgress * partialTickTime;
    }


    private int getArmSwingAnimationEnd(EntityPlayerSP player) {
        return player.isPotionActive(Potion.digSpeed) ? 5 - player.getActivePotionEffect(Potion.digSpeed).getAmplifier() :
                (player.isPotionActive(Potion.digSlowdown) ? 8 + player.getActivePotionEffect(Potion.digSlowdown).getAmplifier() * 2 : 6);
    }

    private void updateSwingProgress() {
        final EntityPlayerSP player = mc.thePlayer;
        if (player == null) {
            return;
        }

        prevSwingProgress = swingProgress;

        int max = getArmSwingAnimationEnd(player);

        if (AnimationsConfig.special && mc.gameSettings.keyBindAttack.isKeyDown() &&
                mc.thePlayer.isBlocking() &&
                mc.objectMouseOver != null &&
                mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            if (!this.isSwingInProgress || this.swingProgressInt >= max >> 1 || this.swingProgressInt < 0) {
                isSwingInProgress = true;
                swingProgressInt = -1;
            }
        }

        if (!this.isSwingInProgress) this.swingProgressInt = 0;

        if (this.isSwingInProgress) {
            this.swingProgressInt++;

            if (this.swingProgressInt >= max || !mc.thePlayer.isBlocking()) {
                this.swingProgressInt = 0;
                this.isSwingInProgress = false;
            }
        }

        //update swing progress
        this.swingProgress = (float) this.swingProgressInt / (float) max;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            updateSwingProgress();
        }
    }
}
