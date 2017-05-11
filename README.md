# Graphrec

## Description
Mobile app that recognizes functions from looking at photos of their graphs, by using machine learning technology. The idea is that the user should be able to use a mobile phone to take a picture of a graph of a function. This graph can be written on a whiteboard or a blackboard, it can also be a painting or photograph! Then the phone will send it to a server which will classify which type of graph it is, e.g. ascending linear, descending linear, exponential, polynomial, etc. The server will respond with the classification to the mobile which will display it to the user.

### Components
Graphrec is composed of three main components: the mobile application, the server and the neural network.
#### Mobile application
The idea is to have an android application that can take photos and crop them to a fixed size. It should then be able to send these photos to a remote server.
#### The server
The server should receive these cropped images, let a trained neural network classify them with some probability and then send back this result of the evaluation to the mobile application.
#### The neural network
The last component would be a method on the server that trains the neural network using supervised learning. It would automatically generate pictures of graphs to be added to the training data set. All training could be done prior to getting the server up and running or procedurally as the server runs. The most basic function classes for the neural network to differentiate between could be ascending versus descending linear functions of the form `ax+b` where `a` is greater than respectively lesser than `0`.

## Architecture
![Graphrec architecture](architecture.png?raw=true "Graphrec architecture")
## How to build and use
### Android application
The android application is build with [Android studio](https://developer.android.com/studio/index.html) and therefore uses the buildsystem [Gradle](https://gradle.org/). The target android SDK is at the moment version 25, but the minumum version is 15. This means, for example that the old camera API is used instead of the new one. For new devices (API level 19+) you must explicitly accept the app to use the camera [permission](https://developer.android.com/guide/topics/permissions/index.html), otherwise the app will crash.
#### Android studio
You must first import the project to android studio with the built in import function and sync Gradle if needed. Now you have two choices, either you use a virtual android device or you can use a real phone connected to your development computer. Note that if you are using a physical mobile device, then you must enable USB debugging and accept the computer security fingerprint.
#### Other systems
If you want to use another IDE or text editor with gradle supprt then you should be able to manually build the project with the supplied gradle scripts. To run the project on a virtual device you must use the [android emulator tool](https://developer.android.com/studio/run/emulator-commandline.html). If you want to use a physical mobile device instead, then you must install the app with the [apk tool](https://developer.android.com/studio/command-line/adb.html#move). You must also also accept USB debugging and accept the computer security fingerprint.

### Server
We are using python 3 and the Flask web framework.

Install Flask
```bash
pip install Flask
```

And then run the server as usual
```bash
python3 server.py
```
## Testing
### Overall
Our overall testing strategy is to quickly deploy a minimum viable product, MVP, where we have integration of at least the server component and the mobile app component from the start. We want to integrate the whole system early to make sure all parts actually functions together, thus making the detection of system wide bugs, caused by changes to a single component, easy.
### Components
Testing of the individual components are tailored to suit those specific elements and their functionality.
#### Android application
To be decided.
#### Server
We are going to use [Flask's testing convention](http://flask.pocoo.org/docs/0.12/testing/) and python unit tests.
#### Neural network
To be decided.

## Git guidelines
In this project we have a protected master which means that commits can only be merged with pull requests. Generally we want to keep the master stable and develop on the frontend-dev and backend-dev branches instead. The point of this is to prevent either the backend or the frontend to break at any time while developing.

### Your first commit
Let us say that you have found a bug and want to fix it. Then you should first clone the project into your own development machine. Make sure that you fetch the master branch aswell as the development branches (frontend-dev, backend-dev). If you want to work on the backend then you must checkout to the backend-dev branch (or the analogue for frontend):

```bash
git checkout backend-dev
```
Then you can fix your bug either on this branch or a new branch:

```bash
git checkout -b newbranch
```
When you are done then you should check if either of the development branch is updated or the master branch. If any of the two branches are updated, then you must update them locally as well before pushing.

```bash
git fetch origin master
git rebase origin/master master
```
Or the equivalent for any development branches you are working on. Now you should be able to push the development branch and make a pull request!

```bash
git push origin backend-dev
```

### Useful git commands

 Getting an overview of the branches:

 ```bash
git log --graph --decorate --oneline --all
 ```
 Fixing X number of commits from HEAD (The current commit usually). This includes removing commits and squashing them:

 ```bash
 git rebase -i HEAD~X
 ```

Staging partial file changes:

```bash
git add -p .
```

If you are interested in learning more of git then check out: https://www.atlassian.com/git/tutorials
