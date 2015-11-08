# Big Time

An example .cljs application built with boot.

https://big-time.firebaseapp.com/

## The stack

This boot environment gives us:

 - ClojureScript compilation
 - Hot loading
 - Repl
 - Sass compilation
 - Deployment to firebase

## Setting up

You will need a JDK installed. Download one here:

http://www.oracle.com/technetwork/java/javase/downloads/index.html

You will also need `lein`, `boot` and `sassc` to develop on this project.
On Mac you can get all these with brew:

```
brew install leiningen boot-clj sassc
```

That's all you need to do. Now follow the instructions below to start
developing :)

## Development

To start developing simply run:

```
boot dev
```

Then open target/index.html in a browser (no web server yet).

## Deployment

This app is setup to deploy via firebase. You will need the node js
command line client for firebase for this to work. Install with:

```
npm install -g firebase-tools
```

Also ensure `firebase.json` points to your firebase account.

Deploying to firebase is as simple as:

```
boot deploy
```

You will need to be authorized with firebase.
