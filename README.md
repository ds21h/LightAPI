# LightAPI

This module exposes the REST API of the Lights application. It runs as a servlet in a webserver (TomEE) on the same server as LightSwitch.
For an overview please see repository Lights.

The project structure is that of NetBeans 12.4/Maven.

This repository is the successor of repository LightAPI---Old

History:

Version 1.2.1 - 06-11-2021
 - Recompiled with LightSupport version 1.2.1

Version 1.2 - 12-09-2021
 - Upgraded to Netbeans 12.4/Maven/JDK 15/Java EE 8
 - Java at version 11 for deployment on Raspbian
 - Must be deployed on TomEE instead of plain TomCat

Version 1.1.1 - 18-08-2021
 - Recompiled with LightSupport version 1.1.1
 - Deleted old commented-out code

Version 1.1 - 28-06-2021
 - Uses upgraded LightSupport.jar (Version 1.1)
 - Deleted obsolete Setting updates. 

Version 1.0.1 - 10-01-2019
 - Bug in SettingR removed (getJSONObject replaced by optJSONObject)

Version 1.0 - 09-12-2018
 - Created from jb.licht.api version 2.0, not compatible with it!
 - Translated to English
 - URI also changed to English
 - All APIs translated to English
 - Settings communication changed to JSON
 - Requires package jb.light.support
