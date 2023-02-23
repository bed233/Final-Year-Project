# Final Year Project Diary

This is where I will document the work I have done for the project on a day-to-day basis

## 05/10/2022
Cloned the GitLab Repository and Created my Diary.md and Created my Planning folder and moved relevant files such as my project plan into the folder.

## 06/10/2022
Completed My Project plan and uploading Submitted version to Repo.

## 11/10/2022
Found a couple of FOSS Anti-Malware for Android that I could potentially use for Malware Detection in my project. Currently (and over the next couple days) comparing the 2 apps to each other to see which one is better suited for my project and whether they both tackle Anti-Malware in different ways and if there is any benefit to implementing to finding a way to implement both into my project. For example, from my understanding, Hypatia focuses more on individual files on the device such as pictures whereas LibreAV focuses more on scanning the actual apps on the device. This means that theoretically, there is a security benefit to implementing both Open-Soruce malware scanners in my Security Suite. Currently am waiting on a response from the organizors as to how to correctly 'fork' the Open-Source Projects from Github to my Gitlab Repository.

## 13/10/2022
Have Received guidance on how to implment Open Source Projects from Github into my Gitlab. 'forked' the 2 FOSS Anti-Malware for Android Applications into my gitlab and added them as a submodule of my main project folder. Added my supervisor and the organizers to both submodules to ensure everyone who needs access has access. Now that this has been completed, I am able to start looking through the FOSS apps and understand how they work and how I could adapt them to work in my security suite. I have now setup Android Studio and both apps in Android Studio and am able to run both sucessfully on a virtual android device running latest API of android. Now will go through each version of android to try and decide what is the earliest version of android I can work on whilst keeping everything compatible.

## 16/10/2022
Have Tested both FOSS implementations for Malware Detection on multiple version of Android and determined that LibreAV will run on Android 4.1 and above with Hypatia running at Android 5 and above. Using these findings, I will be aiming to use Android 5 for the remainder of my project. This will ensure that I am able to support 98.8% of Android Devices according to Google API Version Distribution Chart. Have also begun going through the code of the FOSS and commenting in order to help me understand the code as their are no comments in the code. This should be completed in the next couple days at most.

## 18/10/2022
Have finished Commneting both FOSS projects to a point where I can understand and modify in order for them to work within my security Suite. Have also Created UML class diagrams for both which should help with this process. Have now begun thinking about how i am going to implement both together.

## 19/10/2022
Have begun (nearly completed) Implementing a Progress Bar into the Hypatia FOSS project. This will allow the user to see the progress of the scan and also allow me to implement a cancel button to stop the scan if the user wishes to do so. I chose to do this as I belive (for now) that a progress bar would be more effective than the orignal app's implementation of just printing out the logs into a Textview on the screen. Using a progress bar on both will also allow me potentially merge their implementations into one and have a single progress bar for both scans aka Hypatia will run up to 50% on the bar with LibreAV finishing the bar off. This means I can implement both into 1 seamless process for the user. Outside of code, I am still drawing up implementation ideas as to what is the best way to merge them together whilst making everything as cohesive as possible for the user.

## 20/10/2022
Completed corretly implementing Progress Bar in Hypatia UI. Struggled a little bit as was trying to find where the relevant data I need to track progress was. Over the weekend, will attempt to merge Hypatia and LibreAV together, However if proven difficult in time allocated, shall focus on getting 1 app to work as a proof of concept with a unified UI in order to save time.

## 22/10/2022
Decided to start over in implementing both apps into 1 app as my previous plan was taking too long. So now i have started my new implementation where I am making progress. Have also begun thinking about overall UI Design.

## 24/10/2022
Have created a UI page that is able to start the activities of another 'app package' and have implemented Hypatia. However, am yet to implement LibreAV however this should be completed by tommorow as I have streamlined the process and now know exactly what I am doing. Once LibreAV is implemented, I will focus slightly more on the UI looking a bit more conisistent and more user friendly before Wednesday. Have also begun looking into open source implementations of an app that is able to modify app permissions. I understand that more likely that not, I would need to make my security suite an admin on the device it is running on for this. I know how I can retrieve a list of all apps and then a list of each permission on each app. Just have to look into how I can change permission without having to send the user to the Android Settings page of that app. This shouldn't take too long and I am hoping I can quickly get back on track with my project timeline within the next week or so.

## 26/10/2022
Completed Merging LibreAV classes and resource files with Hypatia in order to get both implementatios to work in 1 app rather than have 2 seperate apps. Have run into an error using this app however where LibreAV is only showing the splash screen then crashing. I have a potential fix for this problem though which I will be trying tommorow. The UI currently consists of 2 buttons in the centre of the screen, 1 for hypatia and 1 for libreAv and the user decides which one to run. I have not yet implemented a back button in Hypatia/libreAV meaning user needs to restart app to change scanner. I am looking to add this soon however is not a priority due to it only being a proof of Concept. As the Malware Detection aspect is most likely to be a big part of my interim presentation, I will be working on refining its UI throughout the rest of the weeks however this is not a priority until closer to the interim presentation.

