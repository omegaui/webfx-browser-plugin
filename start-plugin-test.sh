#!/bin/sh
echo ">>>>>Injecting WebBrowserPlugin! ..."

cd out
cp "/home/ubuntu/Documents/Omega Projects/Web Browser Plugin/out/omega.browser.WebBrowserPlugin.jar" "/home/ubuntu/Documents/Omega IDE/build/.omega-ide/plugins/omega.browser.WebBrowserPlugin.jar"
cd "/home/ubuntu/Documents/Omega IDE/build"

echo ">>>>>Launching NEW Instance of v2.2"
java -jar "Omega IDE.jar"
echo ">>>>>Exiting Shell"

exit

