Social Counts
=============

A simple JAVA library to fetch various social like/share/comment counts for a given URL. This
helps in analyzing the current outreach of a given URL.

The following social media providers are currently integrated:

* Facebook like/share/comments
* Twitter tweets
* Google Plus ones
* Pinterest pins
* Linkedin shares

Usage
-----

Using the library is pretty easy, as under,

```java
String url = "http://facebook.com";
SocialCounts counts = SocialCountClient.getSocialCounts(url);

System.out.println("Facebook shares: " + counts.facebookShares);
```

The above example runs through each provider one by one, and thus may be slow in retrieving the 
sought information. The following example also shows the usage of a **parallelized** fetch from
all these providers.

```java
String url = "http://facebook.com";
SocialCounts counts = SocialCountClient.getSocialCountsParallel(url);

System.out.println("Facebook shares: " + counts.facebookShares);
```

This would be much faster than the previous, but will make use of parallel threads and may increase
the load on the system a bit.

If you just want to fetch some specific counts, it is possible as,

```java
String url = "http://facebook.com";
SocialCounts counts = new SocialCounts(url);

// fetch only twitter and facebook metrics
SocialCountClient.getTwitterCount(counts);
SocialCountClient.getFacebookCount(counts);

// only done to measure timing - no other significance
counts.markComplete();
```

Release Logs
------------

**Development Version**

* Integration with Facebook, Twitter, Google Plus, Linkedin and Pinterest
* Basic API structure

RoadMap
-------

* Add more providers - HackerNews, Reditt, StumbleUpon etc

Dependencies
------------
`socialcounts` depends on the following open-source frameworks

* `jerry` - a common functionality library
* Apache HTTP Client library
* Google GSON library for JSON deserialization

Versioning
----------

For transparency and insight into our release cycle, and for striving to maintain backward compatibility, 
`socialcounts` will be maintained under the Semantic Versioning guidelines as much as possible.

Releases will be numbered with the follow format:

`<major>.<minor>.<patch>`

And constructed with the following guidelines:

* Breaking backward compatibility bumps the major
* New additions without breaking backward compatibility bumps the minor
* Bug fixes and misc changes bump the patch

For more information on SemVer, please visit http://semver.org/.

License
-------

Copyright (c) 2014, Sandeep Gupta

The project uses various other libraries that are subject to their
own license terms. See the distribution libraries or the project
documentation for more details.

The entire source is licensed under the Apache License, Version 2.0 
(the "License"); you may not use this work except in compliance with
the LICENSE. You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
