# Start as a Java Agent

Usually JAD dynamic attach the applications on the fly, but from version `3.2.0` onwards, JAD supports starting directly as a java agent.

For example, download the full jad zip package, decompress it and start it by specifying `jad-agent.jar` with the parameter `-javaagent`.

```
java -javaagent:/tmp/test/jad-agent.jar -jar math-game.jar
```

The default configuration is in the `jad.properties` file in the decompression directory. Reference: [JAD Properties](jad-properties.md)

Reference: https://docs.oracle.com/javase/8/docs/api/java/lang/instrument/package-summary.html
