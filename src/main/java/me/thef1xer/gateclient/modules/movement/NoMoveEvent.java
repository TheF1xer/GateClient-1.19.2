package me.thef1xer.gateclient.modules.movement;

import me.thef1xer.gateclient.mixin.PlayerMoveC2SPacketAccessor;
import me.thef1xer.gateclient.mixin.PlayerPositionLookS2CPacketAccessor;
import me.thef1xer.gateclient.mixin.Vec3dAccessor;
import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.settings.impl.BooleanSetting;
import me.thef1xer.gateclient.settings.impl.FloatSetting;
import me.thef1xer.gateclient.settings.impl.IntSetting;
import me.thef1xer.gateclient.utils.PlayerUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.MovementType;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class NoMoveEvent extends Module {
    private final float EVENT_CONST = 1F / 16;
    private final MinecraftClient mc = MinecraftClient.getInstance();
    private int ticksSinceLastMove = 0;

    public final IntSetting tickDelay = addSetting(new IntSetting("Tick Delay", "tickdelay", 10, 0, 20));
    public final FloatSetting moveAmount = addSetting(new FloatSetting("Move Amount", "moveamount", 0.8F, 0, 1));
    public final BooleanSetting noRotation = addSetting(new BooleanSetting("No Rotation", "norotation", true));

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

        callbackInfo.cancel();              // Cancel the sending of normal packets while module is active

        // Send a big movement packet that will get cancelled serverside and will reset
        // the player's last position
        if (ticksSinceLastMove == 0) {
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                    mc.player.getX(),
                    mc.player.getY() + 1337,
                    mc.player.getZ(),
                    true
            ));
        }

        // Send small movement packet than will not trigger a Paper PlayerMoveEvent serverside
        if (ticksSinceLastMove >= tickDelay.getValue()) {
            mc.player.setPosition(
                    mc.player.getX() + horizontalVec[0] * EVENT_CONST * moveAmount.getValue(),
                    mc.player.getY() + verticalMovement,
                    mc.player.getZ() + horizontalVec[1] * EVENT_CONST * moveAmount.getValue()
            );

            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                    mc.player.getX(),
                    mc.player.getY(),
                    mc.player.getZ(),
                    true
            ));

            ticksSinceLastMove = 0;
            return;
        }

        ticksSinceLastMove++;
    }

    public void onSendPlayerMoveC2SPacket(PlayerMoveC2SPacket packet)  {
        // This fixes a bug that happens when the ClientPlayNetworkHandler::onPlayerPositionLook method
        // would send a packet with pitch and yaw information that might trigger a PlayerMoveEvent
        ((PlayerMoveC2SPacketAccessor) packet).setPitch(0);
        ((PlayerMoveC2SPacketAccessor) packet).setYaw(0);
    }

    public void onRcvPlayerPositionLookS2CPacket(PlayerPositionLookS2CPacket packet) {
        if (noRotation.getValue()) {
            ((PlayerPositionLookS2CPacketAccessor) packet).setYaw(mc.player.getYaw());
            ((PlayerPositionLookS2CPacketAccessor) packet).setPitch(mc.player.getPitch());
        }
    }

    private double calculateVerticalMovement() {
        double verticalMovement = 0;

        if (mc.player.input.jumping) {
            verticalMovement += EVENT_CONST * moveAmount.getValue();
        }

        if (mc.player.input.sneaking) {
            verticalMovement -= EVENT_CONST * moveAmount.getValue();
        }

        return verticalMovement;
    }
}
