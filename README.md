### adaptive traffic signal simulator

## description

Adaptive Traffic Signal Simulator is a Java 21 command-line simulation engine for a four-way road intersection.

The system reads traffic commands from a JSON file, simulates lane-based vehicle queues, selects safe traffic phases, and writes the required output JSON containing vehicles that left the intersection on each simulation step.

The project focuses on clean Java design, deterministic simulation, adaptive scheduling, safety validation, automated tests, Docker-based execution, and GitHub Actions CI.

## architecture

The project is split into two Gradle modules.

* `simulator-core` contains the framework-independent simulation engine, domain model, queues, phases, safety validation, and schedulers.
* `simulator-cli` contains the command-line adapter responsible for reading JSON input and writing JSON output.

![architecture](docs/traffic-simulator-architecture.svg)

## simulation model

The simulator models a single intersection with four incoming roads:

* `north`
* `south`
* `east`
* `west`

Each road has three logical lanes:

* `LEFT`
* `STRAIGHT`
* `RIGHT`

Vehicles are assigned to lanes based on their movement:

* left turn goes to the left lane
* straight movement goes to the straight lane
* right turn goes to the right lane

Each lane is represented as a FIFO queue, so vehicles leave in the same order in which they arrived on that lane.

![intersection-model](docs/intersection-model.svg)

## commands

The simulator supports two command types.

`addVehicle` adds a vehicle to the correct lane queue.

```json
{
  "type": "addVehicle",
  "vehicleId": "vehicle1",
  "startRoad": "south",
  "endRoad": "north"
}
```

`step` advances the simulation by one logical time unit.

```json
{
  "type": "step"
}
```

Only `step` commands produce entries in the output `stepStatuses` array.

## input and output

Input file format:

```json
{
  "commands": [
    {
      "type": "addVehicle",
      "vehicleId": "vehicle1",
      "startRoad": "south",
      "endRoad": "north"
    },
    {
      "type": "step"
    }
  ]
}
```

Output file format:

```json
{
  "stepStatuses": [
    {
      "leftVehicles": [
        "vehicle1"
      ]
    }
  ]
}
```

## traffic phases

Traffic lights are modeled as safe phases instead of independent boolean flags.

Default phases:

* `NS_STRAIGHT_RIGHT`
* `EW_STRAIGHT_RIGHT`
* `N_LEFT`
* `S_LEFT`
* `E_LEFT`
* `W_LEFT`

Example phase:

```text
NS_STRAIGHT_RIGHT:
  NORTH -> SOUTH
  SOUTH -> NORTH
  NORTH -> WEST
  SOUTH -> EAST
```

Protected left turns are handled as separate phases. 

![traffic-phases](docs/traffic-phases.svg)

## safety validation

The simulator validates traffic phases before running the simulation.

Safety validation is handled by:

* `ConflictMatrix`
* `PhaseValidator`

The model rejects unsafe phase definitions, such as crossing movements from different axes or left turns combined with conflicting opposing traffic.


![safety-validation](docs/safety-validation.svg)

## adaptive scheduler

The default controller is an adaptive deficit-based phase scheduler inspired by weighted round-robin and deficit round-robin scheduling.

It is not a pure weighted round-robin implementation. Instead of using a fixed cycle, each phase accumulates dynamic deficit based on current traffic conditions.

The scheduler considers:

* base phase weight
* number of vehicles that can be served by the phase
* maximum waiting time of serviceable vehicles
* number of skipped rounds
* minimum and maximum green duration
* switch threshold
* service cost after vehicles pass

Simplified scoring model:

```text
dynamicWeight =
    baseWeight
  + queueWeight * serviceableVehicles
  + waitingTimeWeight * maxWaitingTime
  + skippedPhaseWeight * skippedRounds
```

After a phase is selected and vehicles leave the intersection, its deficit is reduced by service cost.

This makes the controller responsive to traffic volume while still preventing starvation of low-traffic phases.

![adaptive-scheduler](docs/adaptive-scheduler.svg)

## run locally

Run all tests:

```bash
./gradlew test
```

On Windows:

```powershell
.\gradlew.bat test
```

Run the sample simulation:

```bash
./gradlew :simulator-cli:run --args="examples/sample-input.json out/sample-output.json"
```

On Windows:

```powershell
.\gradlew.bat :simulator-cli:run --args="examples/sample-input.json out/sample-output.json"
```

The result will be written to:

```text
out/sample-output.json
```

## docker

The project includes a Dockerfile for running the CLI simulator in a container.

Build the image:

```bash
docker build -t adaptive-traffic-simulator .
```

Run the sample simulation:

```bash
docker run --rm \
  -v "$PWD/examples:/app/examples" \
  -v "$PWD/out:/app/out" \
  adaptive-traffic-simulator \
  examples/sample-input.json out/sample-output.json
```

On Windows PowerShell:

```powershell
docker run --rm `
  -v "${PWD}\examples:/app/examples" `
  -v "${PWD}\out:/app/out" `
  adaptive-traffic-simulator `
  examples/sample-input.json out/sample-output.json
```

## CI pipeline

GitHub Actions validates the project on every push.

The pipeline runs:

* Gradle test suite
* application build
* sample simulation smoke test
* Docker image build smoke test

![ci-pipeline](docs/ci-pipeline.svg)

## testing

The test suite covers:

* road parsing and road relationships
* turn direction resolution
* lane assignment
* vehicle validation
* command validation
* FIFO lane queues
* simulation engine behavior
* required output shape
* CLI input/output integration
* default phase definitions
* conflict matrix behavior
* phase safety validation
* adaptive scheduler behavior

Key tested scenarios include:

* one `stepStatus` per `step` command
* vehicles can leave the intersection
* opposite safe movements can leave in the same step
* FIFO order is preserved within a lane
* duplicate vehicle IDs are rejected
* invalid phases are rejected
* default phases are safe
* adaptive scheduler respects green constraints
* adaptive scheduler prefers phases with higher serviceable traffic
* skipped phases are eventually served


## tech stack

[![My Stack](https://skillicons.dev/icons?i=java,gradle,docker,githubactions&theme=light)](https://skillicons.dev)
