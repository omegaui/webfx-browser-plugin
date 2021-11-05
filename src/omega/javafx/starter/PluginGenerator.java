package omega.javafx.starter;
import omega.utils.systems.creators.FileOperationManager;

import omega.browser.WebBrowserEnvironmentMaintainer;

import omega.popup.NotificationPopup;

import omega.Screen;

import omega.instant.support.java.JavaSyntaxParser;

import java.util.LinkedList;
import java.util.Enumeration;

import java.util.zip.ZipOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import java.net.URL;

import omega.plugin.PluginCategory;
import omega.plugin.Plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.PrintWriter;
public class PluginGenerator implements Plugin{

	public static void add(File file, ZipOutputStream zos, String parentPath){
		try{
			String name = file.getAbsolutePath();
			name = name.substring(parentPath.length() + 1);
			ZipEntry entry = new ZipEntry(name);
			entry.setCompressedSize(-1);
			zos.putNextEntry(entry);
			InputStream ins = new FileInputStream(file);
			while(ins.available() > 0){
				zos.write(ins.read());
			}
			zos.flush();
			zos.closeEntry();
			ins.close();
		}
		catch(Exception e){
			//e.printStackTrace();
		}
	}
	
	public static void add(InputStream ins, ZipOutputStream zos, ZipEntry entry){
		try{
			zos.putNextEntry(entry);
			while(ins.available() > 0){
				zos.write(ins.read());
			}
			zos.flush();
			zos.closeEntry();
			ins.close();
		}
		catch(Exception e){
			//e.printStackTrace();
		}
	}
	
	public static boolean genFXPlugin(LinkedList<File> files, WebBrowserEnvironmentMaintainer mainter){
		try{
			File sdkFile = new File(".omega-ide" + File.separator + "plugins" + File.separator + "omega.javafx.starter.PluginGenerator.jar");
			if(!sdkFile.exists()){
				mainter.setMessage("Generating JavaFX Plugin ...");
				File targetZipFile = sdkFile;
				
				targetZipFile.delete();
				
				ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(targetZipFile));
				LinkedList<ZipEntry> addedEntries = new LinkedList<>();
				for(File fx : files){
					mainter.setProgress((((files.indexOf(fx) + 1) * 100) / files.size()) + "% Adding " + fx.getName());
					if(fx.getName().endsWith(".jar")){
						ZipFile libFile = new ZipFile(fx);
						Enumeration entries = libFile.entries();
						while(entries.hasMoreElements()){
							ZipEntry entry = (ZipEntry)entries.nextElement();
							if(addedEntries.contains(entry))
								continue;
							entry.setCompressedSize(-1);
							add(libFile.getInputStream(entry), zos, entry);
							addedEntries.add(entry);
						}
						libFile.close();
					}
				}

				mainter.setProgress("Adding JavaFX Plugin Information ...");

				ZipEntry entry = new ZipEntry("omega/javafx/starter/PluginGenerator.class");
				add(PluginGenerator.class.getResourceAsStream("/omega/javafx/starter/PluginGenerator.class"), zos, entry);

				mainter.setProgress("Finishing Plugin Craft!");
				
				zos.finish();
				zos.close();
				
				mainter.setMessage("A Restart is Required");
				mainter.setProgress("After Restart Enable JavaFX Plugin & Again Restart ;)");
				return false;
			}
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
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
		return "JavaFX SDK";
	}
	@Override
	public String getVersion() {
		return "v2.2";
	}
	@Override
	public String getPluginCategory() {
		return PluginCategory.SDK;
	}
	@Override
	public String getDescription() {
		return "JavaFX Plugin Generated by WebBrowserPlugin";
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
		return "9 MB";
	}
	@Override
	public URL getImage() {
		return getClass().getResource("/fluent-icons/icons8-java-48.png");
	}
}
