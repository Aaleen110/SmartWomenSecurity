# Smart Women Security

The initiative of building this application is to provide security to women.

| Splash      | Home      | Send Location      | Emergency Call      |  
|------------|-------------|------------|-------------|
| <img src="/../master/splash.jpg" width="200">  | <img src="/../master/home.jpg" width="200"> | <img src="/../master/send_location.jpg" width="200"> | <img src="/../master/emergency_call.jpg" width="200"> |


## Overview
This application designed to provide security to women. The main purpose of this application is to provide the awareness on the time of critical situation for women. In today’s world, it is not safe for a person to travel alone at night especially for women. It is better to reduce chances of becoming a victim of violent crime (robbery, sexual assault, rape, domestic violence) and to get help from our loved ones. Smart women security diminishes the risk and brings assistance whenever required. 

## Modules
The application is packed with the following 6 features to provide maximum security at difficult times.

1. SEND MESSAGE

In this module the user needs to select some contacts from android contact app and add them to hot contacts list, which will be shown below. And once the contacts are added and the round red button captioned “SEND” is pressed a customized text message is sent to the contacts added in contact list.  The user is allowed to customize the text she wishes to send by clicking on the option menu.

2. SEND GPS LOCATION

In this module the user needs to select some contacts from android contact app and add them to hot contacts list, which will be shown below. And once the contacts are added and the round red button captioned “SEND GPS” is pressed a customized text message is sent to the contacts added in contact list including the Gps location of the user. So when the hot contact receives the message he will also receive a google map location.  The user is allowed to customize the text she wishes to send by clicking on the option menu.

3. SPY CAMERA

Spy camera module is a good tool if you want to spy click pictures of someone. The main advantage of this module is that the screen remains black as if the screen is locked and user can click pictures on Click of the screen. The blank screen is achieved by making the surface view size 1dp by 1dp and requesting window feature NOTITLE. You need to add Camera permission to click pictures and Write_External_Storage permission in manifest in order to store the pictures in gallery.

4. SPY VIDEO

Spy video module is a good tool if you want to spy someone. The main advantage of this module is that the screen remains black as if the screen is locked and user can record video on Click of the screen and stop the video on another click. The blank screen is achieved by making the surface view size 1dp by 1dp and requesting window feature NOTITLE. You need to add Camera permission to record the video and Write_External_Storage permission in manifest in order to store the videos in external storage directory.

5. SPY AUDIO

Spy Audio module is a good tool if you want to spy someone. The main advantage of this module is that the screen remains black as if the screen is locked and user can record audio on Click of the screen and stop the audio on another click. The blank screen is achieved by making the surface view size 1dp by 1dp and requesting window feature NOTITLE. You need to add RECORD_AUDIO permission to record the video and Write_External_Storage permission in manifest in order to store the audio files in external storage directory. We created object of Media Recorder and called method mediarecorder.start () and mediarecorder.stop () in order to start and stop the video.

6. EMERGENCY CALL

In this module the user needs to select some contacts from android contact app and add them to emergency contacts list, which will be shown below. And once the contacts are added, the user can shake the screen to make a call to the contacts added first in contact list.  The user is allowed to also make a call by clicking items in the contact list.
