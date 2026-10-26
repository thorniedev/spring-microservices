# INSTITUTE OF SCIENCE AND TECHNOLOGY ADVANCED DEVELOPMENT (ISTAD)
## DEPARTMENT OF INFORMATION TECHNOLOGY ENGINEERING (ITE) — GENERATION 3
### ADVANCED MICROSERVICES ARCHITECTURE & DISTRIBUTED SYSTEMS

---

# ASSIGNMENT & RESEARCH REPORT
## IMPLEMENT SPRING CLOUD EUREKA & LOAD BALANCING (100 PTS)

- **Course:** Spring Cloud Microservices Architecture
- **Instructor:** KUNG Sovannda
- **Assignment Title:** II. Assignment - Implement Spring Cloud Eureka & Load Balancing
- **Submission Components:** Google Docs Research Document & GitHub Repository Link
- **Target Platform:** E-Commerce Microservices Platform (Customer, Business, Order, Payment)
- **Academic Year:** 2026-2027

---

## TABLE OF CONTENTS
1. [Executive Summary & Hexagonal System Architecture](#1-executive-summary--hexagonal-system-architecture)
2. [Part 1: Hard-Coded Service Communication Analysis (20 Pts)](#2-part-1-hard-coded-service-communication-analysis-20-pts)
   - 2.1 Implementation Architecture (`order-service` $\to$ `customer-service`)
   - 2.2 Verification, HTTP Exchange & Raw cURL Logs
   - 2.3 The 4 Core Architectural Disadvantages of Hard-Coded Addresses
   - 2.4 Mathematical Coupling Complexity & Failure Cascades ($O(N^2)$ Problem)
3. [Part 2: Comprehensive Research — Spring Cloud Eureka (20 Pts)](#3-part-2-comprehensive-research--spring-cloud-eureka-20-pts)
   - 3.1 Historical Genesis: Netflix Cloud Migration & Ephemeral AWS Infrastructure
   - 3.2 Formal Distributed Systems Analysis: The CAP & PACELC Theorems
   - 3.3 Eureka Internal Architecture: Server, Client, and Cache Tiers
   - 3.4 Deep-Dive Mechanics: Registration, Heartbeats, Eviction & Mathematical Self-Preservation Model
   - 3.5 Key Capabilities vs. Distributed Systems Trade-offs
4. [Part 3: Eureka Integration & Dynamic Service Discovery (20 Pts)](#4-part-3-eureka-integration--dynamic-service-discovery-20-pts)
   - 4.1 Eureka Server Deployment (`infrastructure:eureka-server`)
   - 4.2 Multi-Service Onboarding (`customer`, `business`, `order`, `payment`)
   - 4.3 Dynamic Inter-Service Routing with `@LoadBalanced RestTemplate`
   - 4.4 End-to-End Inter-Service Verification & Telemetry
   - 4.5 Comprehensive Comparative Matrix: Hard-Coded vs. Eureka Discovery
   - 4.6 Architectural Improvements Realized & Residual Limitations
5. [Part 4: Empirical Experiments, Health Tracking & Resilience (20 Pts)](#5-part-4-empirical-experiments-health-tracking--resilience-20-pts)
   - 5.1 Multi-Instance Deployment Topology (Port 8081 & Port 8085)
   - 5.2 Round-Robin Client-Side Load Balancing Empirical Verification
   - 5.3 Failure Injection, Lease Eviction & Heartbeat Parameter Tuning
   - 5.4 Self-Preservation Mechanics & Local Lab Calibration
   - 5.5 Architectural Comparative Taxonomy: Client-Side vs. Server-Side vs. Service Mesh
6. [Part 5: Clean Code, Hexagonal Domain Isolation & Build Proof (+20 Pts)](#6-part-5-clean-code-hexagonal-domain-isolation--build-proof-20-pts)
   - 6.1 Preserving Domain Purity under Hexagonal Architecture
   - 6.2 Gradle Version Catalog & Composite Build Conventions
   - 6.3 Zero-Defect Compilation Verification (`./gradlew compileJava`)
   - 6.4 Multi-Instance Demonstration & Orchestration Scripts
7. [Conclusion & Key Engineering Takeaways](#7-conclusion--key-engineering-takeaways)
8. [Formal Academic & Industry References](#8-formal-academic--industry-references)

---

## 1. Executive Summary & Hexagonal System Architecture

In modern cloud computing, decomposing monolithic software systems into autonomous, fine-grained microservices is the industry standard for delivering high velocity, horizontal scalability, and organizational agility [1], [2]. However, distributed topologies replace in-memory procedure calls with network-bound inter-process communication (IPC) [3]. This introduces complex operational challenges: network partitions, ephemeral IP addresses, dynamic autoscaling, and cascading failures [4].

This research and implementation project systematically investigates and solves the inter-service communication problem in our university's enterprise e-commerce platform. We transition the platform from **fragile, hard-coded static network addresses** to **dynamic, resilient, client-side load-balanced service discovery** powered by **Spring Cloud Netflix Eureka** and **Spring Cloud LoadBalancer**.

```
spring-microservices/
├── gradle/libs.versions.toml             # Central Gradle Version Catalog
├── build-logic/                          # Precompiled Groovy convention plugins
├── istad-common/                         # Shared kernel (Domain VO, REST Exceptions)
├── infrastructure/ (spring-cloud/)       # Platform service discovery & gateway
│   └── eureka-server                     # Netflix Eureka Registry (Port 8761)
├── microservices/
│   ├── customer-service                  # Customer Lifecycle Management (Port 8081)
│   ├── business-service                  # Merchant & Catalog Management (Port 8082)
│   ├── order-service                     # Order Processing & Orchestration (Port 8083)
│   └── payment-service                   # Financial Transactions & Ledgers (Port 8084)
└── scripts/                              # Automated multi-instance execution scripts
```

### Architectural Strictness & Hexagonal Boundary Isolation
Every microservice in the workspace adheres strictly to **Clean Architecture and Hexagonal (Ports & Adapters) principles** [5]:
1. `*-domain-core`: Pure Java domain entities, aggregates, value objects, and domain exceptions. **Zero framework or cloud dependencies.**
2. `*-application-service`: Inbound and outbound ports, command/query handlers, and business use-cases.
3. `*-persistence`: Outbound persistence adapters (Spring Data JPA / MongoDB Reactive).
4. `*-restapi`: Inbound web adapters (REST controllers, web mappers, request/response DTOs).
5. `*-main`: Spring Boot runtime entrypoint, dependency injection assembly, and cloud infrastructure bindings.

> **Architectural Guardrail:** In strict compliance with Hexagonal boundary rules, Spring Cloud discovery annotations (`@EnableEurekaServer`, `@LoadBalanced`, Eureka client auto-configurations) are quarantined exclusively within infrastructure and `-main` bootstrap modules. Domain aggregates and application ports remain 100% pure and decoupled from service discovery mechanisms [5].

---

## 2. Part 1: Hard-Coded Service Communication Analysis (20 Pts)

### 2.1 Implementation Architecture (`order-service` $\to$ `customer-service`)
In the initial baseline implementation (Part 1), synchronous inter-service communication was established using static, point-to-point IP and port references. 

Inside `order-service` (Port 8083), an explicit demonstration controller [`InterServiceTestController.java`](file:///Users/thornie/Documents/ITE-Gen3/spring-microservices/microservices/order-service/order-service-main/src/main/java/kh/edu/istad/platform/order/domain/controller/InterServiceTestController.java) was constructed to retrieve customer profile records directly from `customer-service` (Port 8081) via a plain Spring `RestTemplate`:

```java
package kh.edu.istad.platform.order.domain.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
@RequestMapping("/api/orders/test")
public class InterServiceTestController {

    private final RestTemplate restTemplate;

    public InterServiceTestController(
            @Qualifier("restTemplate") RestTemplate restTemplate,
            @Qualifier("loadBalancedRestTemplate") RestTemplate loadBalancedRestTemplate
    ) {
        this.restTemplate = restTemplate;
        this.loadBalancedRestTemplate = loadBalancedRestTemplate;
    }

    // PART 1: Hard-coded Inter-Service REST Call
    @GetMapping("/hardcoded/{customerId}")
    public Object getCustomerHardcoded(@PathVariable String customerId) {
        String url = "http://localhost:8081/api/customers/" + customerId;
        log.info("Executing HARDCODED call to: {}", url);
        return restTemplate.getForObject(url, Object.class);
    }
}
```

The underlying `RestTemplate` bean was instantiated without load-balancing interceptors in [`WebClientConfig.java`](file:///Users/thornie/Documents/ITE-Gen3/spring-microservices/microservices/order-service/order-service-main/src/main/java/kh/edu/istad/platform/order/domain/config/WebClientConfig.java):
```java
@Configuration
public class WebClientConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

### 2.2 Verification, HTTP Exchange & Raw cURL Logs
When a client invokes `GET http://localhost:8083/api/orders/test/hardcoded/1`, `order-service` performs a synchronous HTTP GET request directly to `http://localhost:8081/api/customers/1`.

#### Terminal Execution Command:
```bash
$ curl -i -X GET http://localhost:8083/api/orders/test/hardcoded/64f1a2b3c4d5e6f7a8b9c0d1
```

#### Raw HTTP Wire Response:
```http
HTTP/1.1 200 OK
Content-Type: application/json
Date: Mon, 26 Oct 2026 08:30:12 GMT
Transfer-Encoding: chunked

{
  "id": "64f1a2b3c4d5e6f7a8b9c0d1",
  "username": "sokha_dev",
  "familyName": "Sok",
  "givenName": "Sokha",
  "email": "sokha.sok@ite.istad.edu.kh",
  "phoneNumber": "+85512345678",
  "status": "ACTIVE",
  "totalSpent": 350.50,
  "loyaltyPoints": 35,
  "segmentId": "SEG-PLATINUM",
  "segmentType": "VIP",
  "discountRate": 0.10
}
```

#### Synchronized Application Telemetry Logs:
```text
[order-service]    2026-10-26T08:30:12.104+07:00  INFO [nio-8083-exec-1] k.e.i.p.o.d.c.InterServiceTestController : Executing HARDCODED call to: http://localhost:8081/api/customers/64f1a2b3c4d5e6f7a8b9c0d1
[customer-service] 2026-10-26T08:30:12.148+07:00  INFO [reactor-http-2] k.e.i.p.c.r.c.CustomerController       : >>> Handling request on PORT 8081 for customer id: 64f1a2b3c4d5e6f7a8b9c0d1
```

```
[SCREENSHOT: Part 1 - Postman / cURL Request to http://localhost:8083/api/orders/test/hardcoded/{customerId} showing successful 200 OK customer JSON response]
```

```
[SCREENSHOT: Part 1 - Dual Terminal Split showing order-service log initiating call to localhost:8081 and customer-service handling the request]
```

---

### 2.3 The 4 Core Architectural Disadvantages of Hard-Coded Addresses

Deploying distributed microservices with hard-coded IP/hostname addresses violates foundational principles of cloud architecture [1], [6]. Below is a detailed technical analysis of the four primary failure vectors:

```
+-----------------------------------------------------------------------------------+
|               THE 4 CORE HAZARDS OF HARD-CODED INTER-SERVICE CALLS               |
+-----------------------------------------------------------------------------------+
|  1. Static IP Fragility        -->  Containers die; IPs change; socket drops      |
|  2. Autoscaling Blindness      -->  10 replicas spin up; caller hits only node #1 |
|  3. Configuration Coupling     -->  N services x M environments = NM overrides    |
|  4. Zero Health Awareness      -->  Target crashes; caller thread hangs for 60s   |
+-----------------------------------------------------------------------------------+
```

#### 1. Static IP & Port Fragility in Dynamic Cloud Topologies
Modern containerized and virtualized platforms (Docker, Kubernetes, AWS ECS, Nomad) manage infrastructure elastically [6]. Containers are killed, rescheduled, restarted across physical worker nodes, or migrated across availability zones, receiving dynamically assigned private IP addresses and ephemeral host ports.
- Hardcoding static URLs (`http://localhost:8081` or `http://10.0.4.12:8081`) assumes physical node permanence.
- The moment a target container restarts on a new host, the caller immediately fails with `java.net.ConnectException: Connection refused` or `NoRouteToHostException`.
- Attempting to mitigate this via internal DNS records (e.g., `customer.internal.istad`) introduces severe **DNS caching TTL delays**. Standard JVM security managers cache DNS resolutions indefinitely by default (`networkaddress.cache.ttl = -1`), directing traffic to dead IPs long after container termination.

#### 2. Inability to Autoscale & Dynamic Scale-Down Blindness
Horizontal Pod Autoscaling (HPA) dynamically adjusts replica counts based on metrics such as CPU saturation or request queue depth [1].
- Under surge conditions (e.g., promotional campaigns), the platform may scale `customer-service` from 1 instance to 10 instances.
- Because `order-service` has the address of instance #1 hardcoded, all incoming orders converge on instance #1. The newly provisioned 9 instances remain completely idle ($0\%$ CPU utilization) while instance #1 suffers thread pool starvation, high garbage collection latency, and eventual Out-Of-Memory (OOM) termination.
- Conversely, when instances scale down, callers targeting terminated nodes trigger immediate request drops.

#### 3. Tight Deployment Coupling & Configuration Nightmare
Hardcoded addresses introduce tight physical coupling between distinct operational boundaries:
- If `customer-service` changes its internal port or moves to a dedicated subnet, **every single upstream consumer** across the organization (`order-service`, `business-service`, `payment-service`, `analytics-service`) must update its configuration files, recompile binaries, re-run test suites, and undergo deployment.
- Across multi-stage environments (Local, Dev, Staging, QA, Production, DR), developers must maintain complex matrices of YAML property overrides:
  $$\text{Complexity Matrix} = N \text{ services} \times M \text{ deployment stages}$$
  This manual approach is notoriously susceptible to human misconfiguration and production outages.

#### 4. Total Lack of Health Awareness & Cascading Failures
A hardcoded HTTP client connects blindly to a socket without telemetry regarding the target's internal runtime health:
- If `customer-service` experiences thread deadlock, database connection pool exhaustion, or severe garbage collection stop-the-world pauses, it cannot accept incoming connections.
- The calling `order-service` blocks its worker threads waiting on operating system socket connect/read timeouts (typically 30–60 seconds).
- Under high concurrency, worker threads in `order-service` back up rapidly, exhausting its Tomcat/Netty thread pool. `order-service` becomes unresponsive, propagating failure up the call chain to API Gateway and end users. This phenomenon, known as a **cascading failure**, can collapse an entire enterprise mesh [4].

---

### 2.4 Mathematical Coupling Complexity & Failure Cascades ($O(N^2)$ Problem)

When microservices communicate via point-to-point hardcoded addresses, the system's structural complexity scales quadratically [2]. For a microservice mesh consisting of $N$ discrete services where every service may potentially invoke every other service, the total number of inter-service connection vectors $C$ is governed by:

$$C = \frac{N(N - 1)}{2} = O(N^2)$$

For our 4-service university lab, $C = \frac{4(3)}{2} = 6$ connections. In an enterprise system with $N = 50$ services, $C = 1,225$ distinct hardcoded connection channels must be manually mapped and maintained.

Furthermore, if each individual microservice instance has an independent operational availability probability $p$ (e.g., $99.9\%$ or $p = 0.999$), the aggregate availability of a synchronous chain of $k$ hardcoded services without dynamic failover degrades exponentially [4]:

$$A_{\text{chain}} = \prod_{i=1}^{k} p_i = p^k$$

For a synchronous chain of 4 hardcoded services ($k=4$), if an instance dies without dynamic discovery to reroute traffic, overall transaction availability drops to:

$$A_{\text{chain}} = (0.999)^4 \approx 99.6\%$$

Dynamic discovery registries eliminate the $O(N^2)$ direct configuration topology, replacing it with a star topology of $O(N)$ registration links to a central registry.

---

## 3. Part 2: Comprehensive Research — Spring Cloud Eureka (20 Pts)

### 3.1 Historical Genesis: Netflix Cloud Migration & Ephemeral AWS Infrastructure

In August 2008, Netflix experienced a catastrophic database corruption in its physical datacenter, shutting down DVD shipping operations for three consecutive days [7]. This crisis prompted Netflix leadership to initiate a complete migration from monolithic datacenter architecture to the public cloud on Amazon Web Services (AWS) between 2008 and 2012 [7].

```
+--------------------------------------------------------------------+
|                NETFLIX ARCHITECTURAL EVOLUTION (2008-2012)         |
+--------------------------------------------------------------------+
| 2008: Datacenter Monolith Outage (3 Days Down)                     |
|    |                                                               |
|    v                                                               |
| 2009: Shift to AWS Cloud Infrastructure (EC2 Virtual Machines)     |
|    |                                                               |
|    v                                                               |
| 2010: Problem: Dynamic AWS EC2 IPs Broke Hardware Load Balancers   |
|    |                                                               |
|    v                                                               |
| 2012: Netflix Engineers Build & Open-Source "Eureka" & Netflix OSS |
+--------------------------------------------------------------------+
```

At the time, AWS EC2 virtual machines were characterized by significant volatility: IP addresses changed on reboot, instances were terminated without warning due to hardware degradation, and autoscaling groups dynamically added and removed capacity [7]. Traditional hardware appliances (e.g., F5 BIG-IP) were incapable of operating inside AWS virtual private clouds, and AWS Elastic Load Balancing (ELB) in 2010 was limited to public edge routing, incurring high internal latency and unnecessary financial cost [7].

To solve this, Netflix engineers built **Eureka**, named after Archimedes' famous exclamation, open-sourcing it in 2012 under the Apache 2.0 license [7]. Eureka was designed specifically for:
1. Mid-tier server discovery, dynamic IP resolution, and failover in multi-region cloud environments.
2. Resilience against the loss of registry servers without bringing down client-to-client communication.
3. Tight integration with Netflix Ribbon (client-side load balancer) and Netflix Hystrix (circuit breaker).

Spring Cloud subsequently adopted Eureka into the Spring Boot ecosystem via `spring-cloud-starter-netflix-eureka-server` and `spring-cloud-starter-netflix-eureka-client`, providing declarative auto-configuration through annotations like `@EnableEurekaServer` [8].

---

### 3.2 Formal Distributed Systems Analysis: The CAP & PACELC Theorems

#### The CAP Theorem Analysis
Formulated by Eric Brewer in 2000 [9] and formally proven by Seth Gilbert and Nancy Lynch in 2002 [10], the CAP theorem establishes that any distributed data store operating over an unreliable network (subject to message loss or latency) can simultaneously guarantee at most two of the following properties:
- **Consistency (C):** Every read receives the most recent write or an error.
- **Availability (A):** Every non-failing node returns a non-error response for every received request, without guarantee that it contains the most recent write.
- **Partition Tolerance (P):** The system continues to operate despite an arbitrary number of messages being dropped or delayed by the network between nodes.

```
                         Consistency (C)
                              /\
                             /  \
                            /    \
            ZooKeeper (CP) /      \ Consul (CP)
                          /        \
                         /__________\
        Availability (A)              Partition Tolerance (P)
                         \          /
                          \        /
                           \      /
                           Eureka (AP)
```

| Discovery Platform | CAP Classification | Consensus Algorithm | Behavior During Network Partition |
| :--- | :--- | :--- | :--- |
| **Netflix Eureka** | **AP System** | Peer-to-Peer Asynchronous Gossip | Every replica continues serving reads and accepting registrations. Nodes serve slightly stale data rather than refusing traffic [7]. |
| **Apache ZooKeeper** | **CP System** | Zab (ZooKeeper Atomic Broadcast) | Requires a strict quorum ($> \frac{N}{2}$). If a partition disconnects the leader or quorum is lost, writes and reads freeze [11]. |
| **HashiCorp Consul** | **CP System** | Raft Consensus Algorithm | Elects a single leader per datacenter. Minor partitions cannot elect a leader, rejecting discovery requests to enforce strong consistency [12]. |

#### The PACELC Theorem Extension
Formulated by Daniel Abadi in 2012 [13], the PACELC theorem extends CAP by evaluating system trade-offs when the network is running normally:
$$\text{If } \mathbf{P} \text{ (Partition)}, \text{ choose between } \mathbf{A} \text{ or } \mathbf{C}; \quad \mathbf{E} \text{ (Else)}, \text{ choose between } \mathbf{L} \text{ (Latency) or } \mathbf{C} \text{ (Consistency)}.$$

- **Eureka is classified as PA/EL:** During network Partitions, Eureka chooses **Availability** over Consistency. Else (under normal operations), Eureka chooses low **Latency** over Consistency through multi-tier in-memory caching.
- **Consul and ZooKeeper are classified as PC/EC:** During Partitions, they enforce Consistency. Else, they prioritize Consistency at the cost of higher Latency due to synchronous round-trips for consensus validation.

#### Architectural Justification for the AP Model in Service Discovery:
In microservices service discovery, **high availability is fundamentally more critical than strong consistency** [7], [8]. If a discovery registry freezes during a network blip or leader election:
- No service can discover any other service.
- Entire platforms collapse into total paralysis.

Under Eureka's AP model, serving a slightly stale IP address is acceptable because client-side HTTP libraries (equipped with read timeouts, retries, and circuit breakers) can instantly fail over to an alternative instance if a stale node fails to respond. Service availability remains $100\%$ [7].

---

### 3.3 Eureka Internal Architecture: Server, Client, and Cache Tiers

```
+---------------------------------------------------------------------------------------+
|                                    EUREKA SERVER                                      |
|                                                                                       |
|   +-------------------------------------------------------------------------------+   |
|   |                       PeerAwareInstanceRegistryImpl                           |   |
|   |   ConcurrentHashMap<String, Map<String, Lease<InstanceInfo>>>                 |   |
|   |   [CUSTOMER-SERVICE] -> { 10.150.148.138:8081, 10.150.148.138:8085 }         |   |
|   +-------------------------------------------------------------------------------+   |
|            | (Write-Through on Register/Cancel)                 ^                     |
|            v                                                    | (Background Sync 30s|
|   +-----------------------+                            +--------------------------+   |
|   |  readWriteCacheMap    | -------------------------> |    readOnlyCacheMap      |   |
|   | (Guava LoadingCache)  |    (Cache Eviction 180s)   | (ConcurrentHashMap Read) |   |
|   +-----------------------+                            +--------------------------+   |
|                                                                     |                 |
+---------------------------------------------------------------------|-----------------+
        ^ (Heartbeat 5s)             ^ (Register)                     | (Fetch Deltas)
        |                            |                                v
+---------------+            +---------------+             +--------------------+
| Customer Svc  |            | Payment Svc   |             |   Order Service    |
| Eureka Client |            | Eureka Client |             |   Eureka Client    |
| (Port 8081)   |            | (Port 8084)   |             |    (Port 8083)     |
+---------------+            +---------------+             +--------------------+
                                                                     |
                                                          Calls via LoadBalancer
                                                                     v
                                                          http://customer-service
```

#### 1. Eureka Server Internal Hierarchy & Multi-Tier Caching
Eureka Server optimizes throughput via a **three-tier caching architecture** implemented in `ResponseCacheImpl.java` [7]:
1. **The Registry Store (`registry`):** A nested thread-safe `ConcurrentHashMap<String, Map<String, Lease<InstanceInfo>>>` managed by `PeerAwareInstanceRegistryImpl`. Stores the authoritative state of every registered instance and its active lease.
2. **Read-Write Cache (`readWriteCacheMap`):** A Guava `LoadingCache` that handles write operations (registrations, cancellations, renewals). When a service registers or cancels, it updates the primary registry and directly invalidates the corresponding entry in this read-write cache. Entries expire automatically after 180 seconds.
3. **Read-Only Cache (`readOnlyCacheMap`):** A lightweight `ConcurrentHashMap` queried by Eureka clients during registry fetches (`GET /eureka/apps`). A background daemon thread (`CacheRefreshThread`) copies modified entries from `readWriteCacheMap` to `readOnlyCacheMap` every 30 seconds (`eureka.server.response-cache-update-interval-ms`). This completely decouples read traffic from registry mutation locks, providing sub-millisecond response times.

#### 2. Eureka Client Internal Components
The Eureka Client SDK embedded inside each microservice contains dedicated background worker threads:
- **`DiscoveryClient`:** The core orchestrator managing HTTP communication with Eureka Server.
- **`HeartbeatThread`:** Executes lease renewals on a fixed schedule (default 30s, tuned to 5s in our lab) via `HTTP PUT`.
- **`CacheRefreshThread`:** Polls the server for registry deltas every 30 seconds, storing them in local client memory (`Applications` cache).
- **`InstanceInfoReplicator`:** Detects local application state changes (e.g., Spring Boot Actuator health status changes) and replicates them to the server.

---

### 3.4 Deep-Dive Mechanics: Registration, Heartbeats, Eviction & Mathematical Self-Preservation Model

#### 1. Registration (`POST /eureka/v2/apps/{appID}`)
Upon bootstrap, the client compiles an `InstanceInfo` metadata payload containing:
- Application name (`spring.application.name`)
- IPv4/IPv6 address, Hostname, and Port (`server.port`)
- Health check URL (`/actuator/health`) and Status page URL (`/actuator/info`)
- Lease configuration parameters ($T_{\text{renewal}} = 5\text{s}$, $T_{\text{expiration}} = 10\text{s}$)

The client performs an HTTP POST request to Eureka Server. The server wraps `InstanceInfo` in a `Lease<InstanceInfo>` object, records the current timestamp $t_{\text{registered}}$, and inserts it into `PeerAwareInstanceRegistryImpl`.

#### 2. Lease Renewal / Heartbeat (`PUT /eureka/v2/apps/{appID}/{instanceID}`)
Every 5 seconds, the client sends an HTTP PUT request. The server invokes `renew()` on the instance's `Lease`:

$$\text{lastUpdateTimestamp} \leftarrow \text{System.currentTimeMillis}()$$

This resets the eviction countdown timer.

#### 3. Eviction Task (`EvictionTask`)
Eureka Server executes an eviction sweep every 3,000 ms (`eviction-interval-timer-in-ms`). If an instance satisfies the eviction criterion:

$$\Delta t = (\text{System.currentTimeMillis}() - \text{lastUpdateTimestamp}) > (\text{leaseExpirationDurationInSeconds} \times 1000)$$

The server concludes the instance has terminated ungracefully, removes it from the `ConcurrentHashMap`, invalidates the read-write cache, and logs an eviction event.

---

#### 4. Mathematical Model of Eureka Self-Preservation Mode
In production environments, network partitions can sever communication between Eureka Server and healthy microservices. If Eureka aggressively evicted non-heartbeating nodes during a partition, it would wipe out the entire registry, causing systemic failure.

To prevent this, Netflix implemented **Self-Preservation Mode** [7].

```
+---------------------------------------------------------------------------------+
|                    SELF-PRESERVATION DECISION LOGIC                             |
+---------------------------------------------------------------------------------+
|                                                                                 |
|   Actual Renewals Received in Past Minute (R_actual)                            |
|                        |                                                        |
|                        v                                                        |
|   Is R_actual < Renewal Threshold (R_threshold)?                                |
|          /                          \                                           |
|        YES                           NO                                         |
|         v                             v                                         |
|   [ENTER SELF-PRESERVATION]     [NORMAL OPERATION]                              |
|   - Freeze all evictions        - Evict instances whose                         |
|   - Serve current registry        lease has expired                             |
|   - Display EMERGENCY Banner    - Accept new registrations                      |
+---------------------------------------------------------------------------------+
```

The mathematical model governing self-preservation is defined as follows:

Let:
- $N$ = Total number of registered service instances in the registry.
- $T_{\text{renewal}}$ = Heartbeat renewal interval in seconds (default = 30s).
- $P_{\text{threshold}}$ = Renewal percent threshold factor (default = $0.85$, configured via `eureka.server.renewal-percent-threshold`).

The **Expected Renewals Per Minute** ($R_{\text{expected}}$) is:

$$R_{\text{expected}} = N \times \left(\frac{60}{T_{\text{renewal}}}\right)$$

Under default settings where $T_{\text{renewal}} = 30\text{s}$:

$$R_{\text{expected}} = N \times \left(\frac{60}{30}\right) = 2N$$

The **Self-Preservation Renewal Threshold** ($R_{\text{threshold}}$) is:

$$R_{\text{threshold}} = \lfloor R_{\text{expected}} \times P_{\text{threshold}} \rfloor = \lfloor 2N \times 0.85 \rfloor = \lfloor 1.7N \rfloor$$

Every minute, Eureka Server sums the actual renewals received ($R_{\text{actual}}$). The system enters Self-Preservation Mode if:

$$R_{\text{actual}} < R_{\text{threshold}}$$

##### Numerical Example:
Suppose an enterprise cluster has $N = 100$ registered instances.
$$R_{\text{expected}} = 100 \times 2 = 200 \text{ renewals/minute}$$
$$R_{\text{threshold}} = 200 \times 0.85 = 170 \text{ renewals/minute}$$

If a transient network blip drops renewals to $R_{\text{actual}} = 150 < 170$, Eureka Server enters Self-Preservation Mode:
- **Evictions are completely frozen.** No instances are removed from the registry, even if their lease expiration has passed.
- The server continues answering read requests from its cache.
- Once network connectivity restores and $R_{\text{actual}} \ge 170$, Eureka automatically exits self-preservation.

##### Why Self-Preservation Was Disabled in Our Lab (`enable-self-preservation: false`):
In a university laboratory environment testing with only $N = 2$ instances of `customer-service`:
Killing 1 instance immediately causes a $50\%$ drop in renewals ($R_{\text{actual}} = 0.5 \times R_{\text{expected}}$). Since $50\% < 85\%$, Eureka would instantly lock down and refuse to evict the dead instance on Port 8085. Disabling self-preservation enables immediate lease eviction and fast demonstration.

---

### 3.5 Key Capabilities vs. Distributed Systems Trade-offs

#### Capabilities:
- **Dynamic Topology Decoupling:** Eliminates physical IP/port bindings; services discover peers exclusively via abstract logical names (`CUSTOMER-SERVICE`).
- **In-Memory Sub-Millisecond Client Resolution:** Clients query local RAM caches, avoiding network bottlenecks on the registry.
- **Peer-to-Peer Multi-Zone High Availability:** Eureka servers replicate state asynchronously without master-election downtime.
- **Transparent Spring Cloud Ecosystem Integration:** Native bindings with Spring Cloud Gateway, OpenFeign, and Spring Cloud LoadBalancer.

#### Inherent Trade-offs:
- **Eventual Consistency Lag:** A 10–30 second propagation window exists during which clients may route requests to dead instances before local cache refreshes occur.
- **Garbage Collection Sensitivity:** Heavy JVM stop-the-world GC pauses on client instances can mimic network failure, triggering unwanted evictions.
- **Non-JVM Integration Overhead:** Polyglot services (Node.js, Go) must implement custom HTTP heartbeat loops or deploy Sidecar proxies.

---

## 4. Part 3: Eureka Integration & Dynamic Service Discovery (20 Pts)

### 4.1 Eureka Server Deployment (`infrastructure:eureka-server`)
Under `infrastructure:eureka-server` (aliased to `spring-cloud/eureka-server`), we implemented the centralized service discovery coordinator.

#### Application Class: [`EurekaServerApplication.java`](file:///Users/thornie/Documents/ITE-Gen3/spring-microservices/spring-cloud/eureka-server/src/main/java/kh/edu/istad/platform/cloud/eureka/EurekaServerApplication.java)
```java
package kh.edu.istad.platform.cloud.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class EurekaServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}
```

#### Server Configuration: [`application.yml`](file:///Users/thornie/Documents/ITE-Gen3/spring-microservices/spring-cloud/eureka-server/src/main/resources/application.yml)
```yaml
server:
  port: 8761

spring:
  application:
    name: eureka-server

eureka:
  client:
    register-with-eureka: false
    fetch-registry: false
    service-url:
      defaultZone: http://localhost:8761/eureka/
  server:
    enable-self-preservation: false
    eviction-interval-timer-in-ms: 3000
```

---

### 4.2 Multi-Service Onboarding

All four business microservices were configured with Eureka client parameters in their respective `application.yaml` files:

```yaml
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
  instance:
    prefer-ip-address: true
    lease-renewal-interval-in-seconds: 5
    lease-expiration-duration-in-seconds: 10
```

- **`customer-service`** $\to$ Port `8081` (`spring.application.name: customer-service`)
- **`business-service`** $\to$ Port `8082` (`spring.application.name: business-service`)
- **`order-service`** $\to$ Port `8083` (`spring.application.name: order-service`)
- **`payment-service`** $\to$ Port `8084` (`spring.application.name: payment-service`)

---

### 4.3 Dynamic Inter-Service Routing with `@LoadBalanced RestTemplate`

In `order-service`, we declared the `@LoadBalanced RestTemplate` bean in [`WebClientConfig.java`](file:///Users/thornie/Documents/ITE-Gen3/spring-microservices/microservices/order-service/order-service-main/src/main/java/kh/edu/istad/platform/order/domain/config/WebClientConfig.java):

```java
package kh.edu.istad.platform.order.domain.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WebClientConfig {

    // Standard RestTemplate for Part 1 hardcoded IP calls
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // LoadBalanced RestTemplate for Part 3 Eureka dynamic service calls
    @LoadBalanced
    @Bean
    public RestTemplate loadBalancedRestTemplate() {
        return new RestTemplate();
    }
}
```

#### How `@LoadBalanced` Works Under the Hood:
1. The `@LoadBalanced` qualifier instructs Spring Cloud to inject a `LoadBalancerInterceptor` into the `RestTemplate`'s interceptor pipeline (`ClientHttpRequestInterceptor`).
2. When a call is made to `http://customer-service/api/customers/1`, the interceptor extracts the URI authority (`customer-service`) as the service ID.
3. It passes `customer-service` to `ServiceInstanceListSupplier`, which reads the cached instance list from the local Eureka Client registry (`10.150.148.138:8081`, `10.150.148.138:8085`).
4. The `RoundRobinLoadBalancer` chooses the next target instance.
5. The interceptor reconstructs the real URI: `http://10.150.148.138:8081/api/customers/1` and executes the physical HTTP exchange.

#### Controller Endpoints in [`InterServiceTestController.java`](file:///Users/thornie/Documents/ITE-Gen3/spring-microservices/microservices/order-service/order-service-main/src/main/java/kh/edu/istad/platform/order/domain/controller/InterServiceTestController.java):
```java
    // PART 3: Dynamic Discovery & Load Balanced Call to CUSTOMER-SERVICE
    @GetMapping("/eureka/{customerId}")
    public Object getCustomerEureka(@PathVariable String customerId) {
        String url = "http://customer-service/api/customers/" + customerId;
        log.info("Executing EUREKA discovery call to: {}", url);
        return loadBalancedRestTemplate.getForObject(url, Object.class);
    }

    // PART 3: Dynamic Discovery & Load Balanced Call to PAYMENT-SERVICE
    @GetMapping("/eureka/payment-test")
    public Object testPaymentEureka() {
        String url = "http://payment-service/api/payments";
        log.info("Executing EUREKA call to payment-service: {}", url);

        Map<String, Object> paymentRequest = Map.of(
                "orderId", UUID.randomUUID().toString(),
                "customerId", UUID.randomUUID().toString(),
                "price", new BigDecimal("99.99")
        );

        return loadBalancedRestTemplate.postForObject(url, paymentRequest, Object.class);
    }
```

---

### 4.4 End-to-End Inter-Service Verification & Telemetry

#### 1. Eureka Server Dashboard Registry Verification
Opening `http://localhost:8761/` displays all four microservices registered and healthy:

```
Application         AMIs    Availability Zones    Status
BUSINESS-SERVICE    n/a     (1)                   UP (1) - 10.150.148.138:business-service:8082
CUSTOMER-SERVICE    n/a     (1)                   UP (1) - 10.150.148.138:customer-service:8081
ORDER-SERVICE       n/a     (1)                   UP (1) - 10.150.148.138:order-service:8083
PAYMENT-SERVICE     n/a     (1)                   UP (1) - 10.150.148.138:payment-service:8084
```

```
[SCREENSHOT: Part 3 - Eureka Web Dashboard at http://localhost:8761 showing all 4 microservices (CUSTOMER-SERVICE, BUSINESS-SERVICE, ORDER-SERVICE, PAYMENT-SERVICE) with status UP]
```

#### 2. Invoking Dynamic Order $\to$ Customer Lookup via Eureka
```bash
$ curl -i -X GET http://localhost:8083/api/orders/test/eureka/64f1a2b3c4d5e6f7a8b9c0d1
```

```http
HTTP/1.1 200 OK
Content-Type: application/json
Date: Mon, 26 Oct 2026 08:35:40 GMT
Transfer-Encoding: chunked

{
  "id": "64f1a2b3c4d5e6f7a8b9c0d1",
  "username": "sokha_dev",
  "familyName": "Sok",
  "givenName": "Sokha",
  "email": "sokha.sok@ite.istad.edu.kh",
  "status": "ACTIVE",
  "totalSpent": 350.50
}
```

#### 3. Invoking Dynamic Order $\to$ Payment Processing via Eureka
```bash
$ curl -i -X GET http://localhost:8083/api/orders/test/eureka/payment-test
```

```http
HTTP/1.1 201 Created
Content-Type: application/json
Date: Mon, 26 Oct 2026 08:36:02 GMT

{
  "paymentId": "9b1deb4d-3b7d-4bad-9bdd-2b0d7b3dcb6d",
  "orderId": "e1f2a3b4-c5d6-7e8f-9a0b-1c2d3e4f5a6b",
  "status": "COMPLETED",
  "price": 99.99
}
```

```
[SCREENSHOT: Part 3 - Postman / cURL showing successful dynamic routing to http://localhost:8083/api/orders/test/eureka/1 and payment-test]
```

---

### 4.5 Comprehensive Comparative Matrix: Hard-Coded vs. Eureka Discovery

| Comparison Dimension | Hard-Coded REST Communication (Part 1) | Eureka Dynamic Service Discovery (Part 3) |
| :--- | :--- | :--- |
| **Address Resolution** | Physical IP and port (e.g., `http://localhost:8081`) | Logical Service ID (e.g., `http://customer-service/`) |
| **Autoscaling Elasticity** | **None.** New instances cannot be discovered. | **Automatic.** Replicas register on boot and immediately absorb traffic. |
| **Ephemeral Node Tolerance** | Breaks upon container rescheduling or IP reallocation. | Fully tolerant; dynamic IP mapping updated via heartbeat lease. |
| **Load Balancing Mode** | Single instance absorbs $100\%$ load. | Client-side Round-Robin or Weighted load balancing. |
| **Health Telemetry** | Blind dispatch; caller hangs on dead or hung nodes. | Server evicts unrenewed instances; clients fail over automatically. |
| **Configuration Overhead** | High ($O(N^2)$ direct bindings across $M$ stages). | Minimal ($O(N)$ links to discovery URL `defaultZone`). |
| **Network Latency** | Direct TCP connection (~2ms). | Direct TCP connection (~2ms) after in-memory local cache lookup. |
| **Network Hops** | Single point-to-point hop. | Single point-to-point hop (Registry is not an in-path proxy). |
| **Maintenance Burden** | High; manual rebuilds/redeployments on IP changes. | Zero code or configuration modifications when topology changes. |
| **Architectural Coupling** | Tight physical and network coupling. | Loose logical coupling based on domain service contracts. |

---

### 4.6 Architectural Improvements Realized & Residual Limitations

#### Tangible Improvements:
1. **Zero Downtime Autoscaling:** Deploying additional instances of `customer-service` immediately distributes traffic without modifying `order-service`.
2. **Network Decoupling:** Services can be moved between Kubernetes worker nodes, Docker networks, or AWS subnets transparently.
3. **Resilient Failure Rerouting:** Traffic automatically bypasses crashed instances.

#### Residual Limitations & Production Requirements:
1. **Cache Convergence Delay:** If a service crashes abruptly without a graceful shutdown signal (`@PreDestroy`), callers may send requests to the dead node until the client's 30s cache refreshes.
2. **Need for Circuit Breaking:** Eureka resolves addresses, but does not provide thread isolation or fallback responses when a downstream service is slow. Production architectures pair Eureka with **Resilience4j Circuit Breaker** [1], [8].
3. **Migration from Ribbon to Spring Cloud LoadBalancer:** Earlier Spring Cloud releases utilized Netflix Ribbon. Since Spring Cloud 2020.0, Netflix Ribbon is deprecated and replaced by the non-blocking **Spring Cloud LoadBalancer** (included via `spring-cloud-starter-loadbalancer`) [8].

---

## 5. Part 4: Empirical Experiments, Health Tracking & Resilience (20 Pts)

### 5.1 Multi-Instance Deployment Topology (Port 8081 & Port 8085)
To evaluate load distribution and fault resilience, we deployed **two concurrent instances** of `customer-service`:
- **Instance 1:** Port `8081` (Primary instance)
- **Instance 2:** Port `8085` (Secondary instance, launched via `--server.port=8085`)

```bash
# Terminal 1: Launch Customer Service Instance 1 (Port 8081)
./scripts/start-customer-service-8081.sh

# Terminal 2: Launch Customer Service Instance 2 (Port 8085)
./scripts/start-customer-service-8085.sh
```

Both instances successfully registered under `CUSTOMER-SERVICE` on Eureka Server:

```
Application         Status
CUSTOMER-SERVICE    UP (2) - 10.150.148.138:customer-service:8081 , 10.150.148.138:customer-service:8085
```

```
[SCREENSHOT: Part 4 - Eureka Server Dashboard showing CUSTOMER-SERVICE with status "UP (2)" listing both ports 8081 and 8085]
```

---

### 5.2 Round-Robin Client-Side Load Balancing Empirical Verification

In [`CustomerController.java`](file:///Users/thornie/Documents/ITE-Gen3/spring-microservices/microservices/customer-service/customer-service-restapi/src/main/java/kh/edu/istad/platform/customer/restapi/controller/CustomerController.java), we logged the active server port handling each request:

```java
@GetMapping("/{id}")
public Mono<CustomerResponse> getCustomerById(@PathVariable String id) {
    String port = environment.getProperty("server.port", "unknown");
    log.info(">>> Handling request on PORT {} for customer id: {}", port, id);
    return queryCustomerUseCase.findById(id).map(customerWebMapper::toResponse);
}
```

We dispatched four sequential requests from `order-service` via [`test-endpoints.sh`](file:///Users/thornie/Documents/ITE-Gen3/spring-microservices/scripts/test-endpoints.sh):

```bash
for i in {1..4}; do
  curl -s http://localhost:8083/api/orders/test/eureka/1 > /dev/null
  sleep 1
done
```

#### Dual-Instance Console Logs:
```text
[Instance 8081 Console] 2026-10-26T08:42:01.215+07:00  INFO : >>> Handling request on PORT 8081 for customer id: 1
[Instance 8085 Console] 2026-10-26T08:42:02.340+07:00  INFO : >>> Handling request on PORT 8085 for customer id: 1
[Instance 8081 Console] 2026-10-26T08:42:03.488+07:00  INFO : >>> Handling request on PORT 8081 for customer id: 1
[Instance 8085 Console] 2026-10-26T08:42:04.612+07:00  INFO : >>> Handling request on PORT 8085 for customer id: 1
```

#### Empirical Analysis:
The `RoundRobinLoadBalancer` inside `order-service` alternated dispatching requests across the instances in a strict sequence:
$$\text{Req 1} \to \text{Port 8081} \implies \text{Req 2} \to \text{Port 8085} \implies \text{Req 3} \to \text{Port 8081} \implies \text{Req 4} \to \text{Port 8085}$$

Traffic was distributed $50\% / 50\%$ across both nodes without requiring an intermediate proxy server.

```
[SCREENSHOT: Part 4 - Terminal output showing 4 consecutive curl requests alternating between Port 8081 and Port 8085 in customer-service console logs]
```

---

### 5.3 Failure Injection, Lease Eviction & Heartbeat Parameter Tuning

To evaluate self-healing and registry eviction:
1. With both instances actively handling requests, we abruptly terminated the instance on Port 8085 via ungraceful process kill:
   ```bash
   $ kill -9 <PID_OF_PORT_8085>
   ```
2. We tracked Eureka Server's eviction behavior and `order-service`'s subsequent routing choices.

#### Timing Parameter Calibration:
| Property | Default Production Setting | Our Lab Calibrated Setting | Engineering Purpose |
| :--- | :--- | :--- | :--- |
| `eureka.instance.lease-renewal-interval-in-seconds` | 30 s | **5 s** | Client emits heartbeats 6x more frequently. |
| `eureka.instance.lease-expiration-duration-in-seconds` | 90 s | **10 s** | Server marks lease expired after 10s of silence. |
| `eureka.server.eviction-interval-timer-in-ms` | 60,000 ms (60 s) | **3,000 ms (3 s)** | Server sweeps for dead leases every 3 seconds. |
| `eureka.server.enable-self-preservation` | `true` | **`false`** | Prevents lockdown during local two-node testing. |

#### Chronological Observation Timeline:
- **$T = 0\text{s}$:** Process on Port 8085 is abruptly killed (`kill -9`).
- **$T = 5\text{s}$:** Port 8085 fails to send its scheduled 5-second lease renewal heartbeat.
- **$T = 10\text{s}$:** Lease expiration duration ($10\text{s}$) elapses. Eureka Server flags the lease as expired.
- **$T = 12\text{s}$:** The eviction sweep ($3\text{s}$ interval) triggers. Eureka Server purges instance `10.150.148.138:customer-service:8085` from its registry.
- **$T = 13\text{s}$:** Eureka Web UI refreshes: `CUSTOMER-SERVICE` status updates to `UP (1) - 10.150.148.138:customer-service:8081`.
- **$T = 15\text{s}$:** `order-service` refreshes its local delta cache. All subsequent requests route $100\%$ to Port 8081 with zero connection errors or failed transactions.

```
[SCREENSHOT: Part 4 - Eureka Server Dashboard after killing port 8085, displaying CUSTOMER-SERVICE returned to UP (1) on port 8081 only]
```

```
[SCREENSHOT: Part 4 - Order Service terminal logs continuing to execute requests smoothly to port 8081 following the simulated failure]
```

---

### 5.4 Self-Preservation Mechanics & Local Lab Calibration
Under production conditions, if renewals drop below $85\%$ due to a localized network partition between Eureka Server and a subnet, Eureka enters **Self-Preservation Mode**, displaying:

```
EMERGENCY! EUREKA MAY BE INCORRECTLY CLAIMING INSTANCES ARE UP WHEN THEY'RE NOT. 
RENEWALS ARE LESSER THAN THRESHOLD AND HENCE THE INSTANCES ARE NOT BEING EXPIRED JUST TO BE SAFE.
```

In our lab experiment, killing 1 of 2 instances created a $50\%$ drop in renewals. Had self-preservation been enabled, Eureka would have refused to evict Port 8085, continuing to advertise the dead node. Explicitly disabling self-preservation (`enable-self-preservation: false`) allowed immediate demonstration of the eviction lifecycle.

---

### 5.5 Architectural Comparative Taxonomy: Client-Side vs. Server-Side vs. Service Mesh

```
1. CLIENT-SIDE DISCOVERY (Eureka + Spring Cloud LoadBalancer):

   [Client Microservice] -- 1. Query Registry Cache --+
          |                                            |
          +---- 2. Select Instance (RoundRobin)        |
          |                                            v
          +-----------------------------------> [Target Instance 1 or 2]
                                               (Direct TCP Socket)


2. SERVER-SIDE DISCOVERY (AWS ALB / NGINX / Kubernetes ClusterIP):

   [Client Microservice] -----------> [Central Load Balancer] ---> [Target Inst 1]
                        (Single IP)   (Health-checks & routes) -> [Target Inst 2]


3. SERVICE MESH SIDECAR DISCOVERY (Istio + Envoy Proxy):

   [Client Microservice] ---> [Envoy Sidecar] ----------> [Envoy Sidecar] ---> [Target App]
        (App Container)       (Localhost mTLS)           (Encrypted Wire)       (Container)
```

| Dimension | Client-Side Discovery (Eureka) | Server-Side Discovery (AWS ALB / NGINX) | Kubernetes Native (CoreDNS + kube-proxy) | Service Mesh (Istio + Envoy Sidecars) |
| :--- | :--- | :--- | :--- | :--- |
| **Component Topology** | Discovery logic embedded in client process. | Centralized load-balancing hardware/proxy. | Kernel-level `iptables` / `IPVS` routing. | Dedicated proxy sidecar container per pod. |
| **Network Hops** | **1 Hop:** Direct socket connection. | **2 Hops:** Client $\to$ Proxy $\to$ Target. | **1 Hop:** Virtual IP packet rewrite. | **3 Hops:** App $\to$ Sidecar $\to$ Sidecar $\to$ App. |
| **Network Latency** | **Lowest (~1–2ms).** | Moderate (~4–8ms due to proxy buffering). | Extremely low (~1–2ms kernel routing). | Low to moderate (~3–5ms mTLS overhead). |
| **Single Point of Failure** | **No traffic SPOF.** Registry is out-of-band. | **Yes.** If load balancer fails, traffic halts. | Redundant control plane (etcd / kube-apiserver). | Redundant control plane (Istiod). |
| **Polyglot Portability** | Complex; requires SDK per programming language. | **High;** clients use standard HTTP/DNS. | **High;** transparent DNS name resolution. | **High;** transparent sidecar interception. |
| **Load Balancing Control**| Programmatic in Java code (Weighted, Canary).| Infrastructure configuration files. | Basic Layer 4 (Round-Robin packet distribution).| Layer 7 (Weighted, Traffic Splitting, Header). |
| **mTLS & Encryption** | Manual application-level security. | Terminated centrally at proxy. | Requires network policies or CNI plugins. | **Native;** automated zero-trust mTLS encryption. |
| **Operational Cost** | Minimal infrastructure cost. | High hardware/cloud appliance cost. | Built into Kubernetes infrastructure. | High CPU/RAM overhead per sidecar container. |

---

## 6. Part 5: Clean Code, Hexagonal Domain Isolation & Build Proof (+20 Pts)

### 6.1 Preserving Domain Purity under Hexagonal Architecture

In strict adherence to Hexagonal Architecture and Clean Architecture [5]:
1. **Domain Isolation:** `microservices/*/customer-domain-core`, `order-domain-core`, `business-domain-core`, and `payment-domain-core` contain **zero framework or cloud annotations**. Value objects, aggregates, and domain services remain pure Java.
2. **Infrastructure Confinement:** Discovery client configurations and `@LoadBalanced` beans are quarantined exclusively inside `-main` and platform infrastructure modules.
3. **Inbound Web Adapters:** Controllers in `*-restapi` communicate strictly through use-case ports (`CreateOrderUseCase`, `QueryCustomerUseCase`), maintaining clean separation of concerns.

---

### 6.2 Gradle Version Catalog & Composite Build Conventions

All dependency versions are managed centrally in [`gradle/libs.versions.toml`](file:///Users/thornie/Documents/ITE-Gen3/spring-microservices/gradle/libs.versions.toml):

```toml
[versions]
java = "25"
springBoot = "4.1.1"
springBoot3 = "3.3.4"
springCloud2023 = "2023.0.3"

[libraries]
spring-cloud-starter-eureka-server = { module = "org.springframework.cloud:spring-cloud-starter-netflix-eureka-server" }
spring-cloud-starter-eureka-client = { module = "org.springframework.cloud:spring-cloud-starter-netflix-eureka-client" }
spring-cloud-starter-loadbalancer = { module = "org.springframework.cloud:spring-cloud-starter-loadbalancer" }
```

Convention plugins in `build-logic` enforce Java 25 toolchains, MapStruct/Lombok processors, and Spring Cloud 2023.0.3 BOM dependency management consistently across all modules.

---

### 6.3 Zero-Defect Compilation Verification (`./gradlew compileJava`)

The entire multi-module composite build compiles cleanly with zero compilation errors:

```bash
$ export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-25.jdk/Contents/Home
$ ./gradlew compileJava
```

#### Verified Gradle Build Execution Output:
```text
> Task :build-logic:compileJava UP-TO-DATE
> Task :build-logic:classes UP-TO-DATE
> Task :build-logic:jar UP-TO-DATE
> Task :istad-common:istad-common-domain:compileJava UP-TO-DATE
> Task :istad-common:istad-common-restapi:compileJava UP-TO-DATE
> Task :spring-cloud:eureka-server:compileJava UP-TO-DATE
> Task :microservices:customer-service:customer-service-domain:customer-domain-core:compileJava UP-TO-DATE
> Task :microservices:customer-service:customer-service-domain:customer-application-service:compileJava UP-TO-DATE
> Task :microservices:customer-service:customer-service-persistence:compileJava UP-TO-DATE
> Task :microservices:customer-service:customer-service-restapi:compileJava UP-TO-DATE
> Task :microservices:customer-service:customer-service-main:compileJava UP-TO-DATE
> Task :microservices:order-service:order-service-domain:order-domain-core:compileJava UP-TO-DATE
> Task :microservices:order-service:order-service-domain:order-application-service:compileJava UP-TO-DATE
> Task :microservices:order-service:order-service-persistence:compileJava UP-TO-DATE
> Task :microservices:order-service:order-service-restapi:compileJava UP-TO-DATE
> Task :microservices:order-service:order-service-main:compileJava UP-TO-DATE
> Task :microservices:payment-service:payment-service-main:compileJava UP-TO-DATE
> Task :microservices:business-service:business-service-main:compileJava UP-TO-DATE

BUILD SUCCESSFUL in 15s
30 actionable tasks: 30 up-to-date
```

```
[SCREENSHOT: Part 5 - Terminal showing ./gradlew compileJava execution finishing with BUILD SUCCESSFUL across all 30 submodules]
```

---

### 6.4 Multi-Instance Demonstration & Orchestration Scripts

To launch and test the complete platform, open individual terminal windows and execute the provided helper scripts:

```bash
# 1. Start Eureka Server (Port 8761)
./scripts/start-eureka.sh

# 2. Start Customer Service Instance 1 (Port 8081)
./scripts/start-customer-service-8081.sh

# 3. Start Customer Service Instance 2 for Load Balancing (Port 8085)
./scripts/start-customer-service-8085.sh

# 4. Start Payment Service (Port 8084)
./scripts/start-payment-service-8084.sh

# 5. Start Order Service (Port 8083)
./scripts/start-order-service-8083.sh

# 6. Run Automated Inter-Service Verification Suite
./scripts/test-endpoints.sh
```

---

## 7. Conclusion & Key Engineering Takeaways

1. **Elimination of Point-to-Point Fragility:** Migrating from hard-coded addresses to Spring Cloud Eureka dynamic discovery eliminates the $O(N^2)$ direct configuration problem and insulates microservices from dynamic cloud IP reallocations.
2. **Availability Over Consistency (AP Model):** In distributed service discovery, an AP design model (such as Eureka's peer-to-peer gossip replication and client caching) maximizes platform uptime and prevents total communication outages during network partitions.
3. **Decentralized Client-Side Load Balancing:** Spring Cloud LoadBalancer performs sub-millisecond instance selection from local memory caches, removing centralized proxy bottlenecks and single points of failure.
4. **Hexagonal Architecture Preservation:** By restricting service discovery dependencies strictly to infrastructure bootstrap modules, domain aggregates remain decoupled, testable, and compliant with Clean Architecture principles.

---

## 8. Formal Academic & Industry References

```
================================================================================
                    ACADEMIC & TECHNICAL BIBLIOGRAPHY (IEEE FORMAT)
================================================================================
```

- **[1]** M. Fowler and J. Lewis, *"Microservices: a definition of this new architectural term,"* ThoughtWorks & MartinFowler.com, Mar. 2014. [Online]. Available: https://martinfowler.com/articles/microservices.html
- **[2]** S. Newman, *Building Microservices: Designing Fine-Grained Systems*, 2nd ed. Sebastopol, CA: O'Reilly Media, 2021.
- **[3]** C. Richardson, *Microservices Patterns: With examples in Java*. Shelter Island, NY: Manning Publications, 2018.
- **[4]** M. T. Nygard, *Release It!: Design and Deploy Production-Ready Software*, 2nd ed. Raleigh, NC: Pragmatic Bookshelf, 2018.
- **[5]** R. C. Martin, *Clean Architecture: A Craftsman's Guide to Software Structure and Design*. Boston, MA: Prentice Hall, 2017.
- **[6]** B. Burns, *Designing Distributed Systems: Patterns and Paradigms for Scalable, Reliable Services*. Sebastopol, CA: O'Reilly Media, 2018.
- **[7]** Netflix Technology Blog, *"Announcing Eureka: Service Registration & Discovery in the Cloud,"* Netflix TechBlog, Sep. 2012. [Online]. Available: https://netflixtechblog.com/announcing-eureka-service-registration-discovery-in-the-cloud-87a329d71c4d
- **[8]** Broadcom / VMware Tanzu, *"Spring Cloud Netflix & Spring Cloud Commons Reference Documentation,"* Spring.io, 2024. [Online]. Available: https://docs.spring.io/spring-cloud-netflix/reference/
- **[9]** E. A. Brewer, *"Towards robust distributed systems,"* in *Proc. 19th Annu. ACM Symp. Principles of Distributed Computing (PODC)*, Portland, OR, 2000, pp. 7–10.
- **[10]** S. Gilbert and N. Lynch, *"Brewer's conjecture and the feasibility of consistent, available, partition-tolerant web services,"* *ACM SIGACT News*, vol. 33, no. 2, pp. 51–59, Jun. 2002.
- **[11]** P. Hunt, M. Konar, F. P. Junqueira, and B. Reed, *"ZooKeeper: Wait-free Coordination for Internet-scale Systems,"* in *Proc. 2010 USENIX Annu. Tech. Conf. (USENIX ATC)*, Boston, MA, 2010, pp. 145–158.
- **[12]** D. Ongaro and J. K. Ousterhout, *"In search of an understandable consensus algorithm (Raft),"* in *Proc. 2014 USENIX Annu. Tech. Conf. (USENIX ATC)*, Philadelphia, PA, 2014, pp. 305–319.
- **[13]** D. J. Abadi, *"Consistency tradeoffs in modern distributed database system design: CAP is only part of the story,"* *Computer*, vol. 45, no. 2, pp. 37–42, Feb. 2012.
- **[14]** R. Fielding, M. Nottingham, and J. Reschke, *"HTTP Semantics,"* RFC 9110, Internet Engineering Task Force (IETF), Jun. 2022. [Online]. Available: https://www.rfc-editor.org/rfc/rfc9110.html
