angular-dropwizard
==================

AngularJS based front end with Drop Wizard backend.  JSession based auth on REST APIs.  Good starting point if you are trying to move on from here.  PLEASE, if you develop additional features on this stuff which is general purpose please send a pull request!

History
-------
I've been doing spring based dev for a long time. When I ran across DropWizard I really like the level of functionality provided.  I've been playing with AngularJS for a few months, and really like it as well.  So this is my attempt to meld the two.

Get Started
-----------

1. git clone https://github.com/windbender/angular-dropwizard.git
2. mvn package
3. java -jar target/angular-dropwizard.jar server ./serverconfig.yml
4. visit  http://localhost:8080/  with a browser.
5. login using ANY username and the password "secret"  ( see DummyPersonDAO.java )


Notes
-------
* Use of ConfiguredAssetsBundle so that the javascript,css, and html can be changed and reloaded without rebuilding the java server.
* If you use eclipse of some other system which can replace running classes, then you can often change java code without a restart.
* dropwizard-auth is used to do auth, but with some mods.

TODO
----
1. add some healthchecks?
2. update to bootstrap 3
3. if needed update to latest angular and angular sub projects

