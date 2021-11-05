package omega.browser;
import java.io.File;

import omega.database.DataBase;
import omega.database.DataEntry;
public class WebBrowserDataBase extends DataBase{
	public static final File BROWSER_DATA_FILE = new File(".omega-ide" + File.separator + "browser-plugin" + File.separator + ".browser-prefs");
	
	public String javafxSDKPath = "";
	
	public WebBrowserDataBase(){
		super(BROWSER_DATA_FILE.getAbsolutePath());
		
		DataEntry entry = getEntryAt("JavaFX SDK Path", 0);
		if(entry != null){
			javafxSDKPath = entry.getValue();
		}
	}

	@Override
	public void save(){
		clear();
		updateEntry("JavaFX SDK Path", javafxSDKPath, 0);
		super.save();
	}
	
	public static WebBrowserDataBase prepareDataBase(){
		if(!BROWSER_DATA_FILE.getParentFile().exists())
			BROWSER_DATA_FILE.getParentFile().mkdirs();
		return new WebBrowserDataBase();
	}
}
