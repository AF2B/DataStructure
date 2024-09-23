# Queue in Clojure: From Fundamentals to Advanced Concepts

## Table of Contents
1. [Introduction](#introduction)
2. [Internal Structure](#internal-structure)
3. [Basic Operations](#basic-operations)
4. [Advanced Concepts](#advanced-concepts)
5. [Best Practices](#best-practices)
6. [Performance Considerations](#performance-considerations)
7. [Real-world Examples](#real-world-examples)

## Introduction

A queue is a fundamental data structure in computer science that follows the First-In-First-Out (FIFO) principle. It is an abstract data type that serves as a collection of elements, with two main operations: enqueue (to add an element to the rear of the queue) and dequeue (to remove an element from the front of the queue).

## Internal Structure

In Clojure, queues can be implemented using various data structures. The language provides built-in support for persistent queues through the `clojure.lang.PersistentQueue` class, which is the basis for the `clojure.core/PersistentQueue` type.

The internal structure of Clojure's `PersistentQueue` is based on two main components:
1. A persistent vector for the front of the queue
2. A persistent list for the rear of the queue

This hybrid approach allows for efficient operations at both ends of the queue.

## Basic Operations

### Creation and Initialization

In Clojure, we can create an empty queue using `clojure.lang.PersistentQueue/EMPTY`:

```clojure
(def empty-queue clojure.lang.PersistentQueue/EMPTY)
```

### Enqueue Operation

To add an element to the queue (enqueue), we use the `conj` function:

```clojure
(defn enqueue [q item]
  (conj q item))

;; Usage
(def q (-> empty-queue
           (enqueue 1)
           (enqueue 2)
           (enqueue 3)))
```

### Dequeue Operation

To remove and return the first element from the queue (dequeue), we use the `pop` function:

```clojure
(defn dequeue [q]
  (pop q))

;; Usage
(def new-q (dequeue q))
(def first-item (peek q))
```

### Peek Operation

To view the first element without removing it, use the `peek` function:

```clojure
(defn front [q]
  (peek q))

;; Usage
(println (front q)) ; prints 1
```

### Empty Check

To check if the queue is empty:

```clojure
(defn empty-queue? [q]
  (empty? q))

;; Usage
(println (empty-queue? q)) ; prints false
```

### Size

To get the size of the queue:

```clojure
(defn queue-size [q]
  (count q))

;; Usage
(println (queue-size q)) ; prints 3
```

## Advanced Concepts

### Custom Queue Implementation

While Clojure provides a built-in persistent queue, we can create a custom queue implementation for educational purposes:

```clojure
(defrecord CustomQueue [front rear])

(defn create-queue []
  (CustomQueue. '() '()))

(defn custom-enqueue [{:keys [front rear] :as q} item]
  (if (empty? front)
    (CustomQueue. (list item) '())
    (CustomQueue. front (conj rear item))))

(defn custom-dequeue [{:keys [front rear] :as q}]
  (if (empty? front)
    (throw (IllegalStateException. "Queue is empty"))
    (let [new-front (rest front)]
      (if (empty? new-front)
        (CustomQueue. (reverse rear) '())
        (CustomQueue. new-front rear)))))

(defn custom-peek [{:keys [front]}]
  (if (empty? front)
    (throw (IllegalStateException. "Queue is empty"))
    (first front)))

;; Usage
(def cq (-> (create-queue)
            (custom-enqueue 1)
            (custom-enqueue 2)
            (custom-enqueue 3)))

(println (custom-peek cq)) ; prints 1
(def new-cq (custom-dequeue cq))
(println (custom-peek new-cq)) ; prints 2
```

### Priority Queue

Clojure doesn't have a built-in priority queue, but we can implement one using a sorted set:

```clojure
(defn create-priority-queue []
  (sorted-set))

(defn pq-enqueue [pq item]
  (conj pq item))

(defn pq-dequeue [pq]
  [(first pq) (disj pq (first pq))])

(defn pq-peek [pq]
  (first pq))

;; Usage
(def pq (-> (create-priority-queue)
            (pq-enqueue [3 "Low priority"])
            (pq-enqueue [1 "High priority"])
            (pq-enqueue [2 "Medium priority"])))

(println (pq-peek pq)) ; prints [1 "High priority"]
```

## Best Practices

1. Use `clojure.lang.PersistentQueue/EMPTY` for most queue operations, as it's optimized for performance.
2. Prefer using built-in functions like `conj`, `pop`, and `peek` for queue operations.
3. When implementing custom queues, ensure they are immutable and persistent.
4. Use appropriate data structures for specific use cases (e.g., sorted-set for priority queues).
5. Leverage Clojure's sequence abstractions when working with queues.

## Performance Considerations

1. Enqueue operation: O(1) amortized time complexity.
2. Dequeue operation: O(1) amortized time complexity.
3. Peek operation: O(1) time complexity.
4. Space complexity: O(n), where n is the number of elements in the queue.

The amortized constant time for enqueue and dequeue operations is achieved through the hybrid structure of `PersistentQueue`, which uses both a vector and a list internally.

## Real-world Examples

### Example 1: Breadth-First Search

```clojure
(defn bfs [graph start]
  (loop [queue (conj clojure.lang.PersistentQueue/EMPTY start)
         visited #{start}
         result []]
    (if (empty? queue)
      result
      (let [vertex (peek queue)
            neighbors (remove visited (get graph vertex []))
            new-queue (into (pop queue) neighbors)
            new-visited (into visited neighbors)]
        (recur new-queue new-visited (conj result vertex))))))

;; Usage
(def graph {:A [:B :C]
            :B [:A :D :E]
            :C [:A :F]
            :D [:B]
            :E [:B :F]
            :F [:C :E]})

(println (bfs graph :A)) ; prints [:A :B :C :D :E :F]
```

### Example 2: Job Queue Simulator

```clojure
(defn create-job [id duration]
  {:id id :duration duration})

(defn process-job [job]
  (Thread/sleep (* 1000 (:duration job)))
  (println "Completed job" (:id job)))

(defn job-queue-simulator [jobs num-workers]
  (let [job-queue (into clojure.lang.PersistentQueue/EMPTY jobs)]
    (doall
     (pmap (fn [worker-id]
             (loop [queue job-queue]
               (when-let [job (peek queue)]
                 (process-job job)
                 (recur (pop queue)))))
           (range num-workers)))))

;; Usage
(def jobs [(create-job 1 2)
           (create-job 2 1)
           (create-job 3 3)
           (create-job 4 2)
           (create-job 5 1)])

(job-queue-simulator jobs 2)
```

### Example 3: Event-driven Simulation

```clojure
(defrecord Event [time description])

(defn create-event-queue []
  (sorted-set-by #(compare (:time %1) (:time %2))))

(defn add-event [queue event]
  (conj queue event))

(defn process-next-event [queue]
  (when-let [event (first queue)]
    (println "Time:" (:time event) "- Event:" (:description event))
    (disj queue event)))

(defn run-simulation [initial-events end-time]
  (loop [time 0
         event-queue (reduce add-event (create-event-queue) initial-events)]
    (if (or (> time end-time) (empty? event-queue))
      (println "Simulation complete")
      (let [current-event (first event-queue)
            new-queue (process-next-event event-queue)]
        (recur (max time (:time current-event)) new-queue)))))

;; Usage
(def initial-events
  [(Event. 0 "Simulation start")
   (Event. 2 "Customer arrival")
   (Event. 4 "Server free")
   (Event. 7 "Customer departure")])

(run-simulation initial-events 10)
```

These examples demonstrate the versatility and power of queues in real-world scenarios, from graph traversal to job processing and event-driven simulations. The use of Clojure's persistent data structures and functional programming paradigms showcases how queues can be leveraged to solve complex problems efficiently in a functional context.