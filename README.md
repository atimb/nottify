nottify
=======

Delivers websocket notifications triggered by Google Drive activity

_Created for [this CloudSpoke](http://www.cloudspokes.com/challenges/1501) challenge_

Live presentation: http://nottify.herokuapp.com 

Setup instructions
-------

* Open `config.properties` and fill up all necessary keys and secrets, polling interval
* Deploy to heroku: `git push`
* You are done Sir

Features
-------

* Delivering real time notifications of added/deleted documents/folders inside Google Drive
* OAuth2 authorization with drive/docs feed scope
* Multi-user supported, any user can only see his own changes
* Configurable polling interval for Google Drive API
* Handling error cases, token refreshing

Code
-------

A maven managed Java codebase is responsible for GDrive polling, feed parsing, and notification
delivering.
HTTP Notifications go to RequestB.in, and through Pusher.com to the end users.
Client side: javascript/html5/css3/less is rendering the changes.
