import de.lessvoid.nifty.*;
import de.lessvoid.nifty.builder.*;
import de.lessvoid.nifty.controls.*;
import de.lessvoid.nifty.controls.dropdown.builder.*;
import de.lessvoid.nifty.controls.label.builder.*;
import de.lessvoid.nifty.controls.listbox.builder.*;
import de.lessvoid.nifty.controls.slider.builder.*;
import de.lessvoid.nifty.screen.*;
import env3d.advanced.EnvAdvanced;
import env3d.advanced.EnvSkyRoom;
import env3d.advanced.EnvTerrain;
import org.lwjgl.input.Keyboard;

public class Game implements ScreenController {

    private EnvAdvanced env;
    private ListBox<String> listBox;
    private EnvTerrain terrain;
    private String terrainMap;
    private float xScale = 1, yScale = 0.2f, zScale = 1;

    public void play() {
        env = new EnvAdvanced();

        terrainMap = "textures/terrain/termap1.png";
        env.setRoom(new EnvSkyRoom("textures/skybox/default"));
        terrain = new EnvTerrain(terrainMap);
        terrain.setTexture("textures/mud.png");
        terrain.setScale(xScale, yScale, zScale);       
        env.addObject(terrain);
        
        Nifty nifty = env.getNiftyGUI();
        final ScreenController controller = this;

        Screen menuScreen = new ScreenBuilder("menu") {

            {
                
                controller(controller);

                layer(new LayerBuilder("layer") {

                    {                        
                        alignCenter();
                        valignCenter();
                        childLayoutCenter();
                        panel(new PanelBuilder() {

                            {                                
                                style("nifty-panel");
                                alignCenter();
                                valignBottom();
                                childLayoutVertical();
                                //height(percentage(60));
                                width(percentage(80));      
                                control(new LabelBuilder() {
                                    {
                                        style("nifty-label");
                                        text("Press T to exit terrain");
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
                layer(new LayerBuilder() {

                    {
                        alignCenter();
                        childLayoutCenter();
                        panel(new PanelBuilder() {

                            {
                                alignLeft();
                                valignBottom();
                                childLayoutCenter();
                                height(percentage(10));
                                width(percentage(100));
                                backgroundColor("#f60f005f");
                                control(new LabelBuilder() {
                                    {
                                        alignCenter();
                                        valignCenter();
                                        style("nifty-label");
                                        text("Press T to change terrain");
                                    }
                                });
                            }
                        });
                    }
                });
            }
        }.build(env.getNiftyGUI());

        env.getNiftyGUI().gotoScreen("ingame");
        env.setDefaultControl(true);

        env.setCameraXYZ(60, terrain.getHeight(60, 60) + 10, 60);

        while (env.getKey() != 1) {
            if (env.getKey() == Keyboard.KEY_T) {

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
    
    @SuppressWarnings("unchecked") 
    public void bind(Nifty nifty, Screen screen) {
        System.out.println("bind( " + screen.getScreenId() + ")");
        
        DropDown terrain = screen.findNiftyControl("terrain", DropDown.class);
        terrain.addItem(new String("termap1.png"));
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

    public void onStartScreen() {
        System.out.println("onStartScreen");
    }

    public void onEndScreen() {
        System.out.println("onEndScreen");
    }

    @NiftyEventSubscriber(id = "skybox")
    public void onSkyboxDropdownSelection(final String id, final DropDownSelectionChangedEvent<String> event) {
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
    }

    @NiftyEventSubscriber(pattern = "[xyz]Slider")    
    public void onScaleChange(String id, SliderChangedEvent newValue) {        
        System.out.println(id+" "+newValue.getValue());

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
        
    }

    public static void main(String args[]) {
        (new Game()).play();
    }
}