## 27/10/2022
Have resolved issue where LibreAV would crash after showing the splash screen. So now both Implementations are working succesfully in 1 app. 

## 28/10/2022
Modified the UI of Hypatia slightly to be more consistent with LibreAV and the main menu. Have also removed the Splash screen for LibreAV which has made the app feel faster. I am in a position where i can declare this proof of concept complete and will be shifting almost my full attention to creating my proof-of-concept app for Viewing and Modifying App Permissions. This shouldn't be too difficult as LibreAV already gives me the foundation of this feature as it is able to seach apps and retrieve all their permissions meaning that viewing permissions is pretty much complete. Just need to think of implementation that will allow user to edit permissions potentially having to make the app as a administrator. I am hoping to make progress on this over the weeekend and be able to start my Active Sensor Usage POC early next week as well as make progress on my interim report as well as my early deliverable.

## 29/10/2022
After some Preliminary Research for Viewing and Modifying App Permissions, I'm slowly edging to the conclusion that an app that can modify app permissions may not be possible without the user having a rooted device or using ABD. Which may make this avenue I would need to simplify or drop. I am considering still pursuing a simplified version of this concept where the app will allow the user to view their permissions as well as group apps by permissions and if the user would like to change any permissions, the app could potentially redirect them to the built-in Android App Settings page where they can change the permissions from Settings which will have the ability to do this. I still feel like this could potentially add value to the user as they will be able to see the permissions for all their apps rather than click on one at a time. However as these permission lists can be very long, I would need to think long and hard about the most effective UI/Filtering System.

## 31/10/2022
Completed the UI for the PermissionsPOC App. Was a bit more complex than expected as need to make a card for each app that is clickable. I achieved this by using a card view with each card having a picture of the app Icon and some information about the app such as the name and package name. Have also nearly completed the 1st couple sections of my Interim Report (Timescale, Abstract, Aims & Objectives). I believe I will neeed to clarify a new up to date timeline for 2nd term to reflect the slightly slow progres I have been making. That being said, i am still on track to catch up, i just feel like I need to be slightly more generous with my timescale of each task now that I have a better view of how long each task will roughly take.

## 03/11/2022
The Backend for the PermissionsPOC app is taking a bit longer than expected. I have finished implementing all the necessary java code to make the views work as well as any adapters needed for the data regarding the apps. Just need to make it all work together. Really Hoping I can get this POC done by the end of tommorow at the latest as it is taking longer than expected. After the completion of this POC, I will instantly start creating my App Access Control POC which will lock certain apps until the user authenticates themselves. Hoping this would be done as well by early week at the latest. I am continuing to work on my Interim Report and just about to finish Section 1 and 2 and move on to writing my report about which features I could implement in my Security Suite.

## 06/11/2022
Have finally completed my Proof of Concept for App Permissions. This POC has taken a little longer than expected and will consider reorganising my timeline in order to provide myself more time per Proof Of Concept. However I have learnt a lot about ANdroid development which i didnt know and am hoping this new found knowledge will speed up development. Will attempt to complete the App Access Control POC as quick as possible (Thursday at the latest) and then I can switch my focus to the report, demo and presentation. Depending on how quick i can get my 3rd POC complete. I may attempt to complete a POC for file Encryption on Android but this is not confirmed. Have also bookmarked a few open source git hub projects that relate to app access control. The App Access control will most probably work by showing a lockscreen to the user on the applications they choose to require verification.

## 07/11/2022
Realised a mistake when sorting out my Resource files for my App Permissions in order to allow the app to work on Android 5 Devices. Have researched into ways I could implement App Access Control. As there is no direct way to listen to when an app open or closes so, from what i can tell, is to use a Usage Stats Manager to get the app that is at the top of the launcher which is most likely to be the app that the user has just opened. 

## 16/11/2022
Have begun writing my background theory (which features I could implement in my Security Suite) and have made good progress on this report. Have looked into possible implementations and have begun programming my POC for App lock. This will most likely be my final POC of this term as I focus more on my report and my Demo and Presentation.

## 17/11/2022
Made progress on my 3rd Proof of Concept, is taking a longer than expected as I didnt anticipate how different usage stats worked between different versions of android. I am now considering looking at using an Open Source project to fulfill my app access control.

## 18/11/2022
I have found potential candidates that I could use for app acccess control from github and just going through their feature set and evaluating their suitability as well as ensuring there are no licensing issues with the rest of my project.

## 19/11/2022
Have settled on an Open Source Implementation of App access control that I will be using as part of my Proof of Concepts. 

## 20/11/2022
Completed my early delvierable report for my interim report and have begun looking for an open-source implementation/ way I could implement an app that alerts the user when any service on their device is using mic or camera (Active Sensor Usage). Have multiple section of my report left to write and am trying to get as much completed before draft look with my supervisor.

