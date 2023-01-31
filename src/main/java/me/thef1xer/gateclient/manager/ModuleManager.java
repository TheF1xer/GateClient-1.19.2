package me.thef1xer.gateclient.manager;

import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.modules.Modules;
import me.thef1xer.gateclient.modules.movement.LOBotSpoof;
import me.thef1xer.gateclient.modules.movement.NoMoveEvent;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    public final List<Module> moduleList = new ArrayList<>();

    public void init() {
        moduleList.add(Modules.LO_BOT_SPOOF = new LOBotSpoof());
        moduleList.add(Modules.NO_MOVE_EVENT = new NoMoveEvent());
    }
}
