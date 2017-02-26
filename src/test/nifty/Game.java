/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.nifty;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.DropDownSelectionChangedEvent;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.controls.Slider;
import de.lessvoid.nifty.controls.SliderChangedEvent;
import de.lessvoid.nifty.controls.dropdown.builder.DropDownBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.controls.listbox.builder.ListBoxBuilder;
import de.lessvoid.nifty.controls.slider.builder.SliderBuilder;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.SizeValue;
import env3d.advanced.EnvAdvanced;
import env3d.advanced.EnvSkyRoom;
import env3d.advanced.EnvTerrain;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.input.Keyboard;

/**
 *
 * @author jmadar
 */
public class Game implements ScreenController {

    private EnvAdvanced env;
    private ListBox<String> listBox;
    private EnvTerrain terrain;
    private String terrainMap;
    private float xScale = 1, yScale = 0.2f, zScale = 1;

    public void play() {
        env = new EnvAdvanced();

        terrainMap = "textures/terrain/termap1.png";
        env.setRoom(new EnvSkyRoom("/Users/jmadar/Documents/skybox/sky5"));
        terrain = new EnvTerrain(terrainMap);
        terrain.setTexture("textures/mud.png");
        terrain.setScale(xScale, yScale, zScale);       
        env.addObject(terrain);
        //env.getNiftyGUI().fromXml("../../src/test/nifty/helloEnv3d.xml", "start", this);
        Nifty nifty = env.getNiftyGUI();
        Logger.getLogger("de").setLevel(Level.SEVERE);        
        final ScreenController controller = this;


        Screen menuScreen = new ScreenBuilder("menu") {

            {
                
                controller(controller);

                layer(new LayerBuilder("layer") {

                    {                        
                        //backgroundColor("#ffffff00");
                        alignCenter();
                        valignCenter();
                        childLayoutCenter();
                        //interactOnClick("quit()");
                        panel(new PanelBuilder() {

                            {                                
                                //style("nifty-panel");
                                alignCenter();
                                valignCenter();
                                childLayoutVertical();
                                
                                height(SizeValue.percent(60));
                                width(SizeValue.percent(80));                                
//                                control(new ListBoxBuilder("listBox") {
//
//                                    {
//                                        width(percentage(80));
//                                        backgroundColor("#00000000");
//                                        color("#00000000");
//                                        alignCenter();
//                                        valignCenter();
//                                        font("aurulent-sans-16.fnt");
//                                        displayItems(4);
//                                        hideHorizontalScrollbar();
//
//                                    }
//                                });
//                                control(new ListBoxBuilder("terrainList") {
//
//                                    {
//                                        width(percentage(80));
//                                        backgroundColor("#fff6");
//                                        alignCenter();
//                                        valignCenter();
//                                        font("aurulent-sans-16.fnt");
//                                        displayItems(4);
//                                        hideHorizontalScrollbar();
//
//                                    }
//                                });
                                control(new DropDownBuilder("skybox") {
                                    {
                                        width("*");
                                    }
                                });
                                control(new DropDownBuilder("terrain") {
                                    {
                                        width("*");
                                    }
                                });
                                control(new SliderBuilder("xSlider", false));
                                control(new SliderBuilder("ySlider", false));
                                control(new SliderBuilder("zSlider", false));
                            }
                        });
                    }
                });
            }
        }.build(env.getNiftyGUI());
        

        new ScreenBuilder("ingame") {

            {
                controller(controller);                
                layer(new LayerBuilder("layer2") {

                    {
                        alignCenter();
                        childLayoutCenter();
                        panel(new PanelBuilder("ingame_p1") {

                            {
                                alignLeft();
                                valignBottom();
                                childLayoutCenter();
                                height(SizeValue.percent(10));
                                width(SizeValue.percent(100));
                                backgroundColor("#f60f005f");
                                text(new TextBuilder("text") {

                                    {
                                        font("aurulent-sans-16.fnt");
                                        alignCenter();
                                        valignCenter();
                                        text("Press B to change background image");
                                    }
                                });
                            }
                        });
                        //control(new ConsoleBuilder("console", 10));
                    }
                });
            }
        }.build(env.getNiftyGUI());

        env.getNiftyGUI().gotoScreen("ingame");
        env.setDefaultControl(false);

        env.setCameraXYZ(60, terrain.getHeight(60, 60) + 2, 60);

        while (env.getKey() != 1) {
            if (env.getKey() == Keyboard.KEY_B) {

                if (env.getNiftyGUI().getCurrentScreen().getScreenId().equals("ingame")) {
                    env.getNiftyGUI().gotoScreen("menu");
                    env.setDefaultControl(false);
                } else {
                    env.getNiftyGUI().gotoScreen("ingame");
                    env.setDefaultControl(true);

                }
            }

            env.advanceOneFrame();
        }

        env.exit();
    }
    public void bind(Nifty nifty, Screen screen) {
        System.out.println("bind( " + screen.getScreenId() + ")");
        DropDown skybox = screen.findNiftyControl("skybox", DropDown.class);        
        skybox.addItem("sky5");
        skybox.addItem("sky7");       
        skybox.addItem("sky13");
        skybox.addItem("sky15");
        skybox.addItem("sky17");
        skybox.addItem("sky19");
        skybox.addItem("sky20");
        skybox.addItem("sky21");
        
        DropDown terrain = screen.findNiftyControl("terrain", DropDown.class);
        terrain.addItem("termap1.png");
        terrain.addItem("3dtech0.png");
        terrain.addItem("terrain.png");
        
        Slider xSlider = screen.findNiftyControl("xSlider", Slider.class);
        System.out.println("Current x Scale "+xScale);
        xSlider.setValue(xScale*20);

        Slider ySlider = screen.findNiftyControl("ySlider", Slider.class);
        System.out.println("Current y Scale "+yScale);
        ySlider.setValue(yScale*100);

        Slider zSlider = screen.findNiftyControl("zSlider", Slider.class);
        System.out.println("Current z Scale "+zScale);
        zSlider.setValue(zScale*20);


    }
    public void bind2(Nifty nifty, Screen screen) {
        System.out.println("bind( " + screen.getScreenId() + ")");


        if (screen.getScreenId().equals("menu")) {
            ListBox<String> listBox = screen.findNiftyControl("listBox", ListBox.class);


            if (listBox.getItems().isEmpty()) {
                System.out.println("Adding items");

                listBox.addItem("sky5");
                listBox.addItem("sky7");
                listBox.addItem("sky13");
                listBox.addItem("sky15");
                listBox.addItem("sky17");
                listBox.addItem("sky19");
                listBox.addItem("sky20");
                listBox.addItem("sky21");
            }
            ListBox<String> terrainList = screen.findNiftyControl("terrainList", ListBox.class);


            if (terrainList.getItems().isEmpty()) {
                terrainList.addItem("3dtech0.png");
                terrainList.addItem("termap1.png");
                terrainList.addItem("terrain.png");
            }
            
            Slider xSlider = screen.findNiftyControl("xSlider", Slider.class);
            System.out.println("Current x Scale "+xScale);
            xSlider.setValue(xScale*20);

            Slider ySlider = screen.findNiftyControl("ySlider", Slider.class);
            System.out.println("Current y Scale "+yScale);
            ySlider.setValue(yScale*100);

            Slider zSlider = screen.findNiftyControl("zSlider", Slider.class);
            System.out.println("Current z Scale "+zScale);
            zSlider.setValue(zScale*20);

        }
    }

