package omega.browser;
import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

import javafx.application.Platform;

import omega.Screen;

import omega.plugin.event.PluginReactionEvent;

import java.net.URL;

import omega.plugin.Plugin;
import omega.plugin.PluginCategory;
public class WebBrowserPlugin implements Plugin{
	public WebBrowserPane webBrowserPane;
	public BufferedImage image;
	
	@Override
	public void registerReactions() {
		Screen.getPluginReactionManager().registerPlugin(this, PluginReactionEvent.EVENT_TYPE_IDE_INITIALIZED, (e)->{
			new Thread(()->{
				if(WebBrowserEnvironmentMaintainer.maintain(this) && Screen.getPluginManager().isPluginEnabled(Screen.getPluginManager().getPluginObject(getName()))){
					Platform.startup(()->{
						webBrowserPane = WebBrowserPane.newInstance();
					});
				}
			}).start();
		});
	}
	
	@Override
	public boolean init() {
		return true;
	}
	
	@Override
	public boolean enable() {
		try{
			if(image == null)
				image = ImageIO.read(getClass().getResource("/icons8-web-design-48.png"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		Screen.getScreen().getToolMenu().toolsPopup.createItem("New Browser Tab", image, ()->{
			Screen.getScreen().getTabPanel().addTab("WebBrowser", "webfx-tab-1", image, webBrowserPane, "", null);
		});
		return true;
	}
	@Override
	public boolean disable() {
		return true;
	}
	@Override
	public String getName() {
		return "WebFX Browser";
	}
	@Override
	public String getVersion() {
		return "v2.2";
	}
	@Override
	public String getPluginCategory() {
		return PluginCategory.UTILITY;
	}
	@Override
	public String getDescription() {
		return "A Web Browser Plugin written Using JavaFX";
	}
	@Override
	public String getAuthor() {
		return "Omega UI";
	}
	@Override
	public String getLicense() {
		return "GNU GPL v3";
	}
	@Override
	public boolean needsRestart() {
		return false;
	}
	@Override
	public String getSizeInMegaBytes() {
		return "0.12 MB";
	}
	@Override
	public URL getImage() {
		return getClass().getResource("/icons8-web-design-48.png");
	}
	
}
