package omega.browser;
import omega.javafx.starter.PluginGenerator;

import java.net.URLClassLoader;
import java.net.URL;

import java.util.jar.JarFile;
import java.util.jar.JarEntry;

import omega.popup.NotificationPopup;

import java.io.File;

import java.util.LinkedList;
import java.util.Enumeration;

import java.awt.geom.RoundRectangle2D;

import javax.imageio.ImageIO;

import omega.utils.FileSelectionDialog;
import omega.utils.IconManager;

import omega.comp.TextComp;
import omega.comp.RTextField;

import omega.Screen;

import javax.swing.JDialog;
import javax.swing.JPanel;

import static omega.utils.UIManager.*;
import static omega.comp.Animations.*;
public class WebBrowserEnvironmentMaintainer extends JDialog{
	public TextComp titleComp;
	public TextComp iconComp;
	public TextComp cancelComp;
	public TextComp closeComp;
	public TextComp doneComp;
	public TextComp selectionComp;
	public RTextField pathField;
	public FileSelectionDialog fileDialog;
	
	public WebBrowserPlugin plugin;
	
	public static WebBrowserDataBase dataBase;
	
	public static NotificationPopup popup;
	
	public WebBrowserEnvironmentMaintainer(WebBrowserPlugin plugin){
		super(Screen.getScreen(), true);
		this.plugin = plugin;
		setUndecorated(true);
		setTitle("WebBrowser Environment Maintainer");
		setSize(400, 100);
		setLocationRelativeTo(null);
		setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
		setResizable(false);
		JPanel panel = new JPanel(null);
		panel.setBackground(c2);
		setContentPane(panel);
		init();
	}
	
	public void init(){
		fileDialog = new FileSelectionDialog(this);
		
		titleComp = new TextComp("Specify the path to JavaFX SDK", c2, c2, glow, null);
		titleComp.setBounds(0, 0, getWidth() - 30, 30);
		titleComp.setFont(PX14);
		titleComp.setArc(0, 0);
		titleComp.setClickable(false);
		titleComp.attachDragger(this);
		add(titleComp);
		
		closeComp = new TextComp(IconManager.fluentcloseImage, 20, 20, TOOLMENU_COLOR2_SHADE, c2, c2, this::dispose);
		closeComp.setBounds(getWidth() - 30, 0, 30, 30);
		closeComp.setArc(0, 0);
		add(closeComp);
		
		iconComp = new TextComp(IconManager.fluentwebImage, 48, 48, c2, c2, c2, null);
		iconComp.setBounds(5, 40, 50, 50);
		iconComp.setArc(0, 0);
		iconComp.setClickable(false);
		add(iconComp);
		
		try{
			iconComp.image = ImageIO.read(plugin.getImage());
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		selectionComp = new TextComp(IconManager.fluentplainfolderImage, 25, 25, "Select JavaFX SDK v17 or above", TOOLMENU_COLOR1_SHADE, c2, c2, this::browse);
		selectionComp.setBounds(60, 40, 25, 25);
		selectionComp.setArc(0, 0);
		add(selectionComp);
		
		pathField = new RTextField("Browse or Type", "", TOOLMENU_COLOR2, c2, TOOLMENU_COLOR3);
		pathField.setBounds(100, 40, getWidth() - 110, 25);
		pathField.setFont(PX14);
		add(pathField);
		
		doneComp = new TextComp("Done", TOOLMENU_COLOR1_SHADE, back2, TOOLMENU_COLOR1, ()->{
			String path = pathField.getText();
			File f = new File(path);
			if(!f.exists() || !f.isDirectory()){
				titleComp.setText("Invalid Directory Path");
			}
			else
				dispose();
		});
		doneComp.setBounds(pathField.getX(), 70, 80, 25);
		doneComp.setFont(PX14);
		doneComp.setArc(0, 0);
		add(doneComp);
		
		cancelComp = new TextComp("Cancel", TOOLMENU_COLOR2_SHADE, back2, TOOLMENU_COLOR2, this::dispose);
		cancelComp.setBounds(pathField.getX() + pathField.getWidth() - 80, 70, 80, 25);
		cancelComp.setFont(PX14);
		cancelComp.setArc(0, 0);
		add(cancelComp);
	}
	
	public void browse(){
		LinkedList<File> files = fileDialog.selectDirectories();
		if(!files.isEmpty()){
			dataBase.javafxSDKPath = files.get(0).getAbsolutePath();
			dataBase.save();
		}
		super.dispose();
	}
	
	@Override
	public void dispose(){
		if(dataBase.javafxSDKPath.equals("")){
			dataBase.javafxSDKPath = pathField.getText();
			dataBase.save();
		}
		super.dispose();
	}
	
	public void setMessage(String text){
		popup.message(text);
		popup.build();
		popup.showIt();
	}
	
	public void setProgress(String text){
		popup.shortMessage(text);
		popup.build();
		popup.showIt();
	}
	
	public boolean loadOnClasspath(LinkedList<File> jars){
		try{
			return PluginGenerator.genFXPlugin(jars, this);
		}
		catch(Exception e) {
			popup.message("Unable to Prepare Classpath!");
			e.printStackTrace();
		}
		return !jars.isEmpty();
	}
	
	public boolean loadJavaFX(){
		try{
			File libDir = new File(dataBase.javafxSDKPath + File.separator + "lib");
			File[] F = libDir.listFiles((f)->f.getName().endsWith(".jar"));
			if(F == null || F.length == 0){
				setMessage("No JavaFX Modules were found!");
				dataBase.javafxSDKPath = "";
				setVisible(true);
				loadJavaFX();
				return false;
			}
			setMessage("Loading JavaFX Modules");
			LinkedList<File> files = new LinkedList<>();
			for(int i = 0; i < F.length; i++){
				File fx = F[i];
				setProgress((((i + 1) * 100) / F.length) + "%");
				setMessage(fx.getName());
				files.add(fx);
			}
			
			if(loadOnClasspath(files)){
				String oldPath = System.getProperty("java.library.path");
				oldPath = (oldPath != null) ? (oldPath + ":") : "";
				System.setProperty("java.library.path", oldPath + libDir.getAbsolutePath());
				popup.dispose();
				return true;
			}
		}
		catch(Exception e){
			setMessage("An internal error occured!");
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean maintain(WebBrowserPlugin plugin){
		WebBrowserEnvironmentMaintainer mainter = new WebBrowserEnvironmentMaintainer(plugin);
		dataBase = WebBrowserDataBase.prepareDataBase();
		if(dataBase.javafxSDKPath.equals("") || !new File(dataBase.javafxSDKPath).exists())
			mainter.setVisible(true);
		if(!dataBase.javafxSDKPath.equals("")){
			popup = NotificationPopup.create(Screen.getScreen())
			.size(400, 120)
			.dialogIcon(mainter.iconComp.image)
			.title("WebBrowser Plugin")
			.message("Loading JavaFX on Classpath", TOOLMENU_COLOR5)
			.shortMessage("0%", TOOLMENU_COLOR1)
			.build()
			.locateOnBottomLeft()
			.showIt();
			
			return mainter.loadJavaFX();
		}
		return true;
	}
}
