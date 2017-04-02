# strava-activity-graphs

Generate statistical charts for [Strava activities](https://www.strava.com/dashboard?feed_type=my_activity).


## Prerequisites

- [Install leiningen](http://leiningen.org/#install).
- Strava access token is required. You can get it [creating a Strava application](http://labs.strava.com/developers).


## Usage

In order to display charts in separate windows (one window per chart), run:

```shell
# Replace $access_token with your Strava access token)
lein run $access_token
```
and charts like these will be displayed:
![screen shot](screenshot.png)


## License

Copyright © 2017 Nicolas Kosinski

Distributed under the [Creative Commons Attribution 4.0 license](https://creativecommons.org/licenses/by/4.0/).