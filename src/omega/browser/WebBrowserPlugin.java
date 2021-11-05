package omega.browser;
import javafx.stage.Stage;

import javafx.application.Platform;

import omega.Screen;

import omega.plugin.event.PluginReactionEvent;

import java.net.URL;

import omega.plugin.Plugin;
import omega.plugin.PluginCategory;
public class WebBrowserPlugin implements Plugin{

	@Override
	public void registerReactions() {
		Screen.getPluginReactionManager().registerPlugin(this, PluginReactionEvent.EVENT_TYPE_IDE_INITIALIZED, (e)->{
			new Thread(()->{
				if(WebBrowserEnvironmentMaintainer.maintain(this) && Screen.getPluginManager().isPluginEnabled(Screen.getPluginManager().getPluginObject(getName()))){
					Platform.startup(()->{
						Stage stage = new Stage();
						stage.show();
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
