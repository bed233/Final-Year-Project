# Final Year Project Diary

This is where I will document the work I have done for the project on a day-to-day basis

05/10/2022
Cloned the GitLab Repository and Created my Diary.md and Created my Planning folder and moved relevant files such as my project plan into the folder.

06/10/2022
Completed My Project plan and uploading Submitted version to Repo.

11/10/2022
Found a couple of FOSS Anti-Malware for Android that I could potentially use for Malware Detection in my project. Currently (and over the next couple days) comparing the 2 apps to each other to see which one is better suited for my project and whether they both tackle Anti-Malware in different ways and if there is any benefit to implementing to finding a way to implement both into my project. For example, from my understanding, Hypatia focuses more on individual files on the device such as pictures whereas LibreAV focuses more on scanning the actual apps on the device. This means that theoretically, there is a security benefit to implementing both Open-Soruce malware scanners in my Security Suite. Currently am waiting on a response from the organizors as to how to correctly 'fork' the Open-Source Projects from Github to my Gitlab Repository.

13/10/2022
Have Received guidance on how to implment Open Source Projects from Github into my Gitlab. 'forked' the 2 FOSS Anti-Malware for Android Applications into my gitlab and added them as a submodule of my main project folder. Added my supervisor and the organizers to both submodules to ensure everyone who needs access has access. Now that this has been completed, I am able to start looking through the FOSS apps and understand how they work and how I could adapt them to work in my security suite. I have now setup Android Studio and both apps in Android Studio and am able to run both sucessfully on a virtual android device running latest API of android. Now will go through each version of android to try and decide what is the earliest version of android I can work on whilst keeping everything compatible.

16/1o/2022 
Have Tested both FOSS implementations for Malware Detection on multiple version of Android and determined that LibreAV will run on Android 4.1 and above with Hypatia running at Android 5 and above. Using these findings, I will be aiming to use Android 5 for the remainder of my project. This will ensure that I am able to support 98.8% of Android Devices according to Google API Version Distribution Chart.

Have also begun going through the code of the FOSS and commenting in order to help me understand the code as their are no comments in the code. This should be completed in the next couple days at most.

18/10/2022
Have finished Commneting both FOSS projects to a point where I can understand and modify in order for them to work within my security Suite. Have also Created UML class diagrams for both which should help with this process. Have now begun thinking about how i am going to implement both together.

19/10/2022
Have begun (nearly completed) Implementing a Progress Bar into the Hypatia FOSS project. This will allow the user to see the progress of the scan and also allow me to implement a cancel button to stop the scan if the user wishes to do so. I chose to do this as I belive (for now) that a progress bar would be more effective than the orignal app's implementation of just printing out the logs into a Textview on the screen. Using a progress bar on both will also allow me potentially merge their implementations into one and have a single progress bar for both scans aka Hypatia will run up to 50% on the bar with LibreAV finishing the bar off. This means I can implement both into 1 seamless process for the user. Outside of code, I am still drawing up implementation ideas as to what is the best way to merge them together whilst making everything as cohesive as possible for the user.

20/10/2022
Completed corretly implementing Progress Bar in Hypatia UI. Struggled a little bit as was trying to find where the relevant data I need to track progress was. Over the weekend, will attempt to merge Hypatia and LibreAV together, However if proven difficult in time allocated, shall focus on getting 1 app to work as a proof of concept with a unified UI in order to save time.

22/10/2022
Decided to start over in implementing both apps into 1 app as my previous plan was taking too long. So now i have started my new implementation where I am making progress. Have also begun thinking about overall UI Design.

24/10/2022
Have created a UI page that is able to start the activities of another 'app package' and have implemented Hypatia. However, am yet to implement LibreAV however this should be completed by tommorow as I have streamlined the process and now know exactly what I am doing. Once LibreAV is implemented, I will focus slightly more on the UI looking a bit more conisistent and more user friendly before Wednesday. Have also begun looking into open source implementations of an app that is able to modify app permissions. I understand that more likely that not, I would need to make my security suite an admin on the device it is running on for this. I know how I can retrieve a list of all apps and then a list of each permission on each app. Just have to look into how I can change permission without having to send the user to the Android Settings page of that app. This shouldn't take too long and I am hoping I can quickly get back on track with my project timeline within the next week or so.