## 21/11/2022
Made more progress on my FYP interim report. Have begun and completed around half of my 2nd Section (Technical Achievements). Have questions about the report which I shall ask to my supervisor in Our meeting on Wednesday. Will get the demo of the Permission manager ready in order to show my supervisor in the same meeting.

## 22/11/2022 - 25/11/2022
Continued work on Final Year Report 

## 28/11/2022
Begun New project on Android Studio to merge all of Proof Of Concept's together. Hoping I can get all merged by wednesday where I can record demo video on Thursday ready for submission.
Continued work on Final Year Project

## 29/11/2022
Continued work on Final Year Report

## 01/12/2022
Completed majorirty of Proof Of Concept merging and completed report and demo video.

## 22/12/2022
Realised that I forgot to actually push my Interim Submission and AppLockPOC to the Gitlab so have done that. Over the past few days I have been looking into potentially using Google Flutter in order to create my UI to see if this would make the creation process any quicker and make the UI look more seamless and clean.

## 09/01/2023
Have continued to learn Google Flutter since last entry. Have now Gotten Maxlock to a state where it can be merged with the rest of the modules without causing any issues. Also modified MaxLock in order to allow it to be compatible with Android 5 and up instead of android 6.1 and above. Will Finish Merging modules into 1 app tommorow and then full focus on UI.

## 10/01/2023
Have successfully merged all modules into 1 app with a basic menu to navigate between them. Have tested this with Android versions ranging from Android 5 to 13. Am going to add OnBackPressed functions before merging branch just to make navigating back to the main menu possible as well as change colours back to the intended colours rather than red. Still considering whether to commit to Flutter or not and am hoping to make decision by end of the week. I would only use Flutter for the main menu however it might be easier to use standard Android UI methods in order to keep everything consistent.

## 12/01/2023
Have fixed bug where pressing backspace from any of the home pages of each module to allow it to work.

## 14/01/2023
Been researching into how I can implement a way for the user to pick which folders they want Hypatia to scan rather than have Hypatia scan all the files on the device which takes a lot of time and battery power on the device which is an issue I ran into when testing Hypatia on my own personal device full of files rather than on the relatively empty emulator.

## 15/01/2023
Debugging Hypatia in order to find out where I could modify which folders for the scanner to include in it's scope. This is determined in it's main Activity where I can modify it to select specific folders. Still researching into how a user can select folders to scan but until then considering changing scope to include specific folders most likely to store downloaded files from the browser such as the Downloads and Documents Folder.

## 17/01/2023 -  22/01/2023
Researched into various methods and libraries I could use in order to allow the user to select specific folders to scan on Hypatia.

## 25/01/2023 - 29/01/2023
Created Proof Of Concept's of verious selectors but was unable to figure out how to pass a list of folders back to app. Then realised that I was trying to over engineer the solution and could just create a simple alert dialog with a list of User Folders (Documents, Downloads...) within the app and simplify the process significantly and avoid the need for a new library.

## 30/01/2023
Completed Implementation of the above and Merged Branch into Main.

## 31/01/2023 - 02/02/2023
Realised that my Permission manager scanner needed to be optimized as pressing the button on the main menu can lead to the app to crash due to the scanner not being able to finish in time. I have moved when the scanner starts to as soon as the app is ran on the device which has reduced the delay between the user interacting to the list being shown.

## 03/02/2023
Have begun working on the UI for the app and making the app look clean. Will start with the Permission Manager as it is currently easiest to modify in order to create a base theme. Have also begun using Figma which will with implementing Google's Material 3 design language.

## 05/02/2023
Have realised that using Figma would of slowed down my development so have limited use to jus organising components rather than implementing Figma into Android Studio.

## 07/02/2023
Created and added Toolbar to all aspects of the Permission Viewer to improve design. Redesigned the List page in order to make the list of apps look nicer and more understandable to look out

## 08/02/2023
Added a Back arrow to allow user to go back to main menu. Redesigned the App Details to make it easier to read for the user. Added a refresh button to the toolbar which allows the user to update their list of apps.

## 09/02/2023
Completed Branch for turning Permsision Viewer into Material UI. Have begun the same process on LibreAV

## 10/02/2023 - 11/02/2023
Completed desgining new LibreAV and begun implementing design into the app. Updated the Result List in order to match new design and be more material.

## 12/02/2023
Begun Modifying Toolbar in order to be more in line with rest of app and have modified Result list more to make Safety Classifications more obvious

## 13/02/2023
Completed Result list and Toolbar and have also modified the scan progress bar to match Google's material design Guidelines
and look more consistenw tih the rest of the app.

## 14/02/2023 - 17/02/2023
Completed the UI for the App Scanner module of my security suite. Have begun working on the file scanner UI which will take a lot of work.

## 18/02/2023/ - 22/02/2023
Created progress bar that links up with scanner to show user the progress of the scanner and completed the majority of the file scanner's UI.