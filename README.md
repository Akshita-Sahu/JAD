# JAD - Java Diagnostic Tool

**JAD — Developed and maintained by Akshita Sahu**

![Features](https://img.shields.io/badge/Features-Advanced%20AI%20Diagnostics-blue)
![REST API](https://img.shields.io/badge/REST%20API-Supported-green)
![Build](https://img.shields.io/badge/Build-Passing-brightgreen)

## What's New in JAD
- **AI-Powered Diagnostics:** Auto-detect memory leaks, thread deadlocks, CPU spikes, and GC pressure.
- **Smart Suggestions:** Get next-step recommendations after every command.
- **Enhanced Web Console:** Modern HTML5 Canvas/Chart.js rendering of JVM telemetry.
- **New Commands:** `jad report`, `jad health`, `jad compare`, `jad alert`, `jad export`, `jad history`.
- **Integrations:** REST API endpoints, Slack/webhook push notifications, Prometheus support.
- **Performance:** Sub 1% overhead with concurrent tracing and execution.

## Getting Started in 60 seconds
Download and attach to your JVM:
```bash
curl -O https://github.com/Akshita-Sahu/JAD/releases/download/latest/jad-boot.jar
java -jar jad-boot.jar
```

## Architecture Diagram
```
[User Interface (Web/CLI)] <--> [JAD Server] <--> [JAD Core (AI Diagnostics)] <--> [JVM (Target)]
```

## REST API Reference
You can invoke generic commands seamlessly through standard HTTP endpoints.
`GET /api/exec?command=health`

 Copyright 2024 Akshita Sahu. All Rights Reserved.
