package me.thef1xer.gateclient.utils;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class RayTraceHit {
    public record EntityHit(Entity entity, Vec3d hitPos) {}
}
