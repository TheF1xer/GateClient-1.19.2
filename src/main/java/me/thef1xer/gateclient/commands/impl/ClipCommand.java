package me.thef1xer.gateclient.commands.impl;

import me.thef1xer.gateclient.commands.Command;
import me.thef1xer.gateclient.commands.CommandNode;
import me.thef1xer.gateclient.commands.nodes.FloatNode;
import me.thef1xer.gateclient.commands.nodes.StringNode;
import me.thef1xer.gateclient.utils.MathUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

public class ClipCommand extends Command {
    public CommandNode<Float> horizontal;
    public CommandNode<Float> vertical;

    public ClipCommand() {
        super("clip");
    }

    @Override
    public void init(StringNode commandNode) {
        this.horizontal = commandNode.then(new FloatNode("<horizontal distance>"));

        this.vertical = this.horizontal.then(new FloatNode("<vertical distance>")).executes(() -> {
            ClientPlayerEntity player = MinecraftClient.getInstance().player;

            double[] hMoveVec = MathUtil.getHorizontalDirection(player.getYaw());

            player.setPosition(
                    player.getX() + hMoveVec[0] * this.horizontal.getParseResult(),
                    player.getY() + this.vertical.getParseResult(),
                    player.getZ() + hMoveVec[1] * this.horizontal.getParseResult()
            );
        });
    }
}
