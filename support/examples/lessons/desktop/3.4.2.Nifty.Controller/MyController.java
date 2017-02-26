import env3d.advanced.EnvNode;
 
import de.lessvoid.nifty.*;
import de.lessvoid.nifty.screen.*;
import de.lessvoid.nifty.controls.*;

public class MyController implements ScreenController
{    
    private Game game;
    
    public MyController(Game game) 
    {
        this.game = game;
    }           

    // We use java anootaion to specify that this method will be called
    // when the object id "mySlider" is changed.
    //
    // By using java annotation, the actual name of the method does not
    // matter at all!  The result is that we can use have different methods
    // to respond to different events all inside the same controller.
    @NiftyEventSubscriber(id = "mySlider")
    public void onSliderChange(String id, SliderChangedEvent event) 
    {
        game.getNode().setScale(event.getValue()/20);
    }
    
    public void bind(Nifty nifty, Screen screen) 
    {        
    }
    
    public void onStartScreen()    
    {
    }
    
    public void onEndScreen() 
    {
    }    
}
