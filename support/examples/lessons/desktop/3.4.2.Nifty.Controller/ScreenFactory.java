import de.lessvoid.nifty.*;
import de.lessvoid.nifty.screen.*;
import de.lessvoid.nifty.builder.*;
import de.lessvoid.nifty.controls.*;
import de.lessvoid.nifty.controls.slider.builder.*;

/**
 * The job of ScreenFactory is to construct different Screens
 */
public class ScreenFactory
{
    /**
     * Build a particular screen.  Implmented as a static method to make it easier to call.
     */
    public static void buildScreen(String id, final ScreenController control, Nifty nifty) 
    {
        ScreenBuilder builder = null;
        
        if (id.equals("HelloScreen")) {
            builder = new ScreenBuilder(id) {
                {
                    // Attach the controller to this screen.
                    controller(control);
                    layer(new LayerBuilder() {
                        {
                            // Layout the child elements in the middle
                            childLayoutCenter();
                            // Each layer can have one or more panel
                            panel(new PanelBuilder() {
                                {
                                    // We simply have an empty panel with a 
                                    // translucent background
                                    alignCenter();
                                    valignCenter();
                                    childLayoutCenter();
                                    
                                    // Take up the entire screen space
                                    width(percentage(100));
                                    height(percentage(100));        
                                    
                                    // Put a slider in the middle of the screen
                                    control(new SliderBuilder("mySlider", false) {
                                        {
                                            alignCenter();
                                            valignCenter();
                                        }
                                    });
                                    
                                    // color is expressed using the RGBA notation
                                    backgroundColor("#ffffff88");
                                }                            
                            });
                        }
                    });          
                }
            };
            
            builder.build(nifty);
        }
     
    }
}
