package me.thef1xer.gateclient.manager;

import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.modules.Modules;
import me.thef1xer.gateclient.modules.combat.TpReach;
import me.thef1xer.gateclient.modules.movement.LOBotSpoof;
import me.thef1xer.gateclient.modules.movement.NoMoveEvent;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    public final List<Module> moduleList = new ArrayList<>();

    public void init() {
        // Combat
        Modules.TP_REACH = addModule(new TpReach());

        // Movement
        Modules.LO_BOT_SPOOF = addModule(new LOBotSpoof());
        Modules.NO_MOVE_EVENT = addModule(new NoMoveEvent());
    }

    public <T extends Module> T addModule(T module) {
        moduleList.add(module);
        return module;
    }
}
