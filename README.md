# Big Time

An example .cljs application built with boot.

http://big-time.divshot.io/

## The stack

This boot environment gives us:

 - ClojureScript compilation
 - Hot loading
 - Repl
 - Sass compilation
 - Deployment to divshot

## Setting up

You will need a JDK installed. Download one here:

http://www.oracle.com/technetwork/java/javase/downloads/index.html

You will also need `lein`, `boot` and `sassc` to develop on this project.
On Mac you can get all these with brew:

```
brew install leiningen boot-clj sassc
```

## Development

To start developing simply run:

```
boot dev
```

Then open target/index.html in a browser (no web server yet).

## Deployment

Deploying to divshot is as simple as:

```
boot deploy
```

You will need to be authorized with divshot.
