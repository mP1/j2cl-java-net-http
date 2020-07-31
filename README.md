[![Build Status](https://travis-ci.com/mP1/j2cl-java-net-http.svg?branch=master)](https://travis-ci.com/mP1/j2cl-java-net-http.svg?branch=master)
[![Coverage Status](https://coveralls.io/repos/github/mP1/j2cl-java-net-http/badge.svg?branch=master)](https://coveralls.io/github/mP1/j2cl-java-net-http?branch=master)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/mP1/j2cl-java-net-http.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/mP1/j2cl-java-net-http/context:java)
[![Total alerts](https://img.shields.io/lgtm/alerts/g/mP1/j2cl-java-net-http.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/mP1/j2cl-java-net-http/alerts/)
[![J2CL compatible](https://img.shields.io/badge/J2CL-compatible-brightgreen.svg)](https://github.com/mP1/j2cl-central)



# java.net.http for j2cl

An emulation of some of the classes in the java.net.http package, replacing the underlying sockets implementation with AJAX/XHR.



## walkingkooka.j2cl.java.net.http

- The package `walkingkooka.j2cl.java.net.http` is shaded to `java.net.http`.



### HttpClient

All other methods implemented, including `HttpClient.newBuilder`

- `send` [Ticket](https://github.com/mP1/j2cl-java-net-http/issues/13)
- sendAsync(HttpRequest, HttpResponse.BodyHandler<T>); *absent*
- sendAsync(HttpRequest, HttpResponse.BodyHandler<T>, HttpResponse.PushPromiseHandler<T>); *absent*



### HttpHeaders

All methods emulated



### HttpRequest.BodyPublishers

- fromPublisher(Flow.Publisher<? extends ByteBuffer>); *absent*
- fromPublisher(Flow.Publisher<? extends ByteBuffer>, long); *absent*
- ofByteArray(byte[]); *absent*
- ofByteArray(byte[], int, int); *absent*
- ofByteArrays(Iterable<byte[]>); *absent*
- ofString(String) *IMPLEMENTED*
- ofString(String, Charset) *IMPLEMENTED*



### HttpResponse.BodyHandlers

- [TODO](https://github.com/mP1/j2cl-java-net-http/issues/14)


### HttpResponse.BodySubscribers

- [TODO](https://github.com/mP1/j2cl-java-net-http/issues/15)


## Getting the source

You can either download the source using the "ZIP" button at the top
of the github page, or you can make a clone using git:

```
git clone git://github.com/mP1/j2cl-java-net-http.git
```
