strava-activity-graphs [![Build Status](https://travis-ci.org/nicokosi/strava-activity-graphs.svg?branch=master)](https://travis-ci.org/nicokosi/strava-activity-graphs)
============================

Generate statistical charts for [Strava activities](https://www.strava.com/dashboard?feed_type=my_activity).


## Prerequisites

- [Install leiningen](http://leiningen.org/#install).
- Strava access token is required. You can get it [creating a Strava application](http://labs.strava.com/developers).

## Command line installation

Run `./generate-executable.sh`.

## Usage

In order to display charts in separate windows (one window per chart), run:

```shell
# Replace $access_token with your Strava access token)
lein run $access_token
```
and charts like these will be displayed:
![screen shot](screenshot.png)