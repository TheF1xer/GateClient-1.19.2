package me.thef1xer.gateclient.modules.movement;

import me.thef1xer.gateclient.mixin.Vec3dAccessor;
import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.settings.impl.IntSetting;
import me.thef1xer.gateclient.utils.PlayerUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.MovementType;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class NoMoveEvent extends Module {
    private final float EVENT_CONST = 1F / 16;
    private final MinecraftClient mc = MinecraftClient.getInstance();
    private int ticksSinceLastReset = 0;

    public final IntSetting tickDelay = addSetting(new IntSetting("Tick Delay", "tickdelay", 10, 0, 20));

    public NoMoveEvent() {
        super("No Move Event", "nomoveevent", GLFW.GLFW_KEY_N, ModuleCategory.MOVEMENT);
    }

    public void onMove(MovementType movementType, Vec3d movement) {
        if (movementType == MovementType.SELF) {    // Only when the player is moving by themselves
            ((Vec3dAccessor) movement).setX(0);
            ((Vec3dAccessor) movement).setY(0);
            ((Vec3dAccessor) movement).setZ(0);
        }
    }

    public void onSendMovementPackets(CallbackInfo callbackInfo) {
        final double[] horizontalVec = PlayerUtil.getPlayerHorizontalMoveVec();
        double verticalMovement = calculateVerticalMovement();

        // Send small movement packet than will not trigger a Paper PlayerMoveEvent serverside
        if (ticksSinceLastReset == tickDelay.getValue()) {
            mc.player.setPosition(
                    mc.player.getX() + horizontalVec[0] * EVENT_CONST * 0.8D,
                    mc.player.getY() + verticalMovement,
                    mc.player.getZ() + horizontalVec[1] * EVENT_CONST * 0.8D
            );

            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                    mc.player.getX(),
                    mc.player.getY(),
                    mc.player.getZ(),
                    true
            ));
        }

        // Send a big movement packet that will get cancelled serverside and will reset
        // the player's last position
        if (ticksSinceLastReset > 2 * tickDelay.getValue()) {
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                    mc.player.getX(),
                    mc.player.getY() + 1337,
                    mc.player.getZ(),
                    true
            ));

            ticksSinceLastReset = 0;
        }

        ticksSinceLastReset++;
        callbackInfo.cancel();              // Cancel the sending of normal packets
    }

    private double calculateVerticalMovement() {
        double verticalMovement = 0;

        if (mc.player.input.jumping) {
            verticalMovement += EVENT_CONST * 0.8D;
        }

        if (mc.player.input.sneaking) {
            verticalMovement -= EVENT_CONST * 0.8D;
        }

        return verticalMovement;
    }
}
