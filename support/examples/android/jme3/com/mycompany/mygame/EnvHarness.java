package com.mycompany.mygame;

import android.widget.TextView;
import env3d.android.EnvAndroid;
import env3d.android.EnvMobileGame;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author jmadar
 */
public class EnvHarness extends EnvAndroid {

    public EnvHarness() {

        try {
            Class<? extends EnvMobileGame> clazz = (Class<? extends EnvMobileGame>) Class.forName("Game");
            game = clazz.newInstance();
            game.setEnv(this);
        } catch (Exception ex) {
            handleError("Game init failed", ex);
        }

        // Can't possibly in emulator mode if launch from the harness
        emulatorMode = false;
    }
}
