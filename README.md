# Graphrec

## Description
Mobile app that recognizes functions from looking at photos of their graphs, by using machine learning technology.

### Components
Graphrec is composed of three main components: the mobile application, the server and the neural network.
#### Mobile application 
The idea is to have an android application that can take photos and crop them to a fixed size. It should then be able to send these photos to a remote server. 
#### The server
The server should receive these cropped images, let a trained neural network classify them with some probability and then send back this result of the evaluation to the mobile application. 
#### The neural network
The last component would be a method on the server that trains the neural network using supervised learning. It would automatically generate pictures of graphs to be added to the training data set. All training could be done prior to getting the server up and running or procedurally as the server runs. The most basic function classes for the neural network to differentiate between could be ascending versus descending linear functions of the form `ax+b` where `a` is greater than respectively lesser than `0`.

## Architecture

## How to build and use

## Testing
