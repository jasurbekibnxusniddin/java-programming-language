# Consul → user-service → order-service → api-gateway

## Quick notes / prerequisites

* Use JDK 24 (you said Java 24). Spring Boot 3.4/3.5 line supports modern JDKs (Spring Boot requires ≥17 and is tested up to recent JDKs). Also use Gradle 8.4+ (Gradle 7.x or 8.x are supported; for JDK24 prefer Gradle 8.4+). 
Home

* You’ll need Docker to run Consul (or Kubernetes/Helm if you prefer). I’ll show a quick Docker option. 
HashiCorp Developer

* We’ll use Spring Cloud Consul for discovery/config integration (official quickstart & docs). 
Home

* For Spring Cloud BOM, we’ll use the 2024.x train (works with Spring Boot 3.4.x). 
Home

## Project order (what to create & run first)

1. Consul (server) — start first so services can register. (Run locally with Docker for dev.) 
HashiCorp Developer
2. user-service — a small service that exposes /users. Register with Consul.
3. order-service — calls user-service by logical name through discovery.
4. api-gateway (Spring Cloud Gateway) — optional but useful; discovers services via Consul and exposes unified paths.

Create projects in that order (you can create code for all three first), but start Consul before starting services so registration works.

### 1) Start Consul (dev)
Run a Consul server container (official example):
```bash
docker run --name=consul-server -d \
  -p 8500:8500 -p 8600:8600/udp \
  hashicorp/consul \
  consul agent -server -ui -node=server-1 -bootstrap-expect=1 -client=0.0.0.0 -data-dir=/consul/data
```
UI: http://localhost:8500

This command is from HashiCorp’s docs (official).