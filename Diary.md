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

## 16/1o/2022 
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