    public void onStartScreen() {
        System.out.println("onStartScreen");
    }

    public void onEndScreen() {
        System.out.println("onEndScreen");
    }

    @NiftyEventSubscriber(id = "skybox")
    public void onSkyboxDropdownSelection(final String id, final DropDownSelectionChangedEvent<String> event) {
        // in here we can use the given event to access the new selection
        // event.getSelection(); ...
        System.out.println("Selected " + event.getSelection());
        env.setRoom(new EnvSkyRoom("/Users/jmadar/Documents/skybox/" + event.getSelection()));
        env.addObject(terrain);
        env.getNiftyGUI().gotoScreen("ingame");
        env.setDefaultControl(true);
    }
    
    @NiftyEventSubscriber(id = "terrain")
    public void onTerrainDropdownSelection(final String id, final DropDownSelectionChangedEvent<String> event) {
        System.out.println("Changing Terrain " + event.getSelection());
        env.removeObject(terrain);
        terrain = new EnvTerrain("textures/terrain/" + event.getSelection());
        terrain.setScale(xScale, yScale, zScale);
        terrain.setTexture("textures/mud.png");
        env.addObject(terrain);
        env.getNiftyGUI().gotoScreen("ingame");
        env.setDefaultControl(true);
    }

    @NiftyEventSubscriber(id = "listbox")
    public void onSkyboxSelection(final String id, final ListBoxSelectionChangedEvent<String> event) {
        // in here we can use the given event to access the new selection
        // event.getSelection(); ...
        System.out.println("Selected " + event.getSelection().get(0));
        env.setRoom(new EnvSkyRoom("/Users/jmadar/Documents/skybox/" + event.getSelection().get(0)));
        env.addObject(terrain);
        env.getNiftyGUI().gotoScreen("ingame");
        env.setDefaultControl(true);
    }

    @NiftyEventSubscriber(id = "terrainList")
    public void onTerrainSelection(final String id, final ListBoxSelectionChangedEvent<String> event) {
        System.out.println("Changing Terrain " + event.getSelection().get(0));
        env.removeObject(terrain);
        terrain = new EnvTerrain("textures/terrain/" + event.getSelection().get(0));
        terrain.setScale(xScale, yScale, zScale);
        terrain.setTexture("textures/mud.png");
        env.addObject(terrain);
        env.getNiftyGUI().gotoScreen("ingame");
        env.setDefaultControl(true);
    }

    @NiftyEventSubscriber(pattern = "[xyz]Slider")
    public void onScaleChange(String id, SliderChangedEvent newValue) {        
        System.out.println(id+" "+newValue.getValue());
        //if (newValue.getValue() > 0) {
            if (id.startsWith("x")) {   
                xScale = newValue.getValue() / 20;            
            }
            if (id.startsWith("y")) {
                yScale = newValue.getValue() / 100;       
            }
            if (id.startsWith("z")) {
                zScale = newValue.getValue() / 20;        
            }
            terrain.setScale(xScale, yScale , zScale);
            env.setCameraXYZ(env.getCameraX(), terrain.getHeight(env.getCameraX(), env.getCameraZ())+10, env.getCameraZ());
        //}
    }

    public static void main(String args[]) {
        (new Game()).play();
    }